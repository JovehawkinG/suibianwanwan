package logic;

import java.util.ArrayList;
import java.util.List;

// 94
public class S_94_SInorderTraversal {
    List<Integer> list = new ArrayList<>();
    public List<Integer> inorderTraversal(TreeNode root) {
        printVal(root);
        return list;
    }

    private void printVal(TreeNode root) {
        if (root == null) {
            return;
        }
        printVal(root.left);
        list.add(root.val);
        printVal(root.right);
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
