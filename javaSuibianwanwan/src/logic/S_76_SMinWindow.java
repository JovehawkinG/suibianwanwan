package logic;

// 76
public class S_76_SMinWindow {
    public String minWindow(String s, String t) {
        int[] t_aArr = new int[26];
        int[] t_AArr = new int[26];
        int[] s_aArr = new int[26];
        int[] s_AArr = new int[26];
        int leftIndex = 0;
        String result = "";
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c >= 'a' && c <= 'z') {
                t_aArr[c - 'a']++;
            } else {
                t_AArr[c - 'A']++;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z') {
                s_aArr[c - 'a']++;
            } else {
                s_AArr[c - 'A']++;
            }
            while (isContains(s_aArr, t_aArr) && isContains(s_AArr, t_AArr) && leftIndex <= i) {
                if (result.length() == 0 || i - leftIndex + 1 < result.length()) {
                    result = s.substring(leftIndex, i + 1);
                }
                char cc = s.charAt(leftIndex);
                if (cc >= 'a' && cc <= 'z') {
                    s_aArr[cc - 'a']--;
                } else {
                    s_AArr[cc - 'A']--;
                }
                leftIndex++;
            }
        }
        return result;
    }

    private boolean isContains(int[] sArr, int[] tArr) {
        for (int i = 0; i < 26; i++) {
            if (tArr[i] == 0) {
                continue;
            } else if (sArr[i] < tArr[i]) {
                return false;
            }
        }
        return true;
    }
}
