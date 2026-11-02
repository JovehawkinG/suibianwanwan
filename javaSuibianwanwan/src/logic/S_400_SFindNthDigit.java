package logic;

// 400
public class S_400_SFindNthDigit {
    public int findNthDigit(int n) {
        long d = 1;
        long start = 1;
        long count = 9;
        long remain = n;
        while (remain > d * count) {
            remain -= d * count;
            d++;
            start *= 10;
            count *= 10;
        }
        long num = start + (remain - 1) / d;
        int index = (int) ((remain - 1) % d);
        for (int i = 0; i < d - 1 - index; i++) {
            num /= 10;
        }
        return (int) (num % 10);
    }
}
