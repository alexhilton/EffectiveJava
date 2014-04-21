package com.effectivejava.networks;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.effectivejava.gui.ProgressChangedListener;

/*
 * CSDN blog worms.
 * # get all the urls of articles
 * # get the status of each article: read count, pub date, comments, comment count, originated, title
 * # process article: sharing to weibo or social networks
 * # read/access all the articles, in order to increase read count
 * # archive all the articles: download them to local and zip them
 * # add comment to the article
 */
public class CSDNBlogWorm {
	private static final String HOME_DIR = "/codeshop/workspace/EffectiveJava/";
	private static final String BLOG_HOST = "http://blog.csdn.net";
	private static final Pattern PATTERN_ARTICLE = Pattern.compile("<span\\s+?class=\"link_title\"><a\\s+?href=\"(.+?)\">");
	private static final Pattern PATTERN_COUNT = Pattern.compile("(\\d+?)条数据\\s+?共(\\d+?)页");
	private static final Pattern PATTERN_ORIGINATED = Pattern.compile("<span\\s+?class=\"ico\\s+?(.+?)\">");
	private static final Pattern PATTERN_READ_COUNT = Pattern.compile("阅读</a>\\((\\d+)\\)</span>");
	private static final Pattern PATTERN_COMMENT_COUNT = Pattern.compile("评论</a>\\((\\d+)\\)</span>");
	private static final Pattern PATTERN_PUB_DATE = Pattern.compile("link_postdate\">(.+?)</span>");
	private static final Pattern PATTERN_ARTICLE_COUNT = 
			Pattern.compile("<div\\s+?id=\"article_content\"\\s+?class=\"article_content\">([\\d\\D]+?)</div>");
	
	private static final int CONNECTION_TIMEOUT = 10 * 1000;
	
	// ATTENTION: without settting UA, 403(Forbidden) is returned.
	private static final String PROPERTY_USER_AGENT = "User-Agent";
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0";
	
	private enum StatusNameIndex {
		URL,
		TYPE,
		TITLE,
		PUB_DATE,
		READ_COUNT,
		COMMENT_COUNT,
	}
	
	private static final String[] STATUS_NAME = new String[] {
		"url",
		"type",
		"title",
		"pub_date",
		"read_count",
		"comment_count",
	};
	private static final String HTML_EXTENSION = ".html";
	private static final String TYPE_ORGINATED = "originated";
	private static final String TYPE_REPOSTED = "reposted";
	private final String articleListUrl;
	private final String userId;
	private final String statusFilename;
	private ArrayList<HashMap<String, String>> articleStatus;
	private ProgressChangedListener progressListener;
	
	public CSDNBlogWorm(String userId) {
		this.userId = userId;
		statusFilename = HOME_DIR + this.userId + "_blogstatus.xml";
		articleListUrl = BLOG_HOST + "/" + this.userId + "/article/list/";
		articleStatus = new ArrayList<HashMap<String, String>>();
		restoreBlogStatus();
	}
	
