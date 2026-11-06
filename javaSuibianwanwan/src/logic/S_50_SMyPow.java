package logic;

// 50
public class S_50_SMyPow {
    public double myPow(double x, int n) {
        return pow(x, n);
    }

    private double pow(double x, long n) {
        if (n == 0) {
            return 1.0;
        }
        if (n < 0) {
            return 1.0 / pow(x, -n);
        }
        if ((n & 1) == 1) {
            return x * pow(x, n - 1);
        }
        return pow(x * x, n >> 1);
    }
}
