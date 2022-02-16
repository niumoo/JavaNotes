package com.wdbyte.string;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author niulang
 * @date 2022/02/16
 */
public class StringConcat {

    public static void main(String[] args) {
        System.out.println(concat());
        System.out.println(concat2());
        System.out.println(concat3());
        System.out.println(concat4());
        System.out.println(concat5());
        System.out.println(concat6());
        System.out.println(concat7());
    }

    public static String concat() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        String result = "";

        for (String value : values) {
            result = result + value;
        }
        return result;
    }

    public static String concat2() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        String result = String.join("", values);
        return result;
    }

    public static String concat3() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        String result = "";
        for (String value : values) {
            result = result + nullToString(value);
        }
        return result;
    }

    public static String concat4() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        String result = "";
        for (String value : values) {
            result = result.concat(nullToString(value));
        }
        return result;
    }

    public static String concat5() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        StringBuilder result = new StringBuilder();
        for (String value : values) {
            result = result.append(nullToString(value));
        }
        return result.toString();
    }

    public static String concat6() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        StringJoiner result = new StringJoiner("");
        for (String value : values) {
            result = result.add(nullToString(value));
        }
        return result.toString();
    }

    public static String concat7() {
        String[] values = {"https", "://", "www.", "wdbyte", ".com", null};
        String result = Arrays.stream(values)
            .filter(Objects::nonNull)
            .collect(Collectors.joining());
        return result;
    }

    public static String nullToString(String value) {
        return value == null ? "" : value;
    }

}
