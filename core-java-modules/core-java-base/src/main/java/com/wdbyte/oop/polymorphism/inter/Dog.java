package com.wdbyte.oop.polymorphism.inter;

public class Dog implements Animal{
  @Override
  public void makeSound(){
    System.out.println("汪汪汪");
  }
}