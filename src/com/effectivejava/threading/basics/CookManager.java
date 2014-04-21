package com.effectivejava.threading.basics;

import java.util.ArrayList;
import java.util.List;

public class CookManager implements Runnable {
    public static final int TIME_TO_COOK = 200;
    private boolean knockedOff;
    private List<Cook> availableCooks;
    private Resturaunt resturaunt;
    private int cookLimit;
    private List<Order> ordersToPrepare;
    
    public CookManager(Resturaunt r, int cl) {
        knockedOff = false;
        resturaunt = r;
        cookLimit = cl;
        availableCooks = new ArrayList<Cook>(cookLimit);
        availableCooks.add(new Cook("Cook #1", this));
        availableCooks.add(new Cook("Cook #2", this));
        availableCooks.add(new Cook("Cook #3", this));
        ordersToPrepare = new ArrayList<Order>();
        new Thread(this).start();
    }
    
    public void prepareFood(Order o) {
        synchronized (ordersToPrepare) {
            ordersToPrepare.add(o);
            ordersToPrepare.notify();
        }
    }
    
    public void foodPrepared(Cook shef, Order o) {
        synchronized (availableCooks) {
            availableCooks.add(shef);
            availableCooks.notify();
        }
        resturaunt.foodPrepared(o);
    }
    
    public void run() {
        System.out.println("Cook manager start working...");
        while (!knockedOff) {
            synchronized (ordersToPrepare) {
                while (ordersToPrepare.size() < 1) {
                    try {
                        ordersToPrepare.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("OrdersToPrepare: " + ordersToPrepare);
                Cook shef = null;
                for (Cook cook : availableCooks) {
                    if (!cook.isCooking()) {
                        cook.prepareFood(ordersToPrepare.remove(0));
                        shef = cook;
                        break;
                    }
                }
                synchronized (availableCooks) {
                    availableCooks.remove(shef);
                }
            }
            
            synchronized (availableCooks) {
                while (availableCooks.size() < 1) {
                    try {
                        availableCooks.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Cook manager knocked off");
    }
    
    public void knockOff() {
        for (Cook c : availableCooks) {
            c.knockOff();
        }
        knockedOff = true;
    }
    
    private class Cook implements Runnable {
    	private String name;
    	private boolean knockedOff;
    	private Order order;
    	private CookManager manager;
    	public Cook(String n, CookManager m) {
    		name = n;
    		manager = m;
    		order = null;
    		new Thread(this).start();
    	}
    	
    	public void prepareFood(Order o) {
    		synchronized (this) {
    			System.out.println(this + "Start Cooking " + o);
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
    			System.out.println(this + "Cooking " + order);
    			try {
    				Thread.sleep(CookManager.TIME_TO_COOK);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			System.out.println(this + "Food " + order + " is cooked, ready to deliver");
    			final Order o = order;
    			synchronized (this) {
    				order = null;
    			}
    			manager.foodPrepared(this, o);
    		}
    	}
    	
    	public void knockOff() {
    		knockedOff = true;;
    	}
    	
    	public boolean isCooking() {
    		return order != null;
    	}
    	
    	@Override
    	public String toString() {
    		return name + " ";
    	}
    }
}