package logic;

// 59
public class S_59_SGenerateMatrix {
    public int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int top = 0;
        int bottom = n - 1;
        int left = 0;
        int right = n - 1;
        int val = 1;
        while (val <= n * n) {
            for (int c = left; c <= right; c++) {
                matrix[top][c] = val++;
            }
            top++;
            for (int r = top; r <= bottom; r++) {
                matrix[r][right] = val++;
            }
            right--;
            for (int c = right; c >= left; c--) {
                matrix[bottom][c] = val++;
            }
            bottom--;
            for (int r = bottom; r >= top; r--) {
                matrix[r][left] = val++;
            }
            left++;
        }
        return matrix;
    }
}
