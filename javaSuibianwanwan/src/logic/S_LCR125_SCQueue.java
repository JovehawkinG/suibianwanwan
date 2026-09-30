package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// LCR 125
public class S_LCR125_SCQueue {
    private final Deque<Integer> in = new ArrayDeque<>();
    private final Deque<Integer> out = new ArrayDeque<>();

    public S_LCR125_SCQueue() {
    }

    public void appendTail(int value) {
        in.push(value);
    }

    public int deleteHead() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }
        return out.isEmpty() ? -1 : out.pop();
    }
}
