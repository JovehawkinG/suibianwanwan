package logic;

// 230
public class S_230_SKthSmallest {
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

    public int count = 0;

    public int kthSmallest(TreeNode root, int k) {
        if (root == null) {
            return 0;
        }
        if (root.left != null) {
            int num = kthSmallest(root.left, k);
            if (count == k) {
                return num;
            }
        }
        count++;
        if (k == count) {
            return root.val;
        }
        if (root.right != null) {
            int num = kthSmallest(root.right, k);
            if (count == k) {
                return num;
            }
        }
        return 0;
    }
}
