package logic;

// 44
public class S_44_SIsMatch {
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        int i = 0;
        int j = 0;
        int star = -1;
        int match = 0;

        while (i < m) {
            if (j < n && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                i++;
                j++;
            } else if (j < n && p.charAt(j) == '*') {
                star = j;
                match = i;
                j++;
            } else if (star != -1) {
                match++;
                i = match;
                j = star + 1;
            } else {
                return false;
            }
        }

        while (j < n && p.charAt(j) == '*') {
            j++;
        }
        return j == n;
    }
}
