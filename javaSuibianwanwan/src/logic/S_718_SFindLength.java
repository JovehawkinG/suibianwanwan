package logic;

// 718
public class S_718_SFindLength {
    public int findLength(int[] nums1, int[] nums2) {
        int result = 0;
        int[][] dp = new int[nums1.length][nums2.length];
        for (int i = 0; i < dp.length; i++) {
            if (nums1[i] == nums2[0]) {
                dp[i][0] = 1;
                result = 1;
            }
        }
        for (int i = 0; i < dp[0].length; i++) {
            if (nums1[0] == nums2[i]) {
                dp[0][i] = 1;
                result = 1;
            }
        }
        for (int i = 1; i < nums1.length; i++) {
            for (int j = 1; j < nums2.length; j++) {
                if (nums1[i] == nums2[j]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                result = Math.max(result, dp[i][j]);
            }
        }
        return result;
    }
}
