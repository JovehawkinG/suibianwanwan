package logic;

// 129
public class S_129_SSumNumbers {
    public int result = 0;
    public int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        calSumNum(root, "");
        return result;
    }

    private void calSumNum(TreeNode root, String path) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            result += Integer.parseInt(path + root.val);
            return;
        }
        path = path + root.val;
        calSumNum(root.left, path);
        calSumNum(root.right, path);
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
