/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class SimpleCalendarContract {

    public static final String AUTHORITY = "com.flyingosred.app.android.simplecalendar.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final int INVALID_ID = -1;

    public static final class Month implements BaseColumns {
        private Month() {
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "month");

        public static final String CONTENT_TYPE = "/com.flyingosred.app.android.simplecalendar.provider.month_items";

        public static final String[] PROJECTION_ALL = {"WEEK", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY"};

        public static final String[] PROJECTION_WITHOUT_WEEK = {"DAY", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY"};

        public static final String SELECTION_ARG_YEAR = "year=";

        public static final String SELECTION_ARG_MONTH = "month=";

        public static final String SELECTION_ARG_FIRST_DAY_OF_WEEK = "dayOfWeek=";

        public static final String SELECTION_ARG_SHOW_WEEK_NUMBER = "showWeekNumber=";

        public static final String EXTRA_YEAR = "year";
        public static final String EXTRA_MONTH = "month";
        public static final String EXTRA_DAY = "day";
        public static final String EXTRA_LUNAR_YEAR = "lunar_year";
        public static final String EXTRA_LUNAR_MONTH = "lunar_month";
        public static final String EXTRA_LUNAR_DAY = "lunar_day";
        public static final String EXTRA_LUNAR_IS_LEAP_MONTH = "lunar_is_leap_month";
        public static final String EXTRA_LUNAR_SOLAR_TERM = "solar_term";

        public static void notifyDirectoryChange(ContentResolver resolver) {
            ContentValues contentValues = new ContentValues();
            resolver.update(Month.CONTENT_URI, contentValues, null, null);
        }

    }
}
