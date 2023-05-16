package com.wdbyte.oop.interfac;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/13
 */
public class JavaInterface2 {
    public static void main(String[] args) {
        //XPhone xPhone = new XPhone();
        //xPhone.powerUp();
        //xPhone.call();
    }
}



/**
 * 手机系统功能接口
 */
interface IMobilePhoneSystem{
    // 开机
    void powerUp();
    // 显示
    void display();
    // 声音
    void sound();
}

/**
 * 手机基本功能
 */
interface IMobilePhoneBasicFunction extends IMobilePhoneSystem {
    // 打电话
    void call();
    // 发短信
    void sendMessage();
}


class XPhone implements IMobilePhoneBasicFunction{

    @Override
    public void powerUp() {
        System.out.println("XPhone 开始开机");
    }

    @Override
    public void display() {
        System.out.println("XPhone 开始显示");
    }

    @Override
    public void sound() {
        System.out.println("XPhone 发出声音");
    }

    @Override
    public void call() {
        System.out.println("XPhone 开始拨打电话");
    }

    @Override
    public void sendMessage() {
        System.out.println("XPhone 开始发送短信");
    }
}
