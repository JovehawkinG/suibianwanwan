package logic;

// 19
public class S_19_SRemoveNthFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(-1, head);
        ListNode slow = dummy;
        ListNode fast = head;
        ListNode last = head;
        int num = n;
        while (num > 0) {
            last = last.next;
            num--;
        }
        while (last != null) {
            slow = slow.next;
            fast = fast.next;
            last = last.next;
        }
        slow.next = fast.next;
        return dummy.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
