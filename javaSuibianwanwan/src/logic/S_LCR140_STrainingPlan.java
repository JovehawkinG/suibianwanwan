package logic;

// LCR 140
public class S_LCR140_STrainingPlan {
    public ListNode trainingPlan(ListNode head, int cnt) {
        ListNode fast = head;
        int count = 0;
        while (count < cnt) {
            fast = fast.next;
            count++;
        }
        ListNode result = head;
        while (fast != null) {
            fast = fast.next;
            result = result.next;
        }
        return result;
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
