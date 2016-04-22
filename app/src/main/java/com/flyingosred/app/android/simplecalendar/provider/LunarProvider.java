/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import com.flyingosred.app.android.simplecalendar.Util.Utils;
import com.flyingosred.app.android.simplecalendar.database.LunarDatabase;
import com.flyingosred.app.android.simplecalendar.database.LunarDate;

import java.util.Calendar;

public class LunarProvider {
    public LunarDate get(Calendar solarCalendar) {
        LunarDatabase database = new LunarDatabase();

        int lunarYear = solarCalendar.get(Calendar.YEAR);
        int lunarMonth = 0;
        int lunarDay;

        boolean isLastDayInMonth = false;
        boolean isLeapMonth = false;

        Calendar calendar = database.getSpringFestivalDay(lunarYear);

        int daysInMonth = 0;

        if (Utils.isSameDay(solarCalendar, calendar)) {
            lunarMonth = 1;
            lunarDay = 1;
        } else {
            if (Utils.isDayBefore(solarCalendar, calendar)) {
                lunarYear--;
                calendar = database.getSpringFestivalDay(lunarYear);
            }

            int leapMonth = database.getLeapMonth(lunarYear);

            for (int lunarMonthIndex = 1, bit = 0; lunarMonthIndex <= 12; lunarMonthIndex++, bit++) {
                if (leapMonth > 0 && (lunarMonthIndex == leapMonth + 1) && !isLeapMonth) {
                    lunarMonthIndex--;
                    isLeapMonth = true;
                } else {
                    isLeapMonth = false;
                }
                daysInMonth = database.getDaysInMonth(lunarYear, bit);
                calendar.add(Calendar.DATE, daysInMonth);
                if (Utils.isDayAfter(calendar, solarCalendar)) {
                    calendar.add(Calendar.DATE, 0 - daysInMonth);
                    lunarMonth = lunarMonthIndex;
                    break;
                }
            }
            lunarDay = 1;
            do {
                if (Utils.isSameDay(solarCalendar, calendar)) {
                    break;
                }
                calendar.add(Calendar.DATE, 1);
                lunarDay++;
            } while (true);
            if (lunarDay == daysInMonth) {
                isLastDayInMonth = true;
            }
        }
        LunarDate lunarDate = new LunarDate();
        lunarDate.setYear(lunarYear);
        lunarDate.setMonth(lunarMonth);
        lunarDate.setDay(lunarDay);
        lunarDate.setLastDayInMonth(isLastDayInMonth);
        lunarDate.setLeapMonth(isLeapMonth);
        return lunarDate;
    }
}
