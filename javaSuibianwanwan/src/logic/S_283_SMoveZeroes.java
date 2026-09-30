package logic;

// 283
public class S_283_SMoveZeroes {
    public void moveZeroes(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == 0 && nums[j] == 0) {
                    continue;
                }
                if (nums[i] == 0) {
                    swap(nums, i, j);
                    break;
                }
                if (nums[i] != 0) {
                    break;
                }
            }
        }
        return;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
