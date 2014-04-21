package com.effectivejava.rtti;

public class HierarchyDetector {
    public static String analyze(Class<? extends Object> clazz) {
        final Class<? extends Object> base = clazz.getSuperclass();
        if (base == null) {
            return "";
        } else {
            return "->" + base.getSimpleName() + analyze(base);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("object" + analyze(Object.class));
        System.out.println("this" + analyze(HierarchyDetector.class));
        System.out.println("test" + analyze(HierarchyDetectorTest.class));
        
        char[] array = new char[2];
        if (HierarchyDetector.analyze(array.getClass()).equals("")) {
            System.out.println(" array is a primitive type");
        } else {
            System.out.println(" array is a object subclassing Object");
            System.out.println("array" + HierarchyDetector.analyze(array.getClass()));
        }
    }
}
