package com.dtlim.bantaystocks.common.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dale on 7/14/16.
 */
public class DateUtility {
    private static SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd hh:mmaa", Locale.US);
    private static SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH); //2016-07-14T14:06:00+08:00

    public static String parseUnixTimestampToDisplay(String millis) {
        try {
            long unix = Long.valueOf(millis);
            Date date = new Date(unix);
            return displayFormat.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return millis;
    }

    public static String parseApiToUnixTimestamp(String apiDate) {
        try {
            Date date = apiFormat.parse(apiDate);
            long unixTime = date.getTime();
            return unixTime + "";
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return apiDate;
    }
}
