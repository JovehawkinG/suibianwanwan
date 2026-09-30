package logic;

// 34
public class S_34_SSearchRange {
    public int[] searchRange(int[] nums, int target) {
        int left = findFirst(nums, target);
        if (left == -1) {
            return new int[]{-1, -1};
        }
        return new int[]{left, findLast(nums, target)};
    }

    private int findFirst(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] >= target) {
                if (nums[mid] == target) {
                    ans = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    private int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] <= target) {
                if (nums[mid] == target) {
                    ans = mid;
                }
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }
}
