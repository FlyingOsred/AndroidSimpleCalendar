/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.Util;

import java.util.Calendar;

public final class Utils {
    public static boolean isSameDay(Calendar day1, Calendar day2) {
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
                day1.get(Calendar.MONTH) == day2.get(Calendar.MONTH) &&
                day1.get(Calendar.DATE) == day2.get(Calendar.DATE);
    }

    public static boolean isDayBefore(Calendar calendar, Calendar baseCalendar) {

        if (calendar.get(Calendar.YEAR) < baseCalendar.get(Calendar.YEAR)) {
            return true;
        } else if (calendar.get(Calendar.YEAR) > baseCalendar.get(Calendar.YEAR)) {
            return false;
        }

        if (calendar.get(Calendar.MONTH) < baseCalendar.get(Calendar.MONTH)) {
            return true;
        } else if (calendar.get(Calendar.MONTH) > baseCalendar.get(Calendar.MONTH)) {
            return false;
        }

        if (calendar.get(Calendar.DATE) < baseCalendar.get(Calendar.DATE)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDayAfter(Calendar calendar, Calendar baseCalendar) {
        if (calendar.get(Calendar.YEAR) > baseCalendar.get(Calendar.YEAR)) {
            return true;
        } else if (calendar.get(Calendar.YEAR) < baseCalendar.get(Calendar.YEAR)) {
            return false;
        }

        if (calendar.get(Calendar.MONTH) > baseCalendar.get(Calendar.MONTH)) {
            return true;
        } else if (calendar.get(Calendar.MONTH) < baseCalendar.get(Calendar.MONTH)) {
            return false;
        }

        if (calendar.get(Calendar.DATE) > baseCalendar.get(Calendar.DATE)) {
            return true;
        } else {
            return false;
        }
    }
}
