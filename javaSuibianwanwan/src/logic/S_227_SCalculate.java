package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// 227
public class S_227_SCalculate {
    public int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int num = 0;
        char op = '+';
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c != ' ') {
                apply(stack, op, num);
                op = c;
                num = 0;
            }
        }
        apply(stack, op, num);
        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        return result;
    }

    private void apply(Deque<Integer> stack, char op, int num) {
        switch (op) {
            case '+':
                stack.push(num);
                break;
            case '-':
                stack.push(-num);
                break;
            case '*':
                stack.push(stack.pop() * num);
                break;
            default:
                stack.push(stack.pop() / num);
                break;
        }
    }
}
