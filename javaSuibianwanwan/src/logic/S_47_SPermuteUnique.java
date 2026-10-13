package logic;

// 47
import java.util.*;

public class S_47_SPermuteUnique {
    List<List<Integer>> result = new LinkedList<>();
    List<Integer> path = new LinkedList<>();
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        boolean[] index = new boolean[nums.length];
        dfs(nums, path, index, 0);
        return result;
    }

    private void dfs(int[] nums, List<Integer> path, boolean[] index, int count) {
        if (count == nums.length) {
            result.add(new LinkedList<>(path));
            return;
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (index[i]) {
                continue;
            }
            if (set.contains(nums[i])) {
                continue;
            }
            set.add(nums[i]);
            path.add(nums[i]);
            index[i] = true;
            dfs(nums, path, index, count + 1);
            path.remove(path.size() - 1);
            index[i] = false;
        }
    }
}
