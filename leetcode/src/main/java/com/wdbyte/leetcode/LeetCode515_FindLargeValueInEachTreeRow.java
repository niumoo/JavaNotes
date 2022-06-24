package com.wdbyte.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
 *
 * 515. 在每个树行中找最大值
 *
 * @author niulang
 * @date 2022/06/24
 */
public class LeetCode515_FindLargeValueInEachTreeRow {

    public static void main(String[] args) {
        LeetCode515_FindLargeValueInEachTreeRow code515 = new LeetCode515_FindLargeValueInEachTreeRow();
        TreeNode tree3_5 = new TreeNode(5);
        TreeNode tree3_3 = new TreeNode(3);
        TreeNode tree3_9 = new TreeNode(9);
        TreeNode tree2_3 = new TreeNode(3, tree3_5, tree3_3);
        TreeNode tree2_2 = new TreeNode(2, null, tree3_9);
        TreeNode tree1_1 = new TreeNode(1, tree2_3, tree2_2);
        List<Integer> integers = code515.largestValues(tree1_1);
        System.out.println(integers);
    }

    public List<Integer> largestValues(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        List<TreeNode> nodeList = new ArrayList<>();
        nodeList.add(root);
        while (!nodeList.isEmpty()) {
            List<TreeNode> nodeListTemp = new ArrayList<>();
            Integer max = null;
            for (TreeNode treeNode : nodeList) {
                if (treeNode == null) {
                    continue;
                }
                if (max == null) {
                    max = treeNode.val;
                }
                if (max < treeNode.val) {
                    max = treeNode.val;
                }
                if (treeNode.left != null) {
                    nodeListTemp.add(treeNode.left);
                }
                if (treeNode.right != null) {
                    nodeListTemp.add(treeNode.right);
                }
            }
            result.add(max);
            nodeList = nodeListTemp;
        }
        return result;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {}

    TreeNode(int val) {this.val = val;}

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}