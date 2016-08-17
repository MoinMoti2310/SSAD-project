package ourfood.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.util.DigestUtils;

public final class Digest {
    private static Digest object = new Digest();
    private Properties versionInfo = new Properties();

    private Digest() {
        /*
         * try { InputStream inStream = Digest.class.getResourceAsStream("version.properties");
         * versionInfo.load(inStream); } catch (IOException e) { e.printStackTrace(); // Ignore }
         */
    }

    private boolean isValid0(String version, String appId, String hash) {
        // String hashString = appId + version + versionInfo.getProperty(version, "");
        String hashString = appId + version + "a3df01c2-2306-40a5-8c5d-00a41bc0ba21";
        return hash.equals(DigestUtils.md5DigestAsHex((hashString).getBytes()));
    }

    public static boolean isValid(String version, String appId, String hash) {
        return object.isValid0(version, appId, hash);
    }
}
