package com.wdbyte.leetcode;

import java.util.Stack;

/**
 * 388. 文件的最长绝对路径
 * https://leetcode-cn.com/problems/longest-absolute-file-path/
 *
 * @author niulang
 * @date 2022/04/20
 */
public class LeetCode388_LengthLongestPath {
    public static void main(String[] args) {
        LeetCode388_LengthLongestPath leetCode388 = new LeetCode388_LengthLongestPath();
        //int path = leetCode388.lengthLongestPath2("dir\n        file.txt");
        int path = leetCode388.lengthLongestPath2("dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext");
        System.out.println(path);
    }

    public int lengthLongestPath(String input) {
        String[] array = input.split("\n");
        Stack<String> stack = new Stack();
        int lastTCount = 0;
        int maxSize = 0;
        for (String path : array) {
            int tCount = 0;
            while (path.contains("\t")) {
                tCount++;
                path = path.substring(path.indexOf("\t") + 1);
            }
            if (tCount > lastTCount) {
                stack.push(path);
            } else {
                for (int i = 0; i <= (lastTCount - tCount); i++) {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                }
                stack.push(path);
            }
            lastTCount = tCount;
            if (path.contains(".")) {
                int size = 0;
                for (String s : stack) {
                    size += s.length();
                }
                size = size + stack.size() - 1;
                if (size > maxSize) {
                    maxSize = size;
                }
            }
        }
        return maxSize;
    }

    public int lengthLongestPath2(String input) {
        String[] array = input.split("\n");
        Stack<Integer> stack = new Stack();
        int lastTCount = 0;
        int maxSize = 0;
        for (String path : array) {
            int tCount = 0;
            while (path.contains("\t")) {
                tCount++;
                path = path.substring(path.indexOf("\t") + 1);
            }
            if (tCount > lastTCount) {
                stack.push(path.length());
            } else {
                for (int i = 0; i <= (lastTCount - tCount); i++) {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                }
                stack.push(path.length());
            }
            lastTCount = tCount;
            if (path.contains(".")) {
                int size = 0;
                for (Integer s : stack) {
                    size += s;
                }
                size = size + stack.size() - 1;
                if (size > maxSize) {
                    maxSize = size;
                }
            }
        }
        return maxSize;
    }

}
