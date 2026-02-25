package logic;

// 143
public class SReorderList {

    public void reorderList(ListNode head) {
        reverse(head, false);
    }

    private ListNode reverse(ListNode head, boolean isReverse) {
        ListNode dummy = head;
        ListNode newLastNode = head;
        if (head.next == null) {
            return head;
        }
        if (!isReverse) {
            dummy.next = reverse(head.next, !isReverse);
        } else {
            while (dummy.next != null) {
                if (dummy.next.next == null) {
                    newLastNode = dummy;
                }
                dummy = dummy.next;
            }
            newLastNode.next = null;
            dummy.next = head;
            dummy.next = reverse(dummy.next, !isReverse);
        }
        return dummy;
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
