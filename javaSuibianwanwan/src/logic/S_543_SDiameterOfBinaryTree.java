package logic;

// 543
public class S_543_SDiameterOfBinaryTree {
    int result = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        addNum(root);
        return result;
    }

    private void addNum(TreeNode root) {
        int num = 0;
        if (root == null) {
            return;
        }
        if (root.left != null) {
            num = 1 + calcNum(root.left) + num;
        }
        if (root.right != null) {
            num = 1 + calcNum(root.right) + num;
        }
        result = Math.max(result, num);
        addNum(root.left);
        addNum(root.right);
    }

    private int calcNum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 0;
        }
        int left = 0;
        int right = 0;
        if (root.left != null) {
            left = 1 + calcNum(root.left);
        }
        if (root.right != null) {
            right = 1 + calcNum(root.right);
        }
        return Math.max(left, right);
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
