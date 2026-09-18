package logic;

// 24
public class S_24_SSwapPairs {
    public ListNode swapPairs(ListNode head) {
        return doSwap(head);
    }

    private ListNode doSwap(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode hn = head.next;
        ListNode hnn = hn.next;
        hn.next = head;
        head.next = doSwap(hnn);
        return hn;
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
