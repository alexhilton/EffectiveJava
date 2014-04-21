package com.effectivejava.rtti;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class MethodExtractor {
    
    public MethodExtractor() {
        System.out.println("contructing MethodExtractor");
    }
    public static void main(String[] args) {
        final Class<?> c = java.lang.Class.class;
        Pattern pattern = Pattern.compile("\\w+\\.|\\bfinal\\s\\b|\\bnative\\s\\b");
        Method[] methods = c.getMethods();
        for (Method m : methods) {
            System.out.println(pattern.matcher(m.toString()).replaceAll(""));
        }
        Constructor<?>[] cons = c.getConstructors();
        for (Constructor<?> co : cons) {
            System.out.println(pattern.matcher(co.toString()).replaceAll(""));
        }
    }
}
