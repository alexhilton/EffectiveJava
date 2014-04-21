package com.effectivejava.basics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * NativeHelper --- a tool that helps you to develop with Android NDK.
 * It traverses all source codes in src, finds native methods, collects
 * native methods and writes them into the file and generate Android.mk
 * file. 
 * IMPORTANT: the java files must be able to compile and this program assumes that the delcaration of native method are in only one line
 * In all, it does:
 * 1) extract all native methods into one c file.
 * 2) all native methods in one java file into one c file
 * 3) method name convention: qualifier, return-type, Java_package-name_class-name_method-name(arguments)
 * 4) in Android.mk, lib module is named as the same to application name.
 * 5) TODO: update java sources by adding System.loadLibrary(), and support deeper package
 * This program must be invoked in the root directory of a project.
 * With this tool, all you have to do is write your java code, delcare native methods, run this tool, then you 
 * get your *.c files to implement. after implement navtive interfaces, run ndk-build to build. then all done.
 */
public class NativeHelper {
	private static String packageName;
	private static String javaClassName;
	private static String applicationName;
	private static final String JNI_DIRECTORY = "jni" + File.separator;
	private static final String SRC_DIRECTORY = "src" + File.separator;
	private static final String INDENT = "    "; // four spaces
	private static final String MAKEFILE = JNI_DIRECTORY + "Android.mk";
	
	private static StringBuilder nativeFiles = new StringBuilder();
	
	private static HashMap<String, String> javaTypeToNativeType = new HashMap<String, String>();
	
	static {
		javaTypeToNativeType.put("boolean", "jboolean");
		javaTypeToNativeType.put("byte", "jbyte");
		javaTypeToNativeType.put("char", "jchar");
		javaTypeToNativeType.put("short", "jshort");
		javaTypeToNativeType.put("int", "jint");
		javaTypeToNativeType.put("long", "jlong");
		javaTypeToNativeType.put("float", "jfloat");
		javaTypeToNativeType.put("double", "jdouble");
		javaTypeToNativeType.put("String", "jstring");
		javaTypeToNativeType.put("boolean[]", "jbooleanArray");
		javaTypeToNativeType.put("byte[]", "jbyteArray");
		javaTypeToNativeType.put("char[]", "jcharArray");
		javaTypeToNativeType.put("short[]", "jshortArray");
		javaTypeToNativeType.put("int[]", "jintArray");
		javaTypeToNativeType.put("long[]", "jlongArray");
		javaTypeToNativeType.put("float[]", "jfloatArray");
		javaTypeToNativeType.put("double[]", "jdoubleArray");
		javaTypeToNativeType.put("String[]", "jstringArray");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File jniDirectory = new File(JNI_DIRECTORY);
		if (!jniDirectory.exists()) {
			jniDirectory.mkdirs();
		}
		applicationName = getApplicationName(jniDirectory.getAbsolutePath());
		System.out.println("appplication " + applicationName);	
		
		File srcDirectory = new File(SRC_DIRECTORY);
		if (srcDirectory.exists()) {
			processDirectory(srcDirectory);
		}

		if (!isEmpty(nativeFiles.toString())) {
			generateMakefile();
		} else {
			// remove jni folder
			for (File entry : jniDirectory.listFiles()) {
				entry.delete();
			}
			jniDirectory.delete();
		}
	}
	
	// rename java file 'file.java' to another file 'file-tmp.java'
	// create a file with 'file.java'
	// read from 'file-tmp.java' and write to 'file.java'
	// add loadlibrary at begin or at end
	// delete 'file-tmp.java' finally
	// TODO: what if there are already the loadlibrary sentence? how to update it?
	private static void addLibraryToJavaFile(File originalFile) throws IOException {
		String filename = originalFile.getPath();
		File newFile = new File(filename + ".tmp");
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new FileReader(originalFile));
			out = new PrintWriter(new FileWriter(newFile));
			String buf;
			while ((buf = in.readLine()) != null) {
				out.println(buf);
				if (buf.startsWith("public class")) {
					out.println(INDENT + "static {");
					out.println(INDENT + INDENT + "System.loadLibrary(\"" + applicationName + "\");");
					out.println(INDENT + "}\n");
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			originalFile.delete();
			newFile.renameTo(originalFile);
		}
	}

	private static String getApplicationName(String absolutePath) {
		int index = absolutePath.lastIndexOf(File.separator);
		if (index == -1) {
			return "";
		}
		absolutePath = absolutePath.substring(0, index);
		index = absolutePath.lastIndexOf(File.separator);
		return absolutePath.substring(index+1, absolutePath.length());
	}

