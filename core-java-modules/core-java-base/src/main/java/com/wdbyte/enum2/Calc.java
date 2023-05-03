package com.wdbyte.enum2;

/**
 * 计算枚举类
 */
public enum Calc {
    // 加法
    PLUS {
        public int apply(int x, int y) {
            return x + y;
        }
    },
    // 减法
    MINUS {
        public int apply(int x, int y) {
            return x - y;
        }
    },
    // 乘法
    MULTIPLY {
        public int apply(int x, int y) {
            return x * y;
        }
    };

    public int apply(int x, int y){
        // todo
        return x + y;
    }
}
