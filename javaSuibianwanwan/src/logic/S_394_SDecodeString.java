package logic;

import java.util.Stack;

// 394
public class S_394_SDecodeString {
    public String decodeString(String s) {
        Stack<Character> stack = new Stack<>();
        int index = 0;
        while(index < s.length()) {
            Character c = s.charAt(index);
            if (c != ']') {
                stack.push(c);
            } else {
                String ss = "";
                String num = "";
                boolean isNum = false;
                while (!stack.isEmpty()) {
                    Character pop = stack.peek();
                    if (pop == '[' && !isNum) {
                        stack.pop();
                        isNum = true;
                        continue;
                    } else if (pop >= '0' && pop <= '9' && isNum) {
                        num = pop + num;
                        stack.pop();
                    } else if (pop >= 'a' && pop <= 'z' && !isNum) {
                        ss = pop + ss;
                        stack.pop();
                    } else {
                        break;
                    }
                }
                int number = num == "" ? 1 : Integer.parseInt(num);
                while (number > 0) {
                    for (char c1 : ss.toCharArray()) {
                        stack.push(c1);
                    }
                    number--;
                }
            }
            index++;
        }
        String result = "";
        while (!stack.isEmpty()) {
            result = stack.pop() + result;
        }
        return result;
    }
}
