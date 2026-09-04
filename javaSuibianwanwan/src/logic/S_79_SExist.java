package logic;

// 79
public class S_79_SExist {
    private final int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public boolean exist(char[][] board, String word) {
        boolean[][] index = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isExist(board, index, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExist(char[][] board, boolean[][] index, String word, int indexWord, int i, int j) {
        Character c = word.charAt(indexWord);
        if (i < 0 || i >= board.length) {
            return false;
        }
        if (j < 0 || j >= board[0].length) {
            return false;
        }
        if (index[i][j]) {
            return false;
        }
        if (board[i][j] != c) {
            return false;
        }
        if (indexWord == word.length() - 1) {
            return true;
        }
        index[i][j] = true;
        for (int s = 0; s < direction.length; s++) {
            int[] d = direction[s];
            if (isExist(board, index, word, indexWord + 1, i + d[0], j + d[1])) {
                return true;
            }
        }
        index[i][j] = false;
        return false;
    }
}
