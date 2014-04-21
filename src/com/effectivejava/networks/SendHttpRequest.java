package com.effectivejava.networks;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendHttpRequest {
    public static void main(String[] args) {
        JFrame frame = new SendHttpRequestFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class SendHttpRequestFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 640;

    /**
     * Makes a POST request and returns the server response.
     * 
     * @param urlString
     *            the URL to post to
     * @param nameValuePairs
     *            a map of name/value pairs to supply in the request.
     * @return the server reply (either from the input stream or the error
     *         stream)
     */
    public static String doPost(String urlString,
            Map<String, String> nameValuePairs) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        PrintWriter out = new PrintWriter(connection.getOutputStream());

        boolean first = true;
        for (Map.Entry<String, String> pair : nameValuePairs.entrySet()) {
            if (first)
                first = false;
            else
                out.print('&');
            String name = pair.getKey();
            String value = pair.getValue();
            out.print(name);
            out.print('=');
            out.print(URLEncoder.encode(value, "UTF-8"));
        }

        out.close();

        Scanner in;
        StringBuilder response = new StringBuilder();
        try {
            in = new Scanner(connection.getInputStream());
        } catch (IOException e) {
            if (!(connection instanceof HttpURLConnection))
                throw e;
            InputStream err = ((HttpURLConnection) connection).getErrorStream();
            if (err == null)
                throw e;
            in = new Scanner(err);
        }
        while (in.hasNextLine()) {
            response.append(in.nextLine());
            response.append("\n");
        }

        in.close();
        return response.toString();
    }

    public SendHttpRequestFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Post Data To Server");

        JPanel northPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);
        final JTextField url = new JTextField();
        url.setColumns(50);
        url.setText("http://www.google.com.hk/ig?");
        northPanel.add(url);

        final JTextArea result = new JTextArea();
        add(new JScrollPane(result));

        JButton getButton = new JButton("Get");
        northPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                new Thread(new Runnable() {
                    public void run() {
                        final String SERVER_URL = url.getText();
                        result.setText("");
                        Map<String, String> post = new HashMap<String, String>();
                        post.put("hl", "zh-CN");
                        post.put("q", "android");
                        try {
                            result.setText(doPost(SERVER_URL, post));
                        } catch (IOException e) {
                            result.setText("Exception caught: " + e);
                        }
                    }
                }).start();
            }
        });
    }
}