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

    public static final class Month implements BaseColumns {
        private Month() {
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "month");

        public static final String CONTENT_TYPE = "/com.flyingosred.app.android.simplecalendar.provider.month_items";

        public static final String[] PROJECTION_ALL = {"WEEK", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY"};

        public static final String[] PROJECTION_WITHOUT_WEEK = {"DAY", "DAY", "DAY", "DAY", "DAY", "DAY", "DAY"};

        public static void notifyDirectoryChange(ContentResolver resolver) {
            ContentValues contentValues = new ContentValues();
            resolver.update(Month.CONTENT_URI, contentValues, null, null);
        }

    }
}
