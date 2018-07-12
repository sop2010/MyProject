package com.comons.uitl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date parse2Milliseconds(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss,SSS");
    }

    public static Date parse(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String date, String pattern) {
        Date obj = null;
        if (date != null && !date.isEmpty()) {
            try {
                obj = new SimpleDateFormat(pattern).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format2Milliseconds(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss,SSS");
    }

    public static String format2yyyyMMddHHmmss(Date date) {
        return format(date, "yyyyMMddHHmmss");
    }

    public static String format2yyyyMMddHHmmss() {
        return format2yyyyMMddHHmmss(new Date());
    }

    public static String format(Date date, String pattern) {
        String datestr = null;
        if (date != null) {
            datestr = new SimpleDateFormat(pattern).format(date);
        }
        return datestr;
    }
}
