package com.wdbyte.array;

/**
 * @author https://www.wdbyte.com
 * @date 2023/03/25
 */
public class JavaArray3 {

    public static void main(String[] args) {
        int[][] myArray1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] myArray2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] myArray3 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][][] array3d = {myArray1, myArray2, myArray3};
        for (int i = 0; i < array3d.length; i++) {
            for (int j = 0; j < array3d[i].length; j++) {
                for (int k = 0; k < array3d[i][j].length; k++) {
                    System.out.print(array3d[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println("-----");
        }
        System.out.println(array3d[1][1][1]);
    }
}
