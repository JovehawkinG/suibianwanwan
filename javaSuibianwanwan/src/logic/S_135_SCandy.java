package logic;

// 135
public class S_135_SCandy {
    public int candy(int[] ratings) {
        int[] a = new int[ratings.length];
        int[] b = new int[ratings.length];
        int result = 0;
        a[0] = 1;
        b[b.length - 1] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                a[i] = a[i - 1] + 1;
            } else {
                a[i] = 1;
            }
        }
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                b[i] = b[i + 1] + 1;
            } else {
                b[i] = 1;
            }
        }
        for (int i = 0; i < ratings.length; i++) {
            result += Math.max(a[i], b[i]);
        }
        return result;
    }
}
