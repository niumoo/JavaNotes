package com.wdbyte.leetcode;

/**
 * https://leetcode-cn.com/problems/erect-the-fence/
 * 587. 安装栅栏
 * 在一个二维的花园中，有一些用 (x, y) 坐标表示的树。由于安装费用十分昂贵，你的任务是先用最短的绳子围起
 * 所有的树。只有当所有的树都被绳子包围时，花园才能围好栅栏。你需要找到正好位于栅栏边界上的树的坐标。
 *
 * @author https://www.wdbyte.com
 * @date 2022/04/23
 */
public class LeetCode587_OuterTrees {
    public static void main(String[] args) {
        int[][] trees = {{1, 1}, {2, 2}, {2, 0}, {2, 4}, {3, 3}, {4, 2}};
        System.out.println(multi(trees[2], trees[2], trees[4]));
    }

    public int[][] outerTrees(int[][] trees) {
        // 1. 找到最左边的一个点
        int startX = 0;
        int startY = 0;
        for (int[] tree : trees) {
            int x = tree[0];
            int y = tree[1];
            if (x < startX) {
                startX = x;
                startY = y;
            }
        }
        // 2. 从最左边的一个点开始，寻找最大角度的点的连线
        return null;
    }

    static int[][] sort(int[][] trees) {
        return trees;
    }

    //计算叉积，p1，p2，p0都为点
    static double multi(int[] p1, int[] p2, int[] p0) {
        double x1, y1, x2, y2;
        x1 = p1[0] - p0[0];
        y1 = p1[1] - p0[1];
        x2 = p2[0] - p0[0];
        y2 = p2[1] - p0[1];
        return x1 * y2 - x2 * y1;
    }

}
