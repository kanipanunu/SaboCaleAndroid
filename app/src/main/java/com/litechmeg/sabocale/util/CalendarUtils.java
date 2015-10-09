package com.litechmeg.sabocale.util;

import java.util.Calendar;

/**
 * Created by fukuo on 2015/10/05.
 */
public class CalendarUtils {

    public static Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar;
    }

    public static Calendar getCalendar(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);

        return calendar;
    }

    public static Calendar truncate(Calendar calendar) {
        long timeInMills = calendar.getTimeInMillis();
        long dayInMills = 24 * 60 * 60 * 1000;
        long diffTimeInMills = timeInMills % dayInMills;

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(timeInMills - diffTimeInMills);

        return newCalendar;
    }

    public static boolean isSameDay(Calendar lhs, Calendar rhs) {
        return truncate(lhs) == truncate(rhs);
    }
}
