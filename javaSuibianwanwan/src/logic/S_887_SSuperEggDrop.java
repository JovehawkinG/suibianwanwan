package logic;

// 887
public class S_887_SSuperEggDrop {
    public int superEggDrop(int k, int n) {
        int[] dp = new int[k + 1];
        int moves = 0;
        while (dp[k] < n) {
            moves++;
            for (int eggs = k; eggs >= 1; eggs--) {
                dp[eggs] = dp[eggs] + dp[eggs - 1] + 1;
            }
        }
        return moves;
    }
}
