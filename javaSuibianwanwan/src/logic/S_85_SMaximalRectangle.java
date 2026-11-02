package logic;

import java.util.ArrayDeque;
import java.util.Deque;

// 85
public class S_85_SMaximalRectangle {
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int cols = matrix[0].length;
        int[] heights = new int[cols];
        int max = 0;
        for (char[] row : matrix) {
            for (int c = 0; c < cols; c++) {
                heights[c] = row[c] == '1' ? heights[c] + 1 : 0;
            }
            max = Math.max(max, largestRectangle(heights));
        }
        return max;
    }

    private int largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        for (int i = 0; i <= heights.length; i++) {
            int cur = (i == heights.length) ? 0 : heights[i];
            while (!stack.isEmpty() && heights[stack.peek()] > cur) {
                int height = heights[stack.pop()];
                int left = stack.isEmpty() ? -1 : stack.peek();
                max = Math.max(max, height * (i - left - 1));
            }
            stack.push(i);
        }
        return max;
    }
}
