package logic;

// 209
public class S_209_SMinSubArrayLen {
    public int minSubArrayLen(int target, int[] nums) {
        int len = Integer.MAX_VALUE;
        int left = 0;
        int right = 0;
        int sum = 0;
        while (right < nums.length) {
            sum += nums[right];
            while (sum >= target) {
                len = Math.min(len, right - left + 1);
                sum -= nums[left];
                left++;
            }
            right++;
        }
        return len == Integer.MAX_VALUE ? 0 : len;
    }
}
