package com.effectivejava.threading.basics;

import junit.framework.TestCase;

public abstract class DelayedCheck implements Runnable {
    private static final long TIME_SLICE = 200;
    private long default_timeout = 3000;
    
    public DelayedCheck() {}
    
    public DelayedCheck(long timeout) {
        default_timeout = timeout;
    }
    
    protected abstract boolean check();
    
    public void run() {
        try {
            Thread.sleep(default_timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
            TestCase.fail("unexpected interrupted exception");
        }

        if (check()) {
            return;
        }

        TestCase.fail("Unexpected timeout");
    }
}