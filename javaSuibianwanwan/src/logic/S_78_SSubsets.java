package logic;

import java.util.ArrayList;
import java.util.List;

// 78
public class S_78_SSubsets {
    List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> subsets(int[] nums) {
        if (nums.length == 0) {
            return result;
        }
        result.add(new ArrayList<>());
        calSubSets(nums, 0, new ArrayList<>());
        return result;
    }

    private void calSubSets(int[] nums, int index, List<Integer> list) {
        for (int i = index; i < nums.length; i++) {
            list.add(nums[i]);
            result.add(new ArrayList<>(list));
            calSubSets(nums, i + 1, list);
            list.remove(list.size() - 1);
        }
    }
}
