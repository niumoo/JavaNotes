package com.wdbyte.enum2;

/**
 * 矩形枚举类
 */
public enum Rectangle implements Shape {
    SMALL(3, 4),
    MEDIUM(4, 5),
    LARGE(5, 6);

    private int length;
    private int width;

    Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public double getArea() {
        return length * width;
    }

    public static void main(String[] args) {
        Shape shape = Rectangle.LARGE;
        double shapeArea = shape.getArea();
        System.out.println(shapeArea);
    }
}
