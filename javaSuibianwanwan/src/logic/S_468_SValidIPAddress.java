package logic;

// 468
public class S_468_SValidIPAddress {
    public String validIPAddress(String queryIP) {
        if (queryIP.indexOf('.') >= 0 && isIPv4(queryIP)) {
            return "IPv4";
        }
        if (queryIP.indexOf(':') >= 0 && isIPv6(queryIP)) {
            return "IPv6";
        }
        return "Neither";
    }

    private boolean isIPv4(String ip) {
        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            if (part.length() == 0 || part.length() > 3) {
                return false;
            }
            if (part.length() > 1 && part.charAt(0) == '0') {
                return false;
            }
            int value = 0;
            for (int i = 0; i < part.length(); i++) {
                char c = part.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
                value = value * 10 + (c - '0');
            }
            if (value > 255) {
                return false;
            }
        }
        return true;
    }

    private boolean isIPv6(String ip) {
        String[] parts = ip.split(":", -1);
        if (parts.length != 8) {
            return false;
        }
        for (String part : parts) {
            if (part.length() == 0 || part.length() > 4) {
                return false;
            }
            for (int i = 0; i < part.length(); i++) {
                char c = part.charAt(i);
                boolean digit = c >= '0' && c <= '9';
                boolean lower = c >= 'a' && c <= 'f';
                boolean upper = c >= 'A' && c <= 'F';
                if (!digit && !lower && !upper) {
                    return false;
                }
            }
        }
        return true;
    }
}
