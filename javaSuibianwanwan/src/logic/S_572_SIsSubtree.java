package logic;

// 572
public class S_572_SIsSubtree {
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (subRoot == null) {
            return true;
        }
        if (root == null) {
            return false;
        }
        if (isCalcSubTree(root, subRoot)) {
            return true;
        }
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean isCalcSubTree(TreeNode root, TreeNode subRoot) {
        if (root == null || subRoot == null) {
            return root == subRoot;
        }
        if (root.val != subRoot.val) {
            return false;
        }
        return isCalcSubTree(root.left, subRoot.left) && isCalcSubTree(root.right, subRoot.right);
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
