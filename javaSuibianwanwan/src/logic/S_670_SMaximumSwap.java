package logic;

// 670
public class S_670_SMaximumSwap {
    public int maximumSwap(int num) {
        char[] s = String.valueOf(num).toCharArray();
        int n = s.length;

        // maxIdx[i] = s[i..n-1] 中最大值所在的最靠右下标
        int[] maxIdx = new int[n];
        maxIdx[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] > s[maxIdx[i + 1]]) {
                maxIdx[i] = i;
            } else {
                maxIdx[i] = maxIdx[i + 1];
            }
        }

        // 从左找第一个能被后面更大数字替换的位置，换一次即返回
        for (int i = 0; i < n; i++) {
            if (s[i] < s[maxIdx[i]]) {
                char tmp = s[i];
                s[i] = s[maxIdx[i]];
                s[maxIdx[i]] = tmp;
                return Integer.parseInt(new String(s));
            }
        }
        return num;
    }
}
