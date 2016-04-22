/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.database;

public final class LunarDate {

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean mLastDayInMonth;
    private boolean mLeapMonth;

    public void setYear(int year) {
        mYear = year;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public void setLastDayInMonth(boolean lastDayInMonth) {
        mLastDayInMonth = lastDayInMonth;
    }

    public void setLeapMonth(boolean leapMonth) {
        mLeapMonth = leapMonth;
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

    public boolean isLastDayInMonth() {
        return mLastDayInMonth;
    }

    public boolean isLeapMonth() {
        return mLeapMonth;
    }
}
