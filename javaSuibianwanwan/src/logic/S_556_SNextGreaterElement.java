package logic;

// 556
public class S_556_SNextGreaterElement {
    public int nextGreaterElement(int n) {
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        int i = len - 2;
        while (i >= 0 && digits[i] >= digits[i + 1]) {
            i--;
        }
        if (i < 0) {
            return -1;
        }
        int j = len - 1;
        while (digits[j] <= digits[i]) {
            j--;
        }
        swap(digits, i, j);
        reverse(digits, i + 1, len - 1);
        long value = Long.parseLong(new String(digits));
        return value > Integer.MAX_VALUE ? -1 : (int) value;
    }

    private void swap(char[] digits, int i, int j) {
        char temp = digits[i];
        digits[i] = digits[j];
        digits[j] = temp;
    }

    private void reverse(char[] digits, int left, int right) {
        while (left < right) {
            swap(digits, left++, right--);
        }
    }
}
