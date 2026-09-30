package logic;

// 40
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class S_40_SCombinationSum2 {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remain, int start,
                           List<Integer> path, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) {
                break;
            }
            // 同一层跳过相同元素，避免重复组合
            if (i > start && candidates[i] == candidates[i - 1]) {
                continue;
            }
            path.add(candidates[i]);
            backtrack(candidates, remain - candidates[i], i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
}
