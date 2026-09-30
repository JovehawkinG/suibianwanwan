package logic;

// 14
public class S_14_SLongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        String results = "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (strs[j].length() - 1 < i || strs[j].charAt(i) != c) {
                    return results;
                }
            }
            results = results + c;
        }
        return results;
    }
}
