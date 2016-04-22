/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.flyingosred.app.android.simplecalendar.database.LunarDatabase;

import java.util.Calendar;

public class MonthSelectDialog implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private static final String LOG_TAG = MonthSelectDialog.class.getSimpleName();

    private AlertDialog mDialog;

    private OnMonthSelectedListener mMonthSelectedListener;

    private NumberPicker mYearPicker;

    private NumberPicker mMonthPicker;

    public MonthSelectDialog(Context context, int year, int month, OnMonthSelectedListener listener) {
        mMonthSelectedListener = listener;
        initView(context, year, month);
    }

    private void initView(Context context, int year, int month) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_month_select, null);

        mDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.month_select_dialog_title)
                .setView(dialogView)
                .setCancelable(true)
                .setOnCancelListener(this)
                .setPositiveButton(R.string.month_select_dialog_positive_button_text, this)
                .setNegativeButton(R.string.month_select_dialog_negative_button_text, this)
                .create();
        Calendar calendar = Calendar.getInstance();
        mYearPicker = (NumberPicker) dialogView.findViewById(R.id.month_select_dialog_picker_year);
        mYearPicker.setMaxValue(LunarDatabase.LUNAR_YEAR_MAX);
        mYearPicker.setMinValue(LunarDatabase.LUNAR_YEAR_MIN);
        mYearPicker.setValue(year);
        mMonthPicker = (NumberPicker) dialogView.findViewById(R.id.month_select_dialog_picker_month);
        mMonthPicker.setMaxValue(calendar.getActualMaximum(Calendar.MONTH) + 1);
        mMonthPicker.setMinValue(calendar.getActualMinimum(Calendar.MONTH) + 1);
        mMonthPicker.setValue(month + 1);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mMonthSelectedListener.onMonthSelected(mYearPicker.getValue(), mMonthPicker.getValue() - 1);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    public void show() {
        mDialog.show();
    }

    public interface OnMonthSelectedListener {
        public void onMonthSelected(int year, int month);
    }
}
