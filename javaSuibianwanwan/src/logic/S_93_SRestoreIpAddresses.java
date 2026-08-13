package logic;

import java.util.ArrayList;
import java.util.List;

// 93
public class S_93_SRestoreIpAddresses {
    List<String> result = new ArrayList<>();
    public List<String> restoreIpAddresses(String s) {
        if (s.isEmpty()) {
            return new ArrayList<>();
        }
        splitAddresses(s, 0, "");
        return result;
    }

    private void splitAddresses(String s, int count, String addString) {
        if (count == 4) {
            if (s.isEmpty()) {
                result.add(addString);
            }
            return;
        }
        String tempNum = "";
        for (int i = 0; i < 3 && i < s.length(); i++) {
            tempNum = tempNum + s.charAt(i);
            if (isValid(tempNum)) {
                String newAddString = addString == "" ? tempNum : addString + "." + tempNum;
                splitAddresses(s.substring(i + 1), count + 1, newAddString);
            } else {
                break;
            }
        }
    }

    private boolean isValid(String tempNum) {
        int intNum = Integer.parseInt(tempNum);
        String stringNum = intNum + "";
        if (intNum >= 0 && intNum <= 255 && tempNum.length() == stringNum.length()) {
            return true;
        }
        return false;
    }
}
