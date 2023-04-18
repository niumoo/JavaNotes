package com.wdbyte.oop.polymorphism.inter;

public class Cat implements Animal{
  @Override
  public void makeSound(){
    System.out.println("喵喵喵");
  }
}
