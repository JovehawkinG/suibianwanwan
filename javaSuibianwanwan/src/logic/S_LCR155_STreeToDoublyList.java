package logic;

// LCR 155
public class S_LCR155_STreeToDoublyList {
    private Node pre;
    private Node head;

    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        dfs(root);
        // 首尾相连，构成循环双向链表
        head.left = pre;
        pre.right = head;
        return head;
    }

    // 中序遍历：左 -> 根 -> 右，天然有序
    private void dfs(Node cur) {
        if (cur == null) {
            return;
        }
        dfs(cur.left);

        if (pre == null) {
            head = cur;          // 最左节点即链表头
        } else {
            pre.right = cur;     // 前驱的 right 指向当前节点
            cur.left = pre;      // 当前节点的 left 指向前驱
        }
        pre = cur;               // 当前节点成为下一轮的前驱

        dfs(cur.right);
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node() {}

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
