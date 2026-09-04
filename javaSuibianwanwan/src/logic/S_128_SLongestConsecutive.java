package logic;

import java.util.HashSet;
import java.util.Set;

// 128
public class S_128_SLongestConsecutive {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int result = 0;
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }
            int length = 1;
            int cur = num;
            while (set.contains(cur + 1)) {
                cur++;
                length++;
            }
            result = Math.max(result, length);
        }
        return result;
    }
}
