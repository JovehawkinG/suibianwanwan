package logic;

// 198
public class S_198_SRob {
    public int rob(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[][] dp = new int[nums.length][2];
        dp[0][1] = nums[0];
        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1]);
            dp[i][1] = dp[i - 1][0] + nums[i];
            result = Math.max(result, dp[i][0]);
            result = Math.max(result, dp[i][1]);
        }
        return result;
    }
}
