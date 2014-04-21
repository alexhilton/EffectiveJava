package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Simple simulation of project management based on single-producer
 * and single-consumer module in order to cooperate multi-tasks with
 * notify() and notifyAll().
 * Project --- the data between producer and consumer.
 * Manager produces(plans) a project.
 * Engineer consumes(implements) a project.
 * Tester who is another chain of the product line tests the project.
 */
public class ProjectManagement {
	private int series;
	private Project project;
	private Manager manager;
	private Engineer engineer;
	private Tester tester;
	private boolean needTest;
	
	public ProjectManagement() {
		series = 0;
		project = null;
		needTest = false;
		manager = new Manager();
		engineer = new Engineer();
		tester = new Tester();
	}
	
	/*
	 * Start work
	 */
	public void work() {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(engineer);
		exec.execute(manager);
		exec.execute(tester);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		exec.shutdownNow();
		exec.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ProjectManagement().work();
	}
	
	/*
	 * Class representing a project, the 
	 */
	private class Project {
		private String name;
		private int id;
		
		@SuppressWarnings("unused")
		public Project() {}
		
		public Project(final String n, final int i) {
			name = n;
			id = i;
		}
		
		public String toString() {
			return "Project[" + id + "] -- " + name + id;
		}
	}
	
	/*
	 * Manager -- the producer
	 */
	private class Manager implements Runnable {
		public void run() {
			try {
				while (!Thread.interrupted()) {
					synchronized (this) {
						while (project != null || needTest) {
							wait();
						}
					}
					// No need synchronized or lock here
					final String pn = "Destruction #";
					System.out.println("producing new project: " + pn + ++series);
					// the time needed for new project
					Thread.sleep((long) (Math.random()*100));
					project = new Project(pn, series);
					System.out.println("new project is ready: " + project.toString());
					
					synchronized (engineer) {
						engineer.notify();
					}
					
					// After completing one project, all are gonna rest for a while
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				System.out.println("Manager exit via InterruptedException: " + 
						e.getMessage());
			}
		}
	}
	
	/*
	 * Engineer -- the consumer
	 */
	private class Engineer implements Runnable {
		public void run() {
			try {
				while (!Thread.interrupted()) {
					synchronized (this) {
						while (project == null) {
							wait();
						}
					}
					
					System.out.println("implementing project: " + project.toString());
					// the time needed for consuming project
					Thread.sleep((long) (Math.random()*100));
					System.out.println(project.toString() + " is implemented");
					
					System.out.println("need to test: " + project.toString());
					needTest = true;
					synchronized (tester) {
						tester.notify();
					}
//					project = null;
//					synchronized (manager) {
//						manager.notify();
//					}
					
					// After completing one project, all people involved 
					// are gonna rest for a while
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				System.out.println("engineer exit via InterruptedException: " + 
						e.getMessage());
			}
		}
	}
	
	/*
	 * Tester
	 * Test a product after Engineer finishes a project.
	 */
	private class Tester implements Runnable {
		public void run() {
			try {
				while (!Thread.interrupted()) {
					synchronized (this) {
						while (!needTest) {
							wait();
						}
					}
					System.out.println("testing project: " + project.toString());
					Thread.sleep((long) (Math.random()*100));
					System.out.println("finish testing project: " + project.toString());
					needTest = false;
					project = null; // should it be here?

					synchronized (manager) {
						manager.notify();
					}

					// After completing one project, all people involved 
					// are gonna rest for a while
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				System.out.println("tester exit via InterruptedException: " + 
						e.getMessage());
			}
		}
	}
}
