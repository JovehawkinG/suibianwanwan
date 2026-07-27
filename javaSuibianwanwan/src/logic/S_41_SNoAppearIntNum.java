package logic;

// 41
public class S_41_SNoAppearIntNum {
    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int index = nums[i] - 1;
            if (index < 0 || index >= nums.length) {
                nums[i] = -1;
                continue;
            } else if (nums[index] == index + 1) {
                continue;
            }
            int temp = nums[index];
            nums[index] = nums[i];
            nums[i] = temp;
            i--;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return nums.length + 1;
    }
}
