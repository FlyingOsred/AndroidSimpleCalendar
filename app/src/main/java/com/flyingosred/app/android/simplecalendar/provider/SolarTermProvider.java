/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import com.flyingosred.app.android.simplecalendar.database.SolarTermDatabase;
import com.flyingosred.app.android.simplecalendar.database.SolarTermDatabaseItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class SolarTermProvider {

    private HashMap<SolarTermDatabaseItem, Integer> mSolarTermMap = new HashMap<>();

    public void onCreate() {
        init();
    }

    public void onReset() {
        mSolarTermMap.clear();
        init();
    }

    public void onInvalidate() {
        mSolarTermMap.clear();
    }

    public int get(Calendar calendar) {
        SolarTermDatabaseItem item = new SolarTermDatabaseItem(calendar);
        if (mSolarTermMap.containsKey(item)) {
            return mSolarTermMap.get(item);
        }
        return SimpleCalendarContract.INVALID_ID;
    }

    private void init() {
        for (int i = 0; i < SolarTermDatabase.DATABASE.length; i++) {
            int year = SolarTermDatabase.START_YEAR + i;
            int[][] yearData = SolarTermDatabase.DATABASE[i];
            for (int j = 0; j < yearData.length; j++) {
                int[] dateData = yearData[j];
                int month = dateData[0] - 1;
                int day = dateData[1];
                int hour = dateData[2];
                int minute = dateData[3];
                SolarTermDatabaseItem item = new SolarTermDatabaseItem(
                        year, month, day, hour, minute, 0,
                        TimeZone.getTimeZone(SolarTermDatabase.TIMEZONE));
                mSolarTermMap.put(item, j);
            }
        }
    }
}
