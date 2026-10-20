package logic;

// 153
public class S_153_SFindMin {
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[right]) {
                // 断点在 mid 右侧，mid 本身不可能是最小值
                left = mid + 1;
            } else {
                // mid 到 right 有序，最小值在 mid 或其左侧
                right = mid;
            }
        }
        return nums[left];
    }
}
