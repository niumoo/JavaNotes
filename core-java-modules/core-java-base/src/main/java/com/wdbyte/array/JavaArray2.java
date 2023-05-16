package com.wdbyte.array;

/**
 * @author https://www.wdbyte.com
 * @date 2023/03/25
 */
public class JavaArray2 {

    public static void main(String[] args) {
        int[][] myArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        for (int i = 0; i < myArray.length; i++) {
            for (int j = 0; j < myArray[i].length; j++) {
                System.out.print(myArray[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(myArray[0][0]); // 输出1
        System.out.println(myArray[0][1]); // 输出2
        System.out.println(myArray[1][0]); // 输出4

        System.out.println("------------");

        int intArray[][] = { { 1, 2, 3 }, { 4, 5 } };
        int cloneArray[][] = intArray.clone();
        // 输出：false
        System.out.println(intArray == cloneArray);

        // 因为是浅拷贝，所以元素引用相同，输出 true
        System.out.println(intArray[0] == cloneArray[0]);
        System.out.println(intArray[1] == cloneArray[1]);
    }
}
