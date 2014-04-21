package com.effectivejava.basics;

public class EnumerationTest {
    private Orientation mDirection;
    
    public enum Orientation {
        Horizontal,
        Vertical
    };
    
    public void setOrientation(Orientation dir) {
        mDirection = dir;
    }
    
    public Orientation getOrientation() {
        return mDirection;
    }
    
    public static void main(String[] args) {
        EnumerationTest tester = new EnumerationTest();
        tester.setOrientation(Orientation.Horizontal);
        switch (tester.getOrientation()) {
        case Horizontal:
            // Handle horizontal
            break;
        case Vertical:
            // Handle vertical
            break;
        default:
            break;
        }
    }
}