package logic;

import java.util.LinkedList;
import java.util.Queue;

// 297
public class S_297_SCodec {
    public String serialize(TreeNode root) {
        if (root == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append("null,");
                continue;
            }
            sb.append(node.val).append(",");
            queue.offer(node.left);
            queue.offer(node.right);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if ("null".equals(data)) {
            return null;
        }
        String[] arr = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(arr[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (!"null".equals(arr[i])) {
                node.left = new TreeNode(Integer.parseInt(arr[i]));
                queue.offer(node.left);
            }
            i++;
            if (!"null".equals(arr[i])) {
                node.right = new TreeNode(Integer.parseInt(arr[i]));
                queue.offer(node.right);
            }
            i++;
        }
        return root;
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
