package ourfood.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String getDisplayDate(Date date) {

        if (date == null) {
            return null;
        }
        
        TimeZone timeZone = TimeZone.getDefault();
        
        if (timeZone.getID().equalsIgnoreCase("Etc/UTC")) {
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("IST"));
            return formatter.format(date);
        } else {
            return new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(date);
        }
    }

    public static String getDisplayDateSortableFormat(Date date) {

        if (date == null) {
            return null;
        }
        
        TimeZone timeZone = TimeZone.getDefault();
        
        if (timeZone.getID().equalsIgnoreCase("Etc/UTC")) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("IST"));
            return formatter.format(date);
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        }
    }

    /**
     * Date String without timezone fix.
     * 
     * @param date
     * @return
     */
    public static String getDisplayDateAsIs(Date date) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(date);
    }

    public static Date getDate(String date) {

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("IST"));
        
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
        }

        return null;
    }
}