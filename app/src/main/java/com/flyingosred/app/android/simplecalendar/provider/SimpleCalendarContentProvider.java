/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.Log;

import com.flyingosred.app.android.simplecalendar.R;

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

    private boolean mShowWeekNumber = false;

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    private SharedPreferences mSharedPreferences;

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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSolarTermProvider.onCreate();
        mShowWeekNumber = getPrefShowWeekNumber();
        mFirstDayOfWeek = getPrefFirstDayOfWeek();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int year = Integer.parseInt(selectionArgs[0]);
        int month = Integer.parseInt(selectionArgs[1]);
        Log.d(LOG_TAG, "query called, year=" + year + ", month=" + month);
        return generateCursorData(year, month, mFirstDayOfWeek, mShowWeekNumber);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
        fillDaysOfWeek(mFirstDayOfWeek, window, showWeekNumber ? 1 : 0);
        for (int row = window.getNumRows(); row < MAX_ROWS; row++) {
            window.allocRow();
            for (int col = 0; col < cols; col++) {
                if (col == 0 && showWeekNumber) {
                    window.putLong(calendar.get(Calendar.WEEK_OF_YEAR), row, col);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("year", calendar.get(Calendar.YEAR));
                    bundle.putInt("month", calendar.get(Calendar.MONTH));
                    bundle.putInt("day", calendar.get(Calendar.DATE));
                    Parcel parcel = Parcel.obtain();
                    bundle.writeToParcel(parcel, 0);
                    window.putBlob(parcel.createByteArray(), row, col);
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
