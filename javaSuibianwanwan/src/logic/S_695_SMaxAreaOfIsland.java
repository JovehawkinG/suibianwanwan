package logic;

// 695
public class S_695_SMaxAreaOfIsland {
    public int maxAreaOfIsland(int[][] grid) {
        boolean[][] dp = new boolean[grid.length][grid[0].length];
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                result = Math.max(result, calcArea(grid, i, j, dp));
            }
        }
        return result;
    }
    int calcArea(int[][] grid, int x, int y, boolean[][] dp) {
        if (x < 0 || x > grid.length - 1) {
            return 0;
        }
        if (y < 0 || y > grid[0].length - 1) {
            return 0;
        }
        if (dp[x][y]) {
            return 0;
        }
        dp[x][y] = true;
        if (grid[x][y] == 0) {
            return 0;
        }
        return 1
                + calcArea(grid, x - 1, y, dp)
                + calcArea(grid, x, y - 1, dp)
                + calcArea(grid, x - 1, y - 1, dp)
                + calcArea(grid, x + 1, y, dp)
                + calcArea(grid, x, y + 1, dp)
                + calcArea(grid, x + 1, y + 1, dp);
    }
}
