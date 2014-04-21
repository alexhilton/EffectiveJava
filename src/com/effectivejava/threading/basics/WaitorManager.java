package com.effectivejava.threading.basics;

import java.util.ArrayList;
import java.util.List;

public class WaitorManager implements Runnable {
    public static final int TIME_TO_DELIVER = 50;
    private boolean knockedOff;
    private int waitorLimit;
    private List<Waitor> availableWaitors;
    private List<Order> ordersToDeliver;
    
    public WaitorManager(int wl) {
        knockedOff = false;
        waitorLimit = wl;
        availableWaitors = new ArrayList<Waitor>(waitorLimit);
        availableWaitors.add(new Waitor("Waitor #1", this));
        availableWaitors.add(new Waitor("Waitor #2", this));
        ordersToDeliver = new ArrayList<Order>();
        new Thread(this).start();
    }
    
    public void deliverFood(Order o) {
        synchronized (ordersToDeliver) {
            ordersToDeliver.add(o);
            ordersToDeliver.notify();
        }
    }
    
    public void foodDelivered(Waitor w) {
        synchronized (availableWaitors) {
            availableWaitors.add(w);
            availableWaitors.notify();
        }
    }
    
    public void run() {
        System.out.println("Waitor manager start working...");
        while (!knockedOff) {
            synchronized (ordersToDeliver) {
                while (ordersToDeliver.size() < 1) {
                    try {
                        ordersToDeliver.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                System.out.println("ordertodeliver: " + ordersToDeliver);
                Waitor w = null;
                for (Waitor waitor : availableWaitors) {
                    if (!waitor.isDelivering()) {
                        waitor.deliverFood(ordersToDeliver.remove(0));
                        w = waitor;
                        break;
                    }
                }
                
                synchronized (availableWaitors) {
                    availableWaitors.remove(w);
                }
            }
            
            synchronized (availableWaitors) {
                while (availableWaitors.size() < 1) {
                    try {
                        availableWaitors.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Waitor manager knocked off");
    }
    
    public void knockOff() {
        for (Waitor w : availableWaitors) {
            w.knockOff();
        }
        knockedOff = true;
    }
    
    private class Waitor implements Runnable {
    	private String name;
    	private Order order;
    	private boolean knockedOff;
    	private WaitorManager manager;
    	
    	public Waitor(String n, WaitorManager m) {
    		name = n;
    		order = null;
    		manager = m;
    		new Thread(this).start();
    	}
    	
    	public void deliverFood(Order o) {
    		synchronized (this) {
    			System.out.println(this + "Start delivering " + o);
    			order = o;
    			notify();
    		}
    	}
    	
    	public void run() {
    		while (!knockedOff) {
    			synchronized (this) {
    				while (order == null) {
    					try {
    						wait();
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
    				}
    			}
    			System.out.println(this + " delivering " + order);
    			try {
    				Thread.sleep(WaitorManager.TIME_TO_DELIVER);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			System.out.println(this + "Food " + order + " is delivered, food is ready");
    			synchronized (this) {
    				order.ready();
    				order = null;
    			}
    			manager.foodDelivered(this);
    		}
    	}
    	
    	public void knockOff() {
    		knockedOff = true;
    	}
    	
    	public boolean isDelivering() {
    		synchronized (this) {
    			return order != null;
    		}
    	}
    	
    	@Override
    	public String toString() {
    		return name + " ";
    	}
    }
}