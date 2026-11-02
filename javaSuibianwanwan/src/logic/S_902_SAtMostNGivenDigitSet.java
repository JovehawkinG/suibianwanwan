package logic;

// 902
public class S_902_SAtMostNGivenDigitSet {
    public int atMostNGivenDigitSet(String[] digits, int n) {
        String s = String.valueOf(n);
        int len = s.length();
        int k = digits.length;
        long count = 0;
        for (int i = 1; i < len; i++) {
            count += power(k, i);
        }
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int less = 0;
            boolean matched = false;
            for (String d : digits) {
                char ch = d.charAt(0);
                if (ch < c) {
                    less++;
                } else if (ch == c) {
                    matched = true;
                }
            }
            count += (long) less * power(k, len - 1 - i);
            if (!matched) {
                return (int) count;
            }
        }
        return (int) (count + 1);
    }

    private long power(int base, int exp) {
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        return res;
    }
}
