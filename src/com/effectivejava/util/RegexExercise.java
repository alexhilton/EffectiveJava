package com.effectivejava.util;

public class RegexExercise {
    public static void main(String[] args) {
        final String original = "This is a somehting with an <img src=\"test.png\"/> in it";
        final String expect = "This is a somehting with an image in it";
        
        final String actual = original.replace("<img[^>]+\\>", "image");
        if (!expect.equals(actual)) {
            System.out.println("Wrong: expected <" + expect + ">, but was <" + actual + ">");
        } else {
            System.out.print("Okay");
        }
    }
}
