/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.flyingosred.app.android.simplecalendar.provider.SimpleCalendarContract;

import java.util.Calendar;

public class MonthCursorLoader extends CursorLoader {

    private static final String LOG_TAG = MonthCursorLoader.class.getSimpleName();

    private static final int MAX_SELECTION_ARGS = 4;

    private Calendar mCalendar = Calendar.getInstance();

    private final SharedPreferences mSharedPreferences;

    private boolean mShowWeekNumber = false;

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    public MonthCursorLoader(Context context) {
        super(context);
        setUri(SimpleCalendarContract.Month.CONTENT_URI);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mShowWeekNumber = getPrefShowWeekNumber();
        mFirstDayOfWeek = getPrefFirstDayOfWeek();
    }

    @Override
    protected void onForceLoad() {
        prepareSelectionArgs();
        super.onForceLoad();
    }

    @Override
    protected void onStartLoading() {
        prepareSelectionArgs();
        super.onStartLoading();
    }

    public Calendar getDate() {
        return (Calendar) mCalendar.clone();
    }

    public void setToday() {
        mCalendar = Calendar.getInstance();
        forceLoad();
    }

    public void setMonth(int year, int month) {
        mCalendar.set(year, month, 1);
        forceLoad();
    }

    public void nextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        forceLoad();
    }

    public void previousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        forceLoad();
    }

    private boolean getPrefShowWeekNumber() {
        return mSharedPreferences.getBoolean(getContext().getString(R.string.key_pref_show_week_number), false);
    }

    private int getPrefFirstDayOfWeek() {
        int firstDayOfWeek = 0;
        String value = mSharedPreferences.getString(getContext().getString(R.string.key_pref_first_day_of_week), null);
        if (value != null) {
            firstDayOfWeek = Integer.parseInt(value);
        }
        return validateFirstDayOfWeek(firstDayOfWeek);
    }

    private boolean isDayOfWeekValid(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        return (dayOfWeek >= calendar.getActualMinimum(Calendar.DAY_OF_WEEK) && dayOfWeek <= calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
    }

    private int validateFirstDayOfWeek(int firstDayOfWeek) {
        if (!isDayOfWeekValid(firstDayOfWeek)) {
            return Calendar.getInstance().getFirstDayOfWeek();
        }
        return firstDayOfWeek;
    }

    private void prepareSelectionArgs() {
        Log.d(LOG_TAG, "Preparing selection arguments...");
        String[] selectionArgs = new String[MAX_SELECTION_ARGS];
        selectionArgs[0] = SimpleCalendarContract.Month.SELECTION_ARG_YEAR + mCalendar.get(Calendar.YEAR);
        selectionArgs[1] = SimpleCalendarContract.Month.SELECTION_ARG_MONTH + mCalendar.get(Calendar.MONTH);
        selectionArgs[2] = SimpleCalendarContract.Month.SELECTION_ARG_FIRST_DAY_OF_WEEK + mFirstDayOfWeek;
        selectionArgs[3] = SimpleCalendarContract.Month.SELECTION_ARG_SHOW_WEEK_NUMBER + (mShowWeekNumber ? 1 : 0);
        setSelectionArgs(selectionArgs);
    }
}
