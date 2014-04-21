package com.effectivejava.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileAnt {
    final String TARGET_DIR = File.separator + "assets" + File.separator;
    
    private String root;
    
    public FileAnt(String root) {
        this.root = new File(root).getAbsolutePath();
        processDirectory(new File(this.root));
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            new FileAnt(".");
        }
        for (String dir : args) {
            new FileAnt(dir);
        }
    }

    private void processDirectory(File current) {
        if (current.isDirectory()) {
            // process it recursively
            File[] subs = current.listFiles(new FileFilter() {
                Pattern pattern = Pattern.compile(".java|.xml|.png|.txt|.aidl|.mk");
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        // exclude /assets and /gen
                        return !(file.getName().equals("assets") ||
                                file.getName().equals("gen") ||
                                file.getName().equals("bin"));
                    }
                    String fn = file.getName();
//                    System.out.println(fn);
                    final int index = fn.lastIndexOf(".");
                    if (index == -1) {
                        // skip those file without an extension
                        return false;
                    }
                    String ext = fn.substring(index, fn.length());
                    return pattern.matcher(ext).matches();
                }
            });
            for (File sub : subs) {
                processDirectory(sub);
            }
        } else {
            processFile(current);
        }
    }

    private void processFile(File dir) {
        String filepath = dir.getPath();
        filepath = filepath.substring(root.length(), filepath.length());
        filepath = filepath.substring(0, filepath.lastIndexOf(File.separator));
        if (filepath != null && !filepath.equals("")) {
            filepath = File.separator + filepath + File.separator;
        } else {
            filepath = "";
        }
        final String filename = dir.getName();
//            filename = filename.toLowerCase();
//            filename = filename.replaceAll("[~, ]", "_");
        try {
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                input = new BufferedInputStream(new FileInputStream(dir));
                filepath = root + TARGET_DIR + filepath;
                final File out = new File(filepath);
                if (!out.exists()) {
                    out.mkdirs();
                }
                output = new BufferedOutputStream(new FileOutputStream(filepath + filename));
                byte[] data = new byte[8192];
                int seg = 0;
                while ((seg = input.read(data)) != -1) {
                    output.write(data, 0, seg);
                }
            } finally {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            }
        } catch (FileNotFoundException e) {
            //
        } catch (IOException e) {
            //
        } catch (Exception e) {
            
        }
    }
}