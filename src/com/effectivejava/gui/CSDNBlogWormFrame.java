package com.effectivejava.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.effectivejava.networks.CSDNBlogWorm;

public class CSDNBlogWormFrame extends JFrame {
	private final int SCREEN_WIDTH = 800;
	private final int SCREEN_HEIGHT = 600;
	private String blogId;
	private CSDNBlogWorm blogWorm;
	private final int DEFAULT_LOOP = 2;
	
	public CSDNBlogWormFrame(String id) {
		blogId = id;
		blogWorm = new CSDNBlogWorm(id);
		addUIComponents();
		setTitle("CSDN Blog Worm");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	final JButton access = new JButton("Access");
	final JButton syncStatus = new JButton("Sync status");
	final JButton downloadArticles = new JButton("Download articles");
	final JButton backupArticles = new JButton("Backup articles");
	final JProgressBar progress = new JProgressBar();
	final JTextField loopEditor = new JTextField("2", 2);
	
	private void addUIComponents() {
		final JPanel north = new JPanel();
		JLabel blogEditorLabel = new JLabel("BlogID:");
		north.add(blogEditorLabel);
		JTextField blogEditor = new JTextField(blogId, 8);
		north.add(blogEditor);
		JLabel loopEditorLabel = new JLabel("Loop: ");
		north.add(loopEditorLabel);
		north.add(loopEditor);
		north.add(access);
		north.add(syncStatus);
		north.add(downloadArticles);
		north.add(backupArticles);
		add(north, BorderLayout.NORTH);
		
		final JPanel center = new JPanel();
		progress.setStringPainted(true);
		progress.setBorderPainted(true);
		progress.setMaximum(blogWorm.getArticleCount());
		progress.setMinimum(0);
		progress.setBackground(Color.PINK);
		center.add(progress);
		add(center, BorderLayout.CENTER);
		blogWorm.setProgressListener(new ProgressChangedListener() {
			@Override
			public void reportProgress(int prog) {
				progress.setValue(prog);
			}
		});
		
		access.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				startTransaction();
				new Thread(new Runnable() {
					@Override
					public void run() {
						blogWorm.accessArticles();
						endTransaction();
					}
				}).start();
			}
		});
		
		syncStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startTransaction();
				new Thread(new Runnable() {
					@Override
					public void run() {
						blogWorm.syncBlogStatus();
						endTransaction();
					}
				}).start();
			}
		});
		
		downloadArticles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startTransaction();
				new Thread(new Runnable() {
					@Override
					public void run() {
						blogWorm.downloadArticles();
						endTransaction();
					}
				}).start();
			}
		});
		
		backupArticles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startTransaction();
				new Thread(new Runnable() {
					@Override
					public void run() {
						int loop = DEFAULT_LOOP;
						try {
							loop = Integer.parseInt(loopEditor.getText());
						} catch (NumberFormatException e) {
							e.printStackTrace();
							loop = DEFAULT_LOOP;
						}
						for (int i = 0; i < loop; i++) {
							blogWorm.backupArticles();
						}
						endTransaction();
					}
				}).start();
			}

		});
	}
	
	private void startTransaction() {
		access.setEnabled(false);
		downloadArticles.setEnabled(false);
		syncStatus.setEnabled(false);
		backupArticles.setEnabled(false);
		progress.setValue(0);
	}
	
	private void endTransaction() {
		access.setEnabled(true);
		downloadArticles.setEnabled(true);
		syncStatus.setEnabled(true);
		backupArticles.setEnabled(true);
	}
	
	public static void main(String[] args) {
		new CSDNBlogWormFrame("hitlion2008");
	}
}