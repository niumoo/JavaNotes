package com.wdbyte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author niulang
 * @date 2021/08/02
 */
public class Java8UnaryOperatorParams {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "node", "c++", "rust", "www.wdbyte.com");
        // 转大写
        UnaryOperator<String> upperFun = s -> s.toUpperCase();
        // 截取 3 位
        UnaryOperator<String> subFun = s -> s.substring(0, 3);
        List<String> resultList = map(list, upperFun, subFun);
        System.out.println(resultList);
    }

    public static <T> List<T> map(List<T> list, UnaryOperator<T>... unaryOperator) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            for (UnaryOperator<T> operator : unaryOperator) {
                t = operator.apply(t);
            }
            resultList.add(t);
        }
        return resultList;
    }
}
