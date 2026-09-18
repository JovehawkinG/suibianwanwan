package logic;

import java.util.HashMap;

// 110
public class S_110_SIsBalanced {

    private HashMap<TreeNode, Integer> map = new HashMap<>();

    public boolean isBalanced(TreeNode root) {
        return calcBalanced(root);
    }
    
    private boolean calcBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        int l = calcDeepth(root.left);
        int r = calcDeepth(root.right);
        if (l - r > 1 || r - l > 1) {
            return false;
        }
        return calcBalanced(root.left) && calcBalanced(root.right);
    }

    private Integer calcDeepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (map.containsKey(root)) {
            return map.get(root);
        }
        int num = Math.max(calcDeepth(root.left), calcDeepth(root.right));
        map.put(root, num + 1);
        return num + 1;
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
