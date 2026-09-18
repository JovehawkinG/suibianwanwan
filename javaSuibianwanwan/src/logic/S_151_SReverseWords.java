package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// 151
public class S_151_SReverseWords {
    public String reverseWords(String s) {
        Deque<String> stack = new ArrayDeque<>();
        int n = s.length();
        int i = 0;
        while (i < n) {
            while (i < n && s.charAt(i) == ' ') {
                i++;
            }
            if (i >= n) {
                break;
            }
            int j = i;
            while (j < n && s.charAt(j) != ' ') {
                j++;
            }
            stack.push(s.substring(i, j));
            i = j;
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
            if (!stack.isEmpty()) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}
