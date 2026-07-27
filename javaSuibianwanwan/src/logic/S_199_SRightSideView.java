package logic;

import java.util.*;

// 199
public class SRightSideView {

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> list = new ArrayDeque<>();
        if (root == null) {
            return result;
        }
        list.add(root);
        while (!list.isEmpty()) {
            int size = list.size();
            int rightNum = list.peek().val;
            for (int i = 0; i < size; i++) {
                TreeNode node = list.poll();
                rightNum = node.val;
                if (node.left != null) {
                    list.add(node.left);
                }
                if (node.right != null) {
                    list.add(node.right);
                }
            }
            result.add(rightNum);
        }
        return result;
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

