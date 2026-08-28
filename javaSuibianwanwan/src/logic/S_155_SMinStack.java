package logic;

import java.util.Stack;

// 155
public class S_155_SMinStack {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> min = new Stack<Integer>();
    public S_155_SMinStack() {
    }

    public void push(int val) {
        stack1.push(val);
        if (min.isEmpty() || val <= min.peek()) {
            min.push(val);
        }
    }

    public void pop() {
        if (stack1.isEmpty()) {
            return;
        }
        int val = stack1.pop();
        if (val == min.peek()) {
            min.pop();
        }
    }

    public int top() {
        return stack1.peek();
    }

    public int getMin() {
        return min.peek();
    }
}
