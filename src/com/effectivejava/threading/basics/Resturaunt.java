package com.effectivejava.threading.basics;

/*
 * From this demo, we learn the lesson that why TDD is so good in development practice.
 * Without TDD, you are not assured that your programs work correctly.
 * TDD can, at least, assure you that your programs work.
 */
public class Resturaunt {
    private String name;
    private int cookLimit;
    private int waitorLimit;
    private CookManager cookManager;
    private WaitorManager waitorManager;
    
    public Resturaunt(String name) {
        this.name = name;
        cookLimit = 3;
        waitorLimit = 2;
        System.out.println("Resturation: " + name + " is open now, Welcome everyone!");
        cookManager = new CookManager(this, cookLimit);
        waitorManager = new WaitorManager(waitorLimit);
    }
    
    public void submitOrder(Order o) {
        cookManager.prepareFood(o);
    }
    
    /*
     * There is a problem: waitor #1 always do more work than waitor #2.
     * Because food delivery is quick to finish, so when food is preparing, waitor #1 finishes its
     * delivery and back to queue. When food is prepared, it is waitor #1 to deliver because it is
     * on the head of the queue.
     * Solution:
     * When waitor or cook is about to deliver or cook, remove it from the queue and push it onto
     * the queue after it finishes.
     */
    public void foodPrepared(Order o) {
        waitorManager.deliverFood(o);
    }
    
    public void closeDoor() {
        cookManager.knockOff();
        waitorManager.knockOff();
        System.out.println("Resturant: " + name + " is closed now, welcome to come tomorrow!");
    }
}