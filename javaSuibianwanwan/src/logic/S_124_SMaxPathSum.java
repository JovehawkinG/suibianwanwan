package logic;

import java.util.HashMap;
import java.util.Map;

// 124
public class S_124_SMaxPathSum {

    Map<TreeNode, Integer> map = new HashMap<>();
    public int maxPathSum(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int leftNum = Math.max(calcSingleNum(root.left), 0);
            int rightNum = Math.max(calcSingleNum(root.right), 0);
            int maxSingleNum = leftNum + root.val + rightNum;
            if (root.left != null) {
                maxSingleNum = Math.max(maxSingleNum, maxPathSum(root.left));
            }
            if (root.right != null) {
                maxSingleNum = Math.max(maxSingleNum, maxPathSum(root.right));
            }
            return maxSingleNum;
    }

    private int calcSingleNum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (map.containsKey(root)) {
            return map.get(root);
        }
        int maxNum =  Math.max(calcSingleNum(root.left), calcSingleNum(root.right));
        maxNum = Math.max(maxNum, 0);
        map.put(root, maxNum + root.val);
        return map.get(root);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
