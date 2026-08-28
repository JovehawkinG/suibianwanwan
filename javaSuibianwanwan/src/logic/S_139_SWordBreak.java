package logic;

import java.util.List;

// 139
public class S_139_SWordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < wordDict.size(); j++) {
                int len = wordDict.get(j).length();
                if (i < len) {
                    continue;
                }
                String subS = s.substring(i - len, i);
                if (this.isEqual(subS, wordDict.get(j)) && dp[i - len] == 1) {
                    dp[i] = 1;
                }
            }
        }
        return dp[dp.length - 1] == 1;
    }

    private boolean isEqual(String s1, String s2) {
        return s1.equals(s2);
    }
}
