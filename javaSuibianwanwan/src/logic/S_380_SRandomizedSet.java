package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// 380
public class S_380_SRandomizedSet {
    private final List<Integer> nums;
    private final Map<Integer, Integer> index;
    private final Random random;

    public S_380_SRandomizedSet() {
        nums = new ArrayList<>();
        index = new HashMap<>();
        random = new Random();
    }

    public boolean insert(int val) {
        if (index.containsKey(val)) {
            return false;
        }
        index.put(val, nums.size());
        nums.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer pos = index.get(val);
        if (pos == null) {
            return false;
        }
        int last = nums.get(nums.size() - 1);
        nums.set(pos, last);
        index.put(last, pos);
        nums.remove(nums.size() - 1);
        index.remove(val);
        return true;
    }

    public int getRandom() {
        return nums.get(random.nextInt(nums.size()));
    }
}
