package logic;

// 2
public class S_2_SAddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        boolean needAdd = false;
        ListNode head = new ListNode();
        ListNode dummy = head;
        while (l1 != null || l2 != null) {
            int val1 = l1 == null ? 0 : l1.val;
            int val2 = l2 == null ? 0 : l2.val;
            int val = val1 + val2 + (needAdd ? 1 : 0);
            needAdd = val >= 10;
            val = val % 10;
            head.next = new ListNode(val);
            head = head.next;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (needAdd) {
            head.next = new ListNode(1);
        }
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
