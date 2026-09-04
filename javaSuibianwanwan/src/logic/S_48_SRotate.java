package logic;

// 48
public class S_48_SRotate {
    public void rotate(int[][] matrix) {
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length / 2; i++) {
                int y = matrix.length - 1 - i;
                sawpM(matrix, i, j, y, j);
            }
        }
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j =0; j < i; j++) {
                sawpM(matrix, i, j, j, i);
            }
        }
    }

    void sawpM(int[][] matrix, int x1, int x2, int y1, int y2) {
        int temp = matrix[x1][x2];
        matrix[x1][x2] = matrix[y1][y2];
        matrix[y1][y2] = temp;
    }
}
