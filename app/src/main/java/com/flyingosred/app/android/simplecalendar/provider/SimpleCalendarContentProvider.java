/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.flyingosred.app.android.simplecalendar.database.LunarDatabase;
import com.flyingosred.app.android.simplecalendar.database.LunarDate;

import java.util.Calendar;

public class SimpleCalendarContentProvider extends ContentProvider {

    private static final String LOG_TAG = SimpleCalendarContentProvider.class.getSimpleName();

    private static final String WINDOW_NAME = SimpleCalendarContentProvider.class.getSimpleName();

    private static final int MAX_ROWS = 7;

    private static final int MONTH_ITEM_LIST = 1;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(SimpleCalendarContract.AUTHORITY, "month", MONTH_ITEM_LIST);
    }

    private final SolarTermProvider mSolarTermProvider = new SolarTermProvider();

    private final LunarProvider mLunarProvider = new LunarProvider();

    public SimpleCalendarContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType called uri is " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case MONTH_ITEM_LIST:
                return SimpleCalendarContract.Month.CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate called.");
        mSolarTermProvider.onCreate();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int year = 0;
        int month = 0;
        int firstDayOfWeek = 0;
        boolean showWeekNumber = false;
        for (String arg : selectionArgs) {
            if (arg.startsWith(SimpleCalendarContract.Month.SELECTION_ARG_YEAR)) {
                year = Integer.parseInt(arg.replace(SimpleCalendarContract.Month.SELECTION_ARG_YEAR, "").trim());
            } else if (arg.startsWith(SimpleCalendarContract.Month.SELECTION_ARG_MONTH)) {
                month = Integer.parseInt(arg.replace(SimpleCalendarContract.Month.SELECTION_ARG_MONTH, "").trim());
            } else if (arg.startsWith(SimpleCalendarContract.Month.SELECTION_ARG_SHOW_WEEK_NUMBER)) {
                showWeekNumber = Integer.parseInt(arg.
                        replace(SimpleCalendarContract.Month.SELECTION_ARG_SHOW_WEEK_NUMBER, "")
                        .trim()) == 1 ? true : false;
            } else if (arg.startsWith(SimpleCalendarContract.Month.SELECTION_ARG_FIRST_DAY_OF_WEEK)) {
                firstDayOfWeek = Integer.parseInt(arg.
                        replace(SimpleCalendarContract.Month.SELECTION_ARG_FIRST_DAY_OF_WEEK, "").trim());
            }
        }
        Log.d(LOG_TAG, "query called, year=" + year + ", month=" + month + ", firstDayOfWeek=" + firstDayOfWeek + ", showWeekNumber=" + showWeekNumber);
        if (year <= 0 || month <= 0 || !isDayOfWeekValid(firstDayOfWeek)) {
            throw new IllegalArgumentException("Query input are wrong");
        }
        return generateCursorData(year, month, firstDayOfWeek, showWeekNumber);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isDayOfWeekValid(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        return (dayOfWeek >= calendar.getActualMinimum(Calendar.DAY_OF_WEEK) && dayOfWeek <= calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
    }

    private Cursor generateCursorData(int year, int month, int firstDayOfWeek, boolean showWeekNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);
        int maxDaysInWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        int dayOffset = findDayOffset(dayOfWeekStart, firstDayOfWeek, maxDaysInWeek);
        calendar.add(Calendar.DATE, 0 - dayOffset);
        int cols = showWeekNumber ? (maxDaysInWeek + 1) : maxDaysInWeek;
        CursorWindow window = new CursorWindow(WINDOW_NAME);
        window.setNumColumns(cols);
        fillDaysOfWeek(firstDayOfWeek, window, showWeekNumber ? 1 : 0);
        for (int row = 1; row < MAX_ROWS; row++) {
            window.allocRow();
            for (int col = 0; col < cols; col++) {
                if (col == 0 && showWeekNumber) {
                    window.putLong(calendar.get(Calendar.WEEK_OF_YEAR), row, col);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_YEAR, calendar.get(Calendar.YEAR));
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_MONTH, calendar.get(Calendar.MONTH));
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_DAY, calendar.get(Calendar.DATE));
                    int solarTerm = mSolarTermProvider.get(calendar);
                    if (solarTerm >= 0) {
                        Log.d(LOG_TAG, "match solar term " + solarTerm + " for date " + calendar.getTime());
                        bundle.putInt(SimpleCalendarContract.Month.EXTRA_LUNAR_SOLAR_TERM, solarTerm);
                    }
                    LunarDate lunarDate = mLunarProvider.get(calendar);
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_LUNAR_YEAR, lunarDate.getYear());
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_LUNAR_MONTH, lunarDate.getMonth());
                    bundle.putInt(SimpleCalendarContract.Month.EXTRA_LUNAR_DAY, lunarDate.getDay());
                    bundle.putBoolean(SimpleCalendarContract.Month.EXTRA_LUNAR_IS_LEAP_MONTH, lunarDate.isLeapMonth());
                    Parcel parcel = Parcel.obtain();
                    bundle.writeToParcel(parcel, 0);
                    Log.d(LOG_TAG, "Write parse to row " + row + " col " + col + " size " + parcel.dataSize());
                    window.putBlob(parcel.marshall(), row, col);
                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
        SimpleCalendarCursor cursor = new SimpleCalendarCursor();
        cursor.setColumnNames(createColumnNames(cols));
        cursor.setWindow(window);
        return cursor;
    }

    private int findDayOffset(int dayOfWeekStart, int firstDayOfWeek, int maxDaysInWeek) {
        final int offset = dayOfWeekStart - firstDayOfWeek;
        if (dayOfWeekStart < firstDayOfWeek) {
            return offset + maxDaysInWeek;
        }
        return offset;
    }

    private void fillDaysOfWeek(int firstDayOfWeek, CursorWindow window, int offset) {
        window.allocRow();
        int row = window.getNumRows() - 1;
        int[] daysOfWeek = getDaysOfWeek(firstDayOfWeek);
        for (int i = 0; i < daysOfWeek.length; i++) {
            window.putLong(daysOfWeek[i], row, offset + i);
        }
    }

    private int[] getDaysOfWeek(int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        int count = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        int[] daysOfWeek = new int[count];
        for (int i = 0; i < count; i++) {
            daysOfWeek[i] = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return daysOfWeek;
    }

    private String[] createColumnNames(int cols) {
        String[] columnNames = new String[cols];
        for (int i = 0; i < cols; i++) {
            columnNames[i] = "DAY";
        }
        return columnNames;
    }
}
