package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// 32
public class S_32_SLongestValidParentheses {
    public int longestValidParentheses(String s) {
        int maxLen = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }
}
