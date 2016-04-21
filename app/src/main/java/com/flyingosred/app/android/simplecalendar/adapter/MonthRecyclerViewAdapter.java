/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.adapter;

import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.simplecalendar.R;

import java.util.Calendar;
import java.util.Locale;

public class MonthRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final String LOG_TAG = MonthRecyclerViewAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_WEEK = 1;

    private static final int VIEW_TYPE_DAY_OF_WEEK = 2;

    private static final int VIEW_TYPE_WEEK_NUMBER = 3;

    private static final int VIEW_TYPE_MONTH_DAY = 4;

    private Cursor mCursor = null;

    private GridLayoutManager mLayoutManager;

    public MonthRecyclerViewAdapter(GridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_WEEK) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item_week, parent, false);
            viewHolder = new WeekViewHolder(v);
        } else if (viewType == VIEW_TYPE_DAY_OF_WEEK) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item_day_of_week, parent, false);
            viewHolder = new DayOfWeekViewHolder(v);
        } else {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item_week, parent, false);
            viewHolder = new WeekViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder position is " + position);
        if (mCursor != null) {
            int cols = mCursor.getColumnCount();
            int row = position / cols;
            int col = position % cols;
            int viewType = holder.getItemViewType();
            if (viewType == VIEW_TYPE_DAY_OF_WEEK) {
                DayOfWeekViewHolder dayOfWeekViewHolder = (DayOfWeekViewHolder) holder;
                mCursor.moveToPosition(row);
                int dayOfWeek = mCursor.getInt(col);
                Log.d(LOG_TAG, "dayOfWeek is " + dayOfWeek);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                String dayOfWeekString = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                        Locale.getDefault()).toUpperCase(Locale.getDefault());
                dayOfWeekViewHolder.mTextView.setText(dayOfWeekString);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCursor != null) {
            Calendar calendar = Calendar.getInstance();
            int maxDaysOfWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
            boolean hasWeekNumber = false;
            int cols = mCursor.getColumnCount();
            if (cols > maxDaysOfWeek) {
                hasWeekNumber = true;
            }
            if (hasWeekNumber) {
                if (position == 0) {
                    return VIEW_TYPE_WEEK;
                } else if (position > 0 && position <= maxDaysOfWeek) {
                    return VIEW_TYPE_DAY_OF_WEEK;
                } else if (position % cols == 0) {
                    return VIEW_TYPE_WEEK_NUMBER;
                } else {
                    return VIEW_TYPE_MONTH_DAY;
                }
            } else {
                if (position >= 0 && position < cols) {
                    return VIEW_TYPE_DAY_OF_WEEK;
                } else {
                    return VIEW_TYPE_MONTH_DAY;
                }
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount() * mCursor.getColumnCount();
        }
        return 0;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (mLayoutManager.getSpanCount() != newCursor.getColumnCount()) {
            mLayoutManager.setSpanCount(newCursor.getColumnCount());
        }
        notifyDataSetChanged();
        return oldCursor;
    }

    public static class DayOfWeekViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public DayOfWeekViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }

    public static class WeekNumberViewHolder extends RecyclerView.ViewHolder {
        public WeekNumberViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        public WeekViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MonthDayViewHolder extends RecyclerView.ViewHolder {
        public MonthDayViewHolder(View itemView) {
            super(itemView);
        }
    }
}
