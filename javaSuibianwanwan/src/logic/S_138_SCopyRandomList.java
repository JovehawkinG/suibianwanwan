package logic;

import java.util.HashMap;
import java.util.Map;

// 138
public class S_138_SCopyRandomList {

    public static class Node {
        int val;
        Node next;
        Node random;
        Node(int val) {
            this.val = val;
        }
    }

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        Map<Node, Node> map = new HashMap<>();
        Node dumm = head;
        while (dumm != null) {
            map.put(dumm, new Node(dumm.val));
            dumm = dumm.next;
        }
        dumm = head;
        while (dumm != null) {
            map.get(dumm).next = map.get(dumm.next);
            map.get(dumm).random = map.get(dumm.random);
            dumm = dumm.next;
        }
        return map.get(head);
    }
}