	private void restoreBlogStatus() {
		try {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(statusFilename)));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (!line.contains("<article")) {
						continue;
					}
					HashMap<String, String> status = new HashMap<String, String>();
					for (String attr : STATUS_NAME) {
						Pattern p = Pattern.compile(attr + "=\"(.+?)\"");
						Matcher m = p.matcher(line);
						while (m.find()) {
							status.put(attr, m.group(1));
						}
					}
					articleStatus.add(status);
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (FileNotFoundException e) {
			syncBlogStatus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dump() {
		for (HashMap<String, String> status : articleStatus) {
			System.out.println(status2String(status));
		}
		System.out.println("there are " + articleStatus.size() + " articles");
	}
	
	private String status2String(HashMap<String, String> status) {
		if (status == null || status.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String attr : STATUS_NAME) {
			sb.append("\n" + attr + " = " + status.get(attr));
		}
		return sb.toString();
	}
	
	public void accessArticles() {
		int count = 0;
		int prog = 0;
		for (HashMap<String, String> status : articleStatus) {
			final String type = status.get(STATUS_NAME[StatusNameIndex.TYPE.ordinal()]);
			if (!type.equals(TYPE_ORGINATED)) {
				continue;
			}
			accessArticle(status.get(STATUS_NAME[StatusNameIndex.URL.ordinal()]));
			if (progressListener != null) {
				progressListener.reportProgress(++prog);
			}
			count++;
		}
		System.out.println(count + " articles are updated successfully!");
	}
	
	private void accessArticle(String url) {
		try {
			HttpURLConnection conn = null;
			try {
				conn = openHttpConnection(url);
				System.out.println("Accessed article successfully!");
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readBlogStatus() {
		int pageCount = 1;
		for (int page = 1; pageCount > 0 && page <= pageCount; page++) {
			pageCount = readStatusInPage(page);
			if (progressListener != null) {
				progressListener.reportProgress(articleStatus.size());
			}
		}
	}
	
	private int readStatusInPage(int page) {
		int pageCount = 0;
		try {
			HttpURLConnection conn = null;
			BufferedReader reader = null;
			try {
				conn = openHttpConnection(articleListUrl + page);
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				pageCount = parseBlogStatus(reader);
			} finally {
				if (reader != null) {
					reader.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return pageCount;
	}

	private int parseBlogStatus(BufferedReader reader) throws IOException {
		int pageCount = 0;
		boolean titleFollowing = false;
		String line = null;
		HashMap<String, String> status = new HashMap<String, String>();
		while ((line = reader.readLine()) != null) {
			if (titleFollowing) {
				status.put(STATUS_NAME[StatusNameIndex.TITLE.ordinal()], line.trim());
				titleFollowing = false;
			}
			Matcher m = PATTERN_ORIGINATED.matcher(line);
			while (m.find()) {
				status.put(STATUS_NAME[StatusNameIndex.TYPE.ordinal()], 
						m.group(1).equals("ico_type_Repost") ? TYPE_REPOSTED : TYPE_ORGINATED);
			}
			m = PATTERN_ARTICLE.matcher(line);
			while (m.find()) {
				titleFollowing = true;
				status.put(STATUS_NAME[StatusNameIndex.URL.ordinal()], BLOG_HOST + m.group(1));
			}
			m = PATTERN_PUB_DATE.matcher(line);
			while (m.find()) {
				status.put(STATUS_NAME[StatusNameIndex.PUB_DATE.ordinal()], m.group(1));
			}
			m = PATTERN_READ_COUNT.matcher(line);
			while (m.find()) {
				status.put(STATUS_NAME[StatusNameIndex.READ_COUNT.ordinal()], m.group(1));
			}
			m = PATTERN_COUNT.matcher(line);
			while (m.find()) {
				pageCount = Integer.valueOf(m.group(2));
			}
			m = PATTERN_COMMENT_COUNT.matcher(line);
			while (m.find()) {
				status.put(STATUS_NAME[StatusNameIndex.COMMENT_COUNT.ordinal()], m.group(1));
				// end of an article, put it into map and clear data
				articleStatus.add(status);
				status = new HashMap<String, String>();
			}
		}
		return pageCount;
	}
	
	private void saveBlogStatus() {
		try {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(statusFilename));
				pw.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
				pw.println("<blog>");
				persistStatus(pw);
				pw.print("</blog>");
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void persistStatus(PrintWriter pw) {
		if (articleStatus == null) {
			return;
		}
		
		for (HashMap<String, String> status : articleStatus) {
			final StringBuilder sb = new StringBuilder();
			sb.append("\t<article");
			Iterator<Map.Entry<String, String>> it = status.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> item = it.next();
				sb.append(" " + item.getKey() + "=\"" + item.getValue() + "\"");
			}
			sb.append(" />");
			pw.println(sb.toString());
		}
	}
	
	public void syncBlogStatus() {
		articleStatus.clear();
		readBlogStatus();
		saveBlogStatus();
		System.out.print("sync blog status successfully: there are " + articleStatus.size() + " articles\n");
	}
	
	public void downloadArticles() {
		final String path = ensureDownloadPath();
		int prog = 0;
		for (HashMap<String, String> status : articleStatus) {
			downloadArticle(path, status);
			if (progressListener != null) {
				progressListener.reportProgress(++prog);
			}
		}
	}

	private void downloadArticle(String path, HashMap<String, String> status) {
		final String type = status.get(STATUS_NAME[StatusNameIndex.TYPE.ordinal()]);
		if (!type.equals(TYPE_ORGINATED)) {
			return;
		}
		final String filename = path + File.separator + ensureFilename(status);
		try {
			OutputStream out = null;
			final HttpURLConnection conn = openHttpConnection(status.get(STATUS_NAME[StatusNameIndex.URL.ordinal()]));
			InputStream in = null;
			try {
				out = new FileOutputStream(filename);
				in = conn.getInputStream();
				downloadArticleContent(in, out);
			} finally {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void downloadArticlePage(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int seg = 0;
		while ((seg = in.read(buf)) != -1) {
			out.write(buf, 0, seg);
		}
	}

	// Keep only the article content
	// TODO: how to handle programming source codes inside articles?
	private void downloadArticleContent(InputStream in, OutputStream out) throws IOException {
		String pageHtml = pageAsHtmlString(in);
		Matcher m = PATTERN_ARTICLE_COUNT.matcher(pageHtml);
		while (m.find()) {
			pageHtml = m.group(1);
		}
		final byte[] data = pageHtml.getBytes();
		out.write(data, 0, data.length);
	}
	
	private String pageAsHtmlString(InputStream in) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		return sb.toString();
	}

	private String ensureDownloadPath() {
		final String path = userId + "_blog_articles";
		final File p = new File(path);
		if (!p.exists()) {
			p.mkdirs();
		}
		return path;
	}
	
	public void backupArticles() {
		int prog = 0;
		final String filename = userId + "_articles.zip";
		try {
			ZipOutputStream zos = null;
			BufferedOutputStream out = null;
			try {
				zos = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(filename), new Adler32()));
				out = new BufferedOutputStream(zos);
				zos.setComment("Articles for blogger: " + userId);
				for (HashMap<String, String> status : articleStatus) {
					addArticleEntry(out, zos, status);
					if (progressListener != null) {
						progressListener.reportProgress(++prog);
					}
				}
			} finally {
				if (out != null) {
					out.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addArticleEntry(BufferedOutputStream out, ZipOutputStream zos, HashMap<String, String> status) throws IOException {
		final HttpURLConnection conn = openHttpConnection(status.get(STATUS_NAME[StatusNameIndex.URL.ordinal()]));
		String filename = ensureFilename(status);
		try {
			zos.putNextEntry(new ZipEntry(filename));
			downloadArticleContent(conn.getInputStream(), out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		conn.disconnect();
		out.flush();
	}

	// TODO: question: when does HttpUrlConnection connect to server? getResponseCode()?
	private HttpURLConnection openHttpConnection(final String url)	throws MalformedURLException, IOException {
		final URL u = new URL(url);
		final HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.addRequestProperty(PROPERTY_USER_AGENT, USER_AGENT);
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("Not good res code " + conn.getResponseCode() + ", res msg " + conn.getResponseMessage());
		}
		return conn;
	}

	private String ensureFilename(HashMap<String, String> status) {
		String filename = status.get(STATUS_NAME[StatusNameIndex.TITLE.ordinal()]);
		filename = filename.replaceAll("/", "_");
		filename = filename + HTML_EXTENSION;
		return filename;
	}

	public int getArticleCount() {
		return articleStatus == null ? 0 : articleStatus.size();
	}
	
	public void setProgressListener(ProgressChangedListener listener) {
		progressListener = listener;
	}
	
	public static void main(String[] args) {
		CSDNBlogWorm worm = new CSDNBlogWorm("hitlion2008");
		// loop 2 by default
		if (args.length < 1) {
			worm.accessArticles();
			worm.accessArticles();
			return;
		} 
		
		// -loop n
		if (args[0].equals("-loop")) {
			int loop = 2;
			loop = Integer.parseInt(args[1]);
			for (int i = 0; i < loop; i++) {
				worm.accessArticles();
			}
		} else if (args[0].equals("-sync")) {
			worm.syncBlogStatus();
		} else {
			System.out.println("Usage: blogworm -loop n");
		}
	}
}