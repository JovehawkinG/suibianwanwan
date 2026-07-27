package logic;

// 23
class SMergeKLists {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(10);
        ListNode l2 = new ListNode(8, l1);
        ListNode l3 = new ListNode(5, l2);

        ListNode s1 = new ListNode(10);
        ListNode s2 = new ListNode(8, s1);
        ListNode s3 = new ListNode(5, s2);

        ListNode[] lists = new ListNode[]{l3, s3};
        SMergeKLists sMergeKLists = new SMergeKLists();
        System.out.println(sMergeKLists.mergeKLists(lists).toString());
    }

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        for (int i = 1; i < lists.length; i++) {
            lists[0] = function(lists[0], lists[i]);
        }
        return lists[0];
    }

    ListNode function(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        ListNode temp;
        if (l1.val <= l2.val) {
            temp = l1;
            temp.next = function(l1.next, l2);
        } else {
            temp = l2;
            temp.next = function(l1, l2.next);
        }
        return temp;
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

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            while(next != null) {
                s.append(next.val).append("-");
                next = next.next;
            }
            return s.toString();
        }
    }
}
