package ourfood.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

    public static Map<String, String> filter(Map<String, String> map, String filter) {

        Map<String, String> m = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.contains(filter)) {
                m.put(key.replace(filter, ""), value);
            }
        }

        return m;
    }

    public static String getRemoteIPAddress(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    public static String getDomainName(String url) throws URISyntaxException {

        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min
     *            Minimim value
     * @param max
     *            Maximim value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static String ellipsis(String string, int len) {

        if (string == null) {
            return null;
        }

        int strLen = string.length();

        if (len >= strLen) {
            return string;
        } else {
            return "..." + string.substring(strLen - len + 1);
        }
    }

    public static String ellipsisFirst(String string, int len) {

        if (string == null) {
            return null;
        }

        int strLen = string.length();

        if (len >= strLen) {
            return string;
        } else {
            return string.substring(0, len - 1) + "...";
        }
    }

    // to take mobile number as +91XXXXXXXXXX
    public static String getMobileFormat(String mobile) {

        if (mobile == null) {
            return null;
        }
        
        mobile = mobile.trim();

        if (mobile.length() == 10) {
            return "+91" + mobile;
        } else if (mobile.length() == 12) {
            return "+" + mobile;
        }else if(mobile.length() == 13){
            return mobile;
        }
        
        return mobile;
    }
}