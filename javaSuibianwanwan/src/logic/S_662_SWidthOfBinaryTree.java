package logic;

// 662
import java.util.ArrayDeque;
import java.util.Queue;

public class S_662_SWidthOfBinaryTree {
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

    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<Pair> queue = new ArrayDeque<>();
        queue.offer(new Pair(root, 1L));
        long maxWidth = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            long leftPos = queue.peek().pos;
            long rightPos = leftPos;
            for (int i = 0; i < size; i++) {
                Pair cur = queue.poll();
                rightPos = cur.pos;
                if (cur.node.left != null) {
                    queue.offer(new Pair(cur.node.left, cur.pos * 2));
                }
                if (cur.node.right != null) {
                    queue.offer(new Pair(cur.node.right, cur.pos * 2 + 1));
                }
            }
            maxWidth = Math.max(maxWidth, rightPos - leftPos + 1);
        }
        return (int) maxWidth;
    }

    private static class Pair {
        TreeNode node;
        long pos;
        Pair(TreeNode node, long pos) {
            this.node = node;
            this.pos = pos;
        }
    }
}
