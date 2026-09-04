package logic;

// 70
public class S_70_SClimbStairs {
    public int climbStairs(int n) {
        int[] dp = new int[n + 1];
        if (n < 1) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        if (n < 3) {
            return 2;
        }
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
