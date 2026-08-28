package logic;

import java.util.ArrayList;
import java.util.List;

// 113
public class S_113_SPathSum {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        calcSum(root, targetSum);
        return result;
    }

    private void calcSum(TreeNode root, int targetSum) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            if (targetSum == root.val) {
                path.add(root.val);
                result.add(new ArrayList<>(path));
                path.remove(path.size() - 1);
            }
            return;
        }
        path.add(root.val);
        if (root.left != null) {
            calcSum(root.left, targetSum - root.val);
        }
        if (root.right != null) {
            calcSum(root.right, targetSum - root.val);
        }
        path.remove(path.size() - 1);
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
