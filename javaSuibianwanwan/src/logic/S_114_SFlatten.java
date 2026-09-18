package logic;

// 114
public class S_114_SFlatten {
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode left = root.left;
        TreeNode right = root.right;
        root.right = left;
        root.left = null;
        flatten(left);
        TreeNode dummy = root;
        while (dummy.right != null) {
            dummy = dummy.right;
        }
        dummy.right = right;
        flatten(right);
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
