package cn.shenjunjie.booking.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author junjie.shen
 * @date 2020-2-8
 * @description
 */
public class DateUtil {

    public static long MILLISECONDOFDAY = 1000 * 24 * 60 * 60;
    public static long MILLISECONDOFHOUR = 1000 * 60 * 60;
    public static long MILLISECONDOFMINUTE = 1000 * 60;
    public static long MILLISECONDOFSECOND = 1000;

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ADDITION = " +0800 CST";

    public static Date formatDateByString(String dateStr) {
        return formatDateByString(dateStr, DATE_PATTERN);
    }

    public static Date formatDateByAdditionString(String dateStr) {
        if(dateStr.contains(ADDITION)){
            dateStr = dateStr.substring(0,dateStr.indexOf(ADDITION));
        }
        return formatDateByString(dateStr);
    }

    public static Date formatDateByString(String dateStr, String datePattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    //判断date与当前时间的误差是否小于30s
    public static boolean checkExecutedAtIsRational(Date date, Long toleranceTime) {
        Long range = date.getTime() - System.currentTimeMillis();
        if (range < toleranceTime && range > -toleranceTime) {
            return true;
        }
        return false;
    }

    public static String formatString(Long time) {
        if (time < 0) {
            return null;
        } else if (time <= 60000) {
            return time / 1000 + "s";
        } else {
            return time / 60000 + "min" + (time % 60000) / 1000 + "s";
        }
    }

    public static long getDayDiff(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / MILLISECONDOFDAY;
    }

    public static long getHourDiff(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / MILLISECONDOFHOUR;
    }

    public static long getMinuteDiff(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / MILLISECONDOFMINUTE;
    }

    public static long getSecondDiff(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / MILLISECONDOFSECOND;
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String format(Date date, TimeZone timeZone, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }
}