package com.effectivejava.networks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UrlConnection {
    public static void main(String[] args) {
        try {
            String urlName;
            if (args.length > 0) {
                urlName = args[0];
            } else {
                urlName = "http://www.archermind.com/index.php";
            }
            final URL url = new URL(urlName);
            final URLConnection connection = url.openConnection();

            if (args.length > 2) {
                final String username = args[1];
                final String password = args[2];
                final String input = username + ":" + password;
                final String encoding = base64Encode(input);
                connection.setRequestProperty("Authorization", "Basic"
                        + encoding);
            }

            connection.connect();

            printHeaders(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHeaders(URLConnection connection)
            throws IOException {
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            final String key = entry.getKey();
            for (String value : entry.getValue()) {
                System.out.println(key + ":" + value);
            }
        }

        System.out.println("=======================================");
        System.out.println("getContentType: " + connection.getContentType());
        System.out
                .println("getContentLength: " + connection.getContentLength());
        System.out.println("getcontentencoding: "
                + connection.getContentEncoding());
        System.out.println("getcontentdate " + connection.getDate());
        System.out.println("getexpiration: " + connection.getExpiration());
        System.out.println("getlastmodified: " + connection.getLastModified());
        System.out.println("=======================================");
        
        InputStream is = connection.getInputStream();
//        Scanner scanner = new Scanner(is);
//        for (int i = 1; scanner.hasNextLine() && i <= 10; i++) {
//            System.out.println(scanner.nextLine());
//        }
//        if (scanner.hasNextLine()) {
//            System.out.println("......");
//        }

        File file = new File("webpage.html");
        System.out.println(file.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[8096];
        while (is.read(buffer) != -1) {
            fos.write(buffer);
        }
        is.close();
        fos.close();
    }

    private static String base64Encode(String buffer) {
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        Base64OutputStream out = new Base64OutputStream(byteout);
        try {
            out.write(buffer.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteout.toString();
    }
}

class Base64OutputStream extends FilterOutputStream {
    private int[] buffer = new int[3];
    private int index = 0;
    private int column = 0;

    private char[] toBase64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z' };

    public Base64OutputStream(OutputStream out) {
        super(out);
    }

    public void write(int c) throws IOException {
        buffer[index] = c;
        index++;
        if (index == 3) {
            super.write(toBase64[(buffer[0] & 0xFC) >> 2]);
            super.write(toBase64[((buffer[0] & 0x03) << 4)
                    | ((buffer[1] & 0xF0) >> 4)]);
            super.write(toBase64[((buffer[1] & 0x0F) << 4)
                    | ((buffer[2] & 0xFC) >> 4)]);
            super.write(toBase64[buffer[2] & 0x3F]);
            column += 4;
            index = 0;
            if (column >= 76) {
                super.write('\n');
                column = 0;
            }
        }
    }

    public void flush() throws IOException {
        if (index == 1) {
            super.write(toBase64[(buffer[0] & 0xFC) >> 2]);
            super.write(toBase64[(buffer[0] & 0x03) << 4]);
            super.write('=');
            super.write('=');
        } else if (index == 2) {
            super.write(toBase64[(buffer[0] & 0xFC) >> 2]);
            super.write(toBase64[((buffer[0] & 0x03) << 4)
                    | ((buffer[1] & 0xF0) >> 4)]);
            super.write(toBase64[buffer[1] & 0x0F] << 2);
            super.write('=');
        }
    }
}