package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class S_56_SMergeIntervals {
    public int[][] merge(int[][] intervals) {
        ArrayList<int[]> results = new ArrayList<>();
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        results.add(new int[] {intervals[0][0], intervals[0][1]});
        for (int i = 0; i < intervals.length; i++) {
            int[] lastInts = results.get(results.size() - 1);
            if (intervals[i][0] <= lastInts[1]) {
                lastInts[1] = Math.max(lastInts[1], intervals[i][1]);
            } else {
                results.add(new int[] {intervals[i][0], intervals[i][1]});
            }
        }
        return results.toArray(new int[results.size()][]);
    }
}
