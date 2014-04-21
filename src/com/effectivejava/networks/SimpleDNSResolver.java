package com.effectivejava.networks;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimpleDNSResolver {
	public static void main(String[] args) {  
		try	{
			if (args.length > 0) {  
				String host = args[0];
				InetAddress[] addresses = InetAddress.getAllByName(host);
				for (InetAddress a : addresses)
					System.out.println(a);
			} else	{  
				InetAddress localHostAddress = InetAddress.getLocalHost();
				System.out.println(localHostAddress);
			}
		} catch (Exception e) {  
			e.printStackTrace();
		}
		
		launchGui();
	}
	
	private static void launchGui() {
		JFrame frame = new JFrame("DNS Resolver");
		JPanel panel = new JPanel();
		final JTextField text = new JTextField();
		text.setColumns(16);
		text.setEditable(true);

		final JLabel result = new JLabel();
		result.setText("Please input a URL, then press 'Resolve' to get Host");
		result.setAutoscrolls(true);

		JButton resolve = new JButton("Resolve");
		resolve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				final String url = text.getText();
				try {
					if (url.equals("")) {
						result.setText(InetAddress.getLocalHost().toString());
					} else {
						final InetAddress[] addrs = InetAddress.getAllByName(url);
						StringBuilder buffer = new StringBuilder();
						for (InetAddress a : addrs) {
							buffer.append(a.toString());
							buffer.append("\r\n");
						}
						result.setText(buffer.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		panel.add(text);
		panel.add(resolve);
		panel.add(exit);
		frame.add(panel, BorderLayout.NORTH);
		frame.add(result, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(480, 320);
		frame.setVisible(true);
	}
}