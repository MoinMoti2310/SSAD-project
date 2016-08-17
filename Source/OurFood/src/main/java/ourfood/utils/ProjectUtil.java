package ourfood.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class ProjectUtil {

    public static boolean isScanonRootDomain(String requestUrl) throws URISyntaxException {

        // Display scanon.in if root domain is accessed
        String domain = CommonUtil.getDomainName(requestUrl);
        return domain.toLowerCase().equals("quickappstore.com") || domain.toLowerCase().equals("scanon.in")
                || domain.toLowerCase().equals("qnovon.com") || domain.toLowerCase().equals("www.qnovon.com")
                || domain.toLowerCase().equals("qr3.in") || domain.toLowerCase().equals("hi8.in");
    }

    public static boolean is5ndInfoDomain(String requestUrl) throws URISyntaxException {

        // Display scanon.in if root domain is accessed
        String domain = CommonUtil.getDomainName(requestUrl);
        return domain.toLowerCase().contains("5nd.info");
    }

    public static boolean isLocalhost(String requestUrl) throws URISyntaxException {

        // Display scanon.in if root domain is accessed
        String domain = CommonUtil.getDomainName(requestUrl);
        return domain.toLowerCase().equals("localhost");
    }

    public static Date getDate(Date utcDate) {

        /*
         * String dateInString = "22-01-2015 10:15:55 AM";
         * 
         * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
         * 
         * Date date = formatter.parse(utcDate); TimeZone tz = TimeZone.getDefault();
         * 
         * // To TimeZone America/New_York // SimpleDateFormat sdfAmerica = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
         * SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"); TimeZone tzInAmerica =
         * TimeZone.getTimeZone("IST"); sdfAmerica.setTimeZone(tzInAmerica);
         * 
         * String sDateInAmerica = sdfAmerica.format(utcDate); // Convert to String first Date dateInAmerica =
         * formatter.parse(sDateInAmerica);
         */

        return null;
    }

    /**
     * Format Date (2016-29-02 22:29:35)
     * 
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-dd-mm HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(dateStr);

        return date;
    }

    public static String getRandomPassword() {

        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 7);
    }

    public static String getFileName(String url) {

        return new File(url).getName();

    }

    public static File getFile(MultipartFile multipart, Long appliteId) throws IllegalStateException, IOException {

        File convFile = new File(getTempFilePath(appliteId) + File.separator + multipart.getOriginalFilename());

        try {
            multipart.transferTo(convFile);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return convFile;
    }

    public static String getTempFilePath(Long appliteId) throws IOException {

        // create a temp file
        File tempFile = File.createTempFile("applite-users-" + appliteId, ".tmp");

        System.out.println("Temp file : " + tempFile.getAbsolutePath());

        // Get tempropary file path
        String absolutePath = tempFile.getAbsolutePath();
        String tempFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));

        return tempFilePath;
    }

    /**
     * Split user name to first name & last name
     * 
     * @param name
     * @return
     */
    public static String[] splitName(String name) {

        String[] names = { null, null };

        if (name == null) {
            return names;
        }

        String firstName = null;
        String lastName = null;
        if (name.split("\\w+").length > 1) {
            lastName = name.substring(name.lastIndexOf(" ") + 1);
            firstName = name.substring(0, name.lastIndexOf(" "));
        } else {
            firstName = name;
        }

        return new String[] { firstName, lastName };
    }
}