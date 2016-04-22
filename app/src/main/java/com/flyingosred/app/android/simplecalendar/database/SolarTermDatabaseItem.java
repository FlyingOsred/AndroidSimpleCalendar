package com.flyingosred.app.android.simplecalendar.database;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class SolarTermDatabaseItem {

    private final int mYear;

    private final int mMonth;

    private final int mDay;

    public SolarTermDatabaseItem(Calendar calendar) {
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
    }

    public SolarTermDatabaseItem(int year, int month, int day, int hour, int minute, int second, TimeZone timezone) {
        Calendar calendar = Calendar.getInstance(timezone, Locale.getDefault());
        calendar.set(year, month, day, hour, minute, second);
        calendar.setTimeZone(TimeZone.getDefault());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o.getClass() != getClass())
            return false;
        SolarTermDatabaseItem e = (SolarTermDatabaseItem) o;
        if (e.getYear() == mYear && e.getMonth() == mMonth && e.getDay() == mDay) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (mDay & 0x1F) | ((mMonth & 0xF) << 5) | (mYear << 9);
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }
}
