package com.effectivejava.threading.basics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * This simple program is the same to ProjectManagement with the coordinations
 * of tasks implemented with explicit Lock and Condition.
 */
public class ProjectManagementWithExplicits {
	private int series;
	private Project project;
	private Manager manager;
	private Engineer engineer;
	private Tester tester;
	private boolean needTest;
	
	public ProjectManagementWithExplicits() {
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
			TimeUnit.SECONDS.sleep(3);
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
		new ProjectManagementWithExplicits().work();
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
	private class Manager extends People implements Runnable {
		public Manager() {
			super();
		}
		
		public void run() {
			try {
				while (!Thread.interrupted()) {
//					acquire();
//					try {
//						while (project != null || needTest) {
//							holdon();
//						}
//					} finally {
//						release();
//					}
					waitFor(project != null || needTest);
					
					// No need synchronized or lock here
					final String pn = "Destruction #";
					System.out.println("producing new project: " + pn + ++series);
					// the time needed for new project
					Thread.sleep((long) (Math.random()*100));
					project = new Project(pn, series);
					System.out.println("new project is ready: " + project.toString());
					
//					engineer.acquire();
//					try {
//						engineer.anounce();
//					} finally {
//						engineer.release();
//					}
					engineer.signalFor();
					
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
	private class Engineer extends People implements Runnable {
		public Engineer() {
			super();
		}
		
		public void run() {
			try {
				while (!Thread.interrupted()) {
//					acquire();
//					try {
//						while (project == null) {
//							holdon();
//						}
//					} finally {
//						release();
//					}
					waitFor(project == null);
					
					System.out.println("consuming project: " + project.toString());
					// the time needed for consuming project
					Thread.sleep((long) (Math.random()*100));
					System.out.println(project.toString() + " is consumed");
					
					System.out.println("need to test: " + project.toString());
					needTest = true;
//					tester.acquire();
//					try {
//						tester.anounce();
//					} finally {
//						tester.release();
//					}
					tester.signalFor();
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
	private class Tester extends People implements Runnable {
		public Tester() {
			super();
		}
		
		public void run() {
			try {
				while (!Thread.interrupted()) {
//					acquire();
//					try {
//						while (!needTest) {
//							holdon();
//						}
//					} finally {
//						release();
//					}
					waitFor(!needTest);
						
					System.out.println("testing project: " + project.toString());
					Thread.sleep((long) (Math.random()*100));
					System.out.println("finish testing project: " + project.toString());
					needTest = false;
					project = null; // should it be here?

//					manager.acquire();
//					try {
//						manager.anounce();
//					} finally {
//						manager.release();
//					}
					manager.signalFor();

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
	
	@SuppressWarnings("unused")
	private class People {
		private Lock lock;
		private Condition cond;
		
		protected People() {
			lock = new ReentrantLock();
			cond = lock.newCondition();
		}
		
		protected void acquire() {
			lock.lock();
		}
		
		protected void release() {
			lock.unlock();
		}
		
		protected void holdon() throws InterruptedException {
			cond.await();
		}
		
		protected void anounce() {
			cond.signal();
		}
		
		protected void waitFor(final boolean condition) throws InterruptedException {
			lock.lock();
			try {
				while (condition) {
					cond.await();
				}
			} finally {
				lock.unlock();
			}
		}
		
		protected void signalFor() {
			lock.lock();
			try {
				cond.signalAll();
			} finally {
				lock.unlock();
			}
		}
	}
}