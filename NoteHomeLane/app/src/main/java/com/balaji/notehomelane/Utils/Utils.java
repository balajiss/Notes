package com.balaji.notehomelane.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by balaji on 16/01/18.
 */

public class Utils {

    private static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss a";
    private static String DATE_FORMAT = "EEE, d MMM yyyy";
    private static String TIME_FORMAT = "h:mm aaa";

    public static long getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static String getFormattedDateTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.format(date);
    }

    public static long getTimeInMillisFromString(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateStr);
        return date.getTime();
    }

    public static String getReadableDateTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        DateFormat f = new SimpleDateFormat(DATE_TIME_FORMAT);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }
}
