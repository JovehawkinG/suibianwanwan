package logic;

// LCR 170
public class S_LCR170_SReversePairs {
    public int reversePairs(int[] record) {
        if (record == null || record.length < 2) {
            return 0;
        }
        return mergeSort(record, 0, record.length - 1, new int[record.length]);
    }

    private int mergeSort(int[] record, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }
        int mid = left + (right - left) / 2;
        int count = mergeSort(record, left, mid, temp) + mergeSort(record, mid + 1, right, temp);
        return count + merge(record, left, mid, right, temp);
    }

    private int merge(int[] record, int left, int mid, int right, int[] temp) {
        int i = left;
        int j = mid + 1;
        int k = left;
        int count = 0;
        while (i <= mid && j <= right) {
            if (record[i] <= record[j]) {
                temp[k++] = record[i++];
            } else {
                count += mid - i + 1;
                temp[k++] = record[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = record[i++];
        }
        while (j <= right) {
            temp[k++] = record[j++];
        }
        for (int p = left; p <= right; p++) {
            record[p] = temp[p];
        }
        return count;
    }
}
