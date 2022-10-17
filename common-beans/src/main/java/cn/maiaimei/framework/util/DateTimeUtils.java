package cn.maiaimei.framework.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {
    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String HH_MM = "HH:mm";

    private DateTimeUtils() {
        throw new UnsupportedOperationException();
    }

    public static LocalDate date2LocalDate(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalDate();
    }

    public static LocalDate date2LocalDate(Date date, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalDate();
    }

    public static LocalTime date2LocalTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalTime();
    }

    public static LocalTime date2LocalTime(Date date, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalTime();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalDateTime();
    }

    public static LocalTime date2LocalDateTime(Date date, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalTime();
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToDate(LocalDate localDate, ZoneId zoneId) {
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public static boolean isValidDate(String value) {
        return isValidDateTime(value, YYYY_MM_DD);
    }

    public static boolean isValidTime(String value) {
        return isValidDateTime(value, HH_MM);
    }

    public static boolean isValidDateTime(String value, String format) {
        if (value == null) {
            return true;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        try {
            dtf.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
