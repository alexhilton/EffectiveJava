package com.effectivejava.rtti;

import java.util.Arrays;
import java.util.List;

public class ShapeTest {
    public static void main(String[] args) {
        List<Shape> shapes = Arrays.asList(new Circle(), new Square(), new Triangle());
        for (Shape s : shapes) {
            s.draw();
        }
        for (Shape s : shapes) {
            s.rotate();
        }
    }
}

abstract class Shape {
    void draw() {
        System.out.println(this + ".draw()");
    }
    
    void rotate() {
        final Class c = this.getClass();
        if (c.getSimpleName().equals("Circle")) {
            System.out.println("cannot rotate a circle");
            return;
        }
        System.out.println(this + ".rotate()");
    }
    
    protected boolean hilighted;
    abstract public String toString();
}

class Circle extends Shape {
    @Override
    public String toString() {
        return "Circle";
    }
}

class Square extends Shape {
    @Override
    public String toString() {
        return "Square";
    }
}

class Triangle extends Shape {
    @Override
    public String toString() {
        return "Triangle";
    }
}