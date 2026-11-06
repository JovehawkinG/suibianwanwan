package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// 862
public class S_862_SShortestSubarray {
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        long[] pre = new long[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + nums[i];
        }

        int ans = n + 1;
        Deque<Integer> deque = new ArrayDeque<>();
        for (int j = 0; j <= n; j++) {
            while (!deque.isEmpty() && pre[j] - pre[deque.peekFirst()] >= k) {
                ans = Math.min(ans, j - deque.pollFirst());
            }
            while (!deque.isEmpty() && pre[deque.peekLast()] >= pre[j]) {
                deque.pollLast();
            }
            deque.offerLast(j);
        }
        return ans == n + 1 ? -1 : ans;
    }
}
