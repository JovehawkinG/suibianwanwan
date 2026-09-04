package logic;

// 402
public class S_402_SRemoveKdigits {
    public String removeKdigits(String num, int k) {
        StringBuilder stack = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            while (k > 0 && stack.length() > 0 && stack.charAt(stack.length() - 1) > c) {
                stack.deleteCharAt(stack.length() - 1);
                k--;
            }
            stack.append(c);
        }
        while (k > 0) {
            stack.deleteCharAt(stack.length() - 1);
            k--;
        }
        int i = 0;
        while (i < stack.length() && stack.charAt(i) == '0') {
            i++;
        }
        String result = stack.substring(i);
        return result.isEmpty() ? "0" : result;
    }
}
