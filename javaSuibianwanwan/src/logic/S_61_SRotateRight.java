package logic;

// 61
public class S_61_SRotateRight {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        int count = 0;
        ListNode dummy = head;
        while (dummy != null) {
            count++;
            dummy = dummy.next;
        }
        int realK = k % count;
        if (realK == 0) {
            return head;
        }
        int steps = count - realK;
        ListNode slow = head;
        for (int i = 1; i < steps; i++) {
            slow = slow.next;
        }
        ListNode newHead = slow.next;
        slow.next = null;
        dummy = newHead;
        while (dummy.next != null) {
            dummy = dummy.next;
        }
        dummy.next = head;
        return newHead;
    }
}
