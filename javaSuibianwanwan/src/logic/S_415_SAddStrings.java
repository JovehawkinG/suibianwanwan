package logic;

import java.util.Stack;

// 415
public class S_415_SAddStrings {
    public String addStrings(String num1, String num2) {
        String result = "";
        Stack<Character> c1 = new Stack<>();
        Stack<Character> c2 = new Stack<>();
        int addIndex = 0;
        for (int i = 0; i < num1.length(); i++) {
            char c = num1.charAt(i);
            c1.push(c);
        }
        for (int i = 0; i < num2.length(); i++) {
            char c = num2.charAt(i);
            c2.push(c);
        }
        while (!c1.isEmpty() || !c2.isEmpty()) {
            char cc1 = c1.isEmpty() ? '0' : c1.pop();
            char cc2 = c2.isEmpty() ? '0' : c2.pop();
            int i = Integer.parseInt(String.valueOf(cc1)) + Integer.parseInt(String.valueOf(cc2)) + addIndex;
            if (i > 9) {
                addIndex = 1;
                result = i % 10 + result;
            } else {
                addIndex = 0;
                result = i + result;
            }
        }
        if (addIndex == 1) {
            result = "1" + result;
        }
        return result;
    }
}
