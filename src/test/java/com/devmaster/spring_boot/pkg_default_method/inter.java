package com.devmaster.spring_boot.pkg_default_method;

public interface inter {
    public interface Shape{
        void draw();
        default void setColor(String color){
            System.out.println("Draw a shape with color: "+color);
        }
    }
    interface Interface1{
        default void method1(){
            System.out.println("Interface1.method1");
        }
    }
    interface Interface2{
        default void method2(){
            System.out.println("Interface2.method2");
        }
    }
    public class MultiInheristance implements Interface1, Interface2{
        @Override
        public void method1(){
            Interface1.super.method1();
        }
        @Override
        public void method2(){
            System.out.println("MultiInheristance.method2");
        }
        public static void main(String[] args){
            MultiInheristance mi = new MultiInheristance();
            mi.method1();
            mi.method2();
        }
    }
}
