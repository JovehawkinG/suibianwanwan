package logic;

// 83
public class S_83_SDeleteDuplicates {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null) {
            if (fast.val == slow.val) {
                fast = fast.next;
                slow.next = fast;
            } else {
                fast = fast.next;
                slow = slow.next;
            }
        }
        return head;
    }
}
