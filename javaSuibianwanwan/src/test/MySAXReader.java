package test;

public class MySAXReader {

    public int maxLength(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int result = 1;
        int[] indexArr = new int[nums.length];
        indexArr[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (nums[j] < nums[i]) {
                    indexArr[i] = Math.max(indexArr[i], indexArr[j] + 1);
                    result = Math.max(result, indexArr[i]);
                }
            }
            if (indexArr[i] == 0) {
                indexArr[i] = 1;
            }
        }
        return result;
    }

}