	private static void generateMakefile() {
		try {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(new File(MAKEFILE)));
				out.println("LOCAL_PATH := $(call my-dir)");
				out.println("include $(CLEAR_VARS)");
				out.print("LOCAL_MODULE    := ");
				out.println(applicationName);
				out.print("LOCAL_SRC_FILES :=");
				out.println(nativeFiles.toString());
				out.println("include $(BUILD_SHARED_LIBRARY)");
			} finally {
				if (out != null) {
					out.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void processDirectory(File current) {
		if (current.isDirectory()) {
			File[] entries = current.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String pathname) {
					Pattern p = Pattern.compile("$.java");
					Matcher m = p.matcher(pathname);
					return file.isDirectory() || m.matches();
				}
			});
			for (File entry : entries) {
				processDirectory(entry);
			}
		} else {
			processFile(current);
		}
	}
	
	private static void processFile(File file) {
		try {
			BufferedReader in = null;
			PrintWriter out = null;
			try {
				in = new BufferedReader(new FileReader(file));
				String line;
				boolean nativeFileCreated = false;
				while ((line = in.readLine()) != null) {
					if (line.startsWith("package")) {
						// get package name
						packageName = line.replaceAll("package|;", "").trim();
						System.out.println("package name: " + packageName);
						continue;
					}
					if (isNativeMethod(line)) {
						if (!nativeFileCreated) {
							out = new PrintWriter(new FileWriter(getNativeFile(packageName, file.getName())));
							out.println("#include <jni.h>\n");
							javaClassName = file.getName();
							javaClassName = javaClassName.substring(0, javaClassName.lastIndexOf("."));
							System.out.println("class name: " + javaClassName);
							nativeFileCreated = true;
						}
						out.println(getNativeMethod(line));
					}
				}
				
				if (nativeFileCreated) {
					// TODO: update java file, to add system.loadLibrary();
					addLibraryToJavaFile(file);
				}
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	// assume that native method is declared in only one line
	private static boolean isNativeMethod(String line) {
		line = line.trim();
		// what if 'native' appears in a comment
		return !(line.startsWith("//") || line.startsWith("*")) &&   // comments?
				line.contains(" native ") && // native qualifier
				line.contains(";") && line.contains("(") && line.contains(")") && // necessary for a method declaration
				!line.contains("\"");
	}
	
	// TODO: should add package name to the file in order to avoid duplicate
	private static File getNativeFile(String packageName, String javaFile) {
		String filename = javaFile.substring(0, javaFile.lastIndexOf("."));
		filename = filename + ".c";
		nativeFiles.append(" ");
		nativeFiles.append(filename);
		return new File(JNI_DIRECTORY + filename);
	}
	
	// input line is like:
	//    public native String getStringFromJNI();
	//    private native String getStatus(int arg);
	//    private native String getDataConnection(int type, int extra);
	// get return type
	// get method name
	// get arguments
	private static String getNativeMethod(String line) {
		line = line.replaceAll("private|public|static|native|;", "");
		line = line.trim();
		if (isEmpty(line)) {
			return "";
		}
		String[] parts = line.split(" ");
//		private native String getStatus();
//		String
//		getStatus()
		
//		private native int getNative(int type, int extra);
//		int
//		getNative(int
//		type,
//		int
//		extra)
	
//		private static native String whatFor(String why);
//		String
//		whatFor(String
//		why)
		StringBuilder sb = new StringBuilder();
		sb.append(getJNIType(parts[0]));
		sb.append(" ");
		sb.append(getMethodName(parts[1]));
		for (int i = 2; i < parts.length; i++) {
			if ((i & 0x01) == 0) {
				sb.append(parts[i]);
			} else {
				sb.append(getJNIType(parts[i]));
			}
			if (i < parts.length - 1) {
				sb.append(" ");
			}
		}
		sb.append(" {\n");
//		sb.append(INDENT + "// implementations\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	// methodName()  or
	// methodName(type
	private static String getMethodName(String line) {
		if (isEmpty(line)) {
			return "";
		}
		line.trim();
		int index = line.indexOf("(");
		if (index == -1) {
			return line;
		}
		String javaMethodName = line.substring(0, index);
		String type = line.substring(index+1, line.length());
		StringBuilder sb = new StringBuilder();
		sb.append("Java_");
		sb.append(packageName.replaceAll("\\.", "_"));
		sb.append("_");
		sb.append(javaClassName);
		sb.append("_");
		sb.append(javaMethodName);
		sb.append("(");
		sb.append("JNIEnv* env, jobject thiz"); // these two are necessary
		if (!type.equals(")")) {
			sb.append(", ");
		}
		sb.append(getJNIType(type));
		if (!type.equals(")")) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private static String getArguments(String line) {
		return null;
	}
	
	private static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}
	
	// should return original argument when no match found
	private static String getJNIType(String javaType) {
		String type = javaTypeToNativeType.get(javaType);
		return (type == null ? javaType : type);
	}
	// For test
	private native String getStatus();
	
	private native int getNative(int type, int extra);
	
	private static native String whatFor(String why);
}