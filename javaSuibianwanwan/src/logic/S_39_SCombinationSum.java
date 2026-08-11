package logic;

// 39
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class S_39_SCombinationSum {
    private List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<Integer> list = new ArrayList<>();
        calcSum(candidates, target, 0, list);
        return result;
    }

    private void calcSum(int[] candidates, int target, int index, List<Integer> list) {
        if (target == 0) {
            result.add(new ArrayList<>(list));
        }
        if (target < 0 || target < candidates[index]) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            list.add(candidates[i]);
            calcSum(candidates, target - candidates[i], i, list);
            list.remove(list.size() - 1);
        }
    }
}
