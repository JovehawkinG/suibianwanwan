package logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// 20
public class SIsValid {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> characterMap = new HashMap<>();
        characterMap.put(')', '(');
        characterMap.put('}', '{');
        characterMap.put(']', '[');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (characterMap.containsKey(c)) {
                if (stack.isEmpty()) {
                    return false;
                }
                Character pop = stack.pop();
                if (!characterMap.get(c).equals(pop)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }
}
