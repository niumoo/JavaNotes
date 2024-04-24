package com.wdbyte.assert1;

public class InitializationDemo {

    static {
        init();
    }

    static void init() {
        System.out.println("Static initialization block called");
        // 假设这里有一个重要的初始化逻辑
        // 这个方法错误地在静态初始化之前被调用了
        assert isProperlyInitialized() : "System not properly initialized";
    }

    static boolean isProperlyInitialized() {
        // 这里返回 false 模拟系统未被正确初始化
        return false;
    }

    public InitializationDemo() {
        System.out.println("Constructor called");
    }

    public static void main(String[] args) {
        new InitializationDemo();
    }
}
