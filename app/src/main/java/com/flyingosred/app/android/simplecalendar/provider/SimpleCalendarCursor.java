/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.provider;

import android.database.AbstractWindowedCursor;

public class SimpleCalendarCursor extends AbstractWindowedCursor {

    private String[] mColumnNames = null;

    public void setColumnNames(String[] columnNames) {
        mColumnNames = columnNames;
    }

    @Override
    public int getCount() {
        if (getWindow() != null) {
            return getWindow().getNumRows();
        }
        return 0;
    }

    @Override
    public String[] getColumnNames() {
        return mColumnNames;
    }
}
