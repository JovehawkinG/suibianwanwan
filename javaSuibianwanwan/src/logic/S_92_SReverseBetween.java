package logic;

// 92
public class S_92_SReverseBetween {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode leftAhead = dummy;
        ListNode leftNode = head;
        ListNode rightNode = head;
        ListNode rightNext = null;
        int count = 1;
        while (count < right) {
            if (count < left) {
                leftAhead = leftNode;
                leftNode = leftNode.next;
            }
            rightNode = rightNode.next;
            count++;
            if (count == right) {
                rightNext = rightNode.next;
            }
        }
        leftAhead.next = limitReverse(leftNode, rightNode, rightNext);
        return dummy.next;
    }

    private ListNode limitReverse(ListNode left, ListNode right, ListNode rightNext) {
        if (left == right) {
            return right;
        }
        ListNode newNode = limitReverse(left.next, right, rightNext);
        left.next.next = left;
        left.next = rightNext;
        return newNode;
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
