package com.effectivejava.threading.basics;

import junit.framework.TestCase;

public class ResturauntTest extends TestCase {
    private Resturaunt resturaunt;
    /*
     * TODO:
     * time to cook: 200 ms
     * time to deliver: 50 ms
     * tolerance is 100 ms
     * If tolerance is less than 100, cases will fail.
     * These three parameters are important for these tests to pass, if you change time2cook or time2deliver,
     * cases would fail.
     */
    private int tolerance = 100; // in ms
    public ResturauntTest() {
        super("ResturauntTest");
    }
    
    @Override
    public void setUp() {
        resturaunt = new Resturaunt("Hilton's Place");
    }
    
    @Override
    public void tearDown() {
        resturaunt.closeDoor();
    }
    
    public void testOneOrder() throws InterruptedException {
        System.out.println("============ start testing one orders =============");
        final Order order[] = createOrders(1);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(order[0] + " should be ready", order[0].isReady());
    }
    
    public void testTwoOrders() throws InterruptedException {
        System.out.println("============ start testing two orders =============");
        final Order orders[] = createOrders(2);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " hsould is ready", orders[0].isReady());
        assertTrue(orders[1] + " is ready", orders[1].isReady());
    }
    
    public void testThreeOrders() throws InterruptedException {
        System.out.println("============ start testing three orders =============");
        final Order orders[] = createOrders(3);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " is ready", orders[0].isReady());
        assertTrue(orders[1] + " is ready", orders[1].isReady());
        
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " is ready", orders[2].isReady());
    }
    
    public void testFourOrders() throws InterruptedException {
        System.out.println("============== start testing four orders ===============");
        final Order orders[] = createOrders(4);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " should be ready", orders[0].isReady());
        assertTrue(orders[1] + " should be ready too", orders[1].isReady());
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " should be ready now", orders[2].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[3] + " is ready yet", orders[3].isReady());
    }
    
    public void testFiveOrders() throws InterruptedException {
        System.out.println("======================start testing five orders=====================");
        final Order orders[] = createOrders(5);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " should be ready", orders[0].isReady());
        assertTrue(orders[1] + " should be ready too", orders[1].isReady());
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " should be ready now", orders[2].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[3] + " is ready yet", orders[3].isReady());
        assertTrue(orders[4] + " is ready", orders[4].isReady());
    }
    
    public void testSixOrders() throws InterruptedException {
        System.out.println("======================start testing six orders=====================");
        final Order orders[] = createOrders(6);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " should be ready", orders[0].isReady());
        assertTrue(orders[1] + " should be ready too", orders[1].isReady());
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " should be ready now", orders[2].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[3] + " is ready yet", orders[3].isReady());
        assertTrue(orders[4] + " is ready", orders[4].isReady());
        
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[5] + " is ready", orders[5].isReady());
    }

    public void testSevenOrders() throws InterruptedException {
        System.out.println("======================start testing seven orders=====================");
        final Order orders[] = createOrders(7);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " should be ready", orders[0].isReady());
        assertTrue(orders[1] + " should be ready too", orders[1].isReady());
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " should be ready now", orders[2].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[3] + " is ready yet", orders[3].isReady());
        assertTrue(orders[4] + " is ready", orders[4].isReady());
        
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[5] + " is ready", orders[5].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[6] + " is ready", orders[6].isReady());
    }
    
    public void testEightOrders() throws InterruptedException {
        System.out.println("======================start testing eight orders=====================");
        final Order orders[] = createOrders(8);
        int timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[0] + " should be ready", orders[0].isReady());
        assertTrue(orders[1] + " should be ready too", orders[1].isReady());
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[2] + " should be ready now", orders[2].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[3] + " is ready yet", orders[3].isReady());
        assertTrue(orders[4] + " is ready", orders[4].isReady());
        
        timeToWait = tolerance;
        timeToWait += WaitorManager.TIME_TO_DELIVER;
        Thread.sleep(timeToWait);
        assertTrue(orders[5] + " is ready", orders[5].isReady());
        
        timeToWait = tolerance;
        timeToWait += CookManager.TIME_TO_COOK;
        Thread.sleep(timeToWait);
        assertTrue(orders[6] + " is ready", orders[6].isReady());
        assertTrue(orders[7] + " is ready", orders[7].isReady());
    }
    
    private Order[] createOrders(int count) {
        Order orders[] = new Order[count];
        for (int i = 0; i < count; i++) {
            final Order o = new Order("Pizza " + "#" + i);
            resturaunt.submitOrder(o);
            assertFalse(o + " not ready", o.isReady());
            orders[i] = o;
        }
        return orders;
    }
}