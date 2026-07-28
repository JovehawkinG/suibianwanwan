package logic;

// 92
public class S_92_SReverseBetween {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        return head;
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
