package com.effectivejava.threading.basics;

public class DelayedCheckTest {
    public static void main(String[] args) {
        new DelayedCheckTest().test();
    }
    
    public void test() {
        new DelayedCheck(5000) {
            int count = 0;
            protected boolean check() {
                count++;
                System.out.println("count is " + count);
                if (count == 25) {
                    System.out.println("fuck trigger db");
                }
                return false;
            }
        }.run();
    }
}