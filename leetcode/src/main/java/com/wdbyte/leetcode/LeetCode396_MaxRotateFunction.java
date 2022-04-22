package com.wdbyte.leetcode;

import java.util.Arrays;

/**
 * https://leetcode-cn.com/problems/rotate-function/
 * 给定一个长度为 n 的整数数组nums。
 *
 * 假设arrk是数组nums顺时针旋转 k 个位置后的数组，我们定义nums的 旋转函数F为：
 *
 * F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1]
 * 返回F(0), F(1), ..., F(n-1)中的最大值。
 *
 * 生成的测试用例让答案符合32 位 整数。
 * 例子:
 *      输入: nums = [4,3,2,6]
 *      输出: 26
 *      解释:
 *      F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
 *      F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
 *      F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
 *      F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
 *      所以 F(0), F(1), F(2), F(3) 中的最大值是 F(3) = 26 。
 *
 * @author niulang
 * @date 2022/04/22
 */
public class LeetCode396_MaxRotateFunction {

    public static void main(String[] args) {
        int[] nums = new int[] {4, 3, 2, 6};
        //int[] nums = new int[] {100};
        System.out.println(new LeetCode396_MaxRotateFunction().maxRotateFunction(nums));
    }

    /**
     * 寻找规律
     *
     * @param nums
     * @return
     */
    public int maxRotateFunction(int[] nums) {
        int sum = 0, numSum = Arrays.stream(nums).sum();
        for (int i = 0; i < nums.length; i++) {
            sum += i * nums[i];
        }
        int max = sum;
        for (int i = 1; i < nums.length; i++) {
            // 规律
            sum = sum + numSum - nums.length * nums[nums.length - i];
            max = Math.max(max, sum);
        }
        return max;
    }
}
