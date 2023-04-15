package com.wdbyte.array;

/**
 * 锯齿数组
 * @author niulang
 * @date 2023/03/25
 */
public class JavaArray4 {

    public static void main(String[] args) {
        int[][] arrName1 = new int[][] {
            new int[] {10, 20, 30, 40},
            new int[] {50, 60, 70, 80, 90, 100},
            new int[] {110, 120}
        };

        int[][] arrName2 = {
            new int[] {10, 20, 30, 40},
            new int[] {50, 60, 70, 80, 90, 100},
            new int[] {110, 120}
        };

        int[][] arrName3 = {
            {10, 20, 30, 40},
            {50, 60, 70, 80, 90, 100},
            {110, 120}
        };

        int[][] jaggedArray = new int[3][];
        jaggedArray[0] = new int[] {1, 2, 3};
        jaggedArray[1] = new int[] {4, 5};
        jaggedArray[2] = new int[] {6, 7, 8, 9};

        for (int i = 0; i < jaggedArray.length; i++) {
            for (int j = 0; j < jaggedArray[i].length; j++) {
                System.out.print(jaggedArray[i][j] + " ");
            }
            System.out.println();
        }

    }
}
