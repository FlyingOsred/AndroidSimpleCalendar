/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyingosred.app.android.simplecalendar.R;
import com.flyingosred.app.android.simplecalendar.provider.SimpleCalendarContract;

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

    private int mActivatedPosition = AdapterView.INVALID_POSITION;

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
        } else if (viewType == VIEW_TYPE_MONTH_DAY) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item_month_day, parent, false);
            viewHolder = new MonthDayViewHolder(v, parent.getContext());
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
            mCursor.moveToPosition(row);
            Log.d(LOG_TAG, "onBindViewHolder getItemViewType is " + viewType);
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
            } else if (viewType == VIEW_TYPE_MONTH_DAY) {
                MonthDayViewHolder monthDayViewHolder = (MonthDayViewHolder) holder;
                monthDayViewHolder.onBind(mCursor, col, position);
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
        if (newCursor != null && (mLayoutManager.getSpanCount() != newCursor.getColumnCount())) {
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

    public static class MonthDayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mItemView;
        public TextView mDateTextView;
        public TextView mInfoTextView;
        public TextView mTodayTextView;
        public TextView mOffOrWorkTextView;

        private int mPosition = -1;

        private final Context mContext;

        public MonthDayViewHolder(View itemView, Context context) {
            super(itemView);
            mItemView = itemView;
            itemView.setOnClickListener(this);
            mDateTextView = (TextView) itemView.findViewById(R.id.month_day_date_text_view);
            mInfoTextView = (TextView) itemView.findViewById(R.id.month_day_info_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.month_day_today_text_view);
            mOffOrWorkTextView = (TextView) itemView.findViewById(R.id.month_day_off_or_work_text_view);
            mContext = context;
        }

        public void onBind(Cursor cursor, int col, int position) {
            mPosition = position;
            Parcel parcel = Parcel.obtain();
            byte[] data = cursor.getBlob(col);
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            Bundle bundle = new Bundle();
            bundle.readFromParcel(parcel);
            int year = bundle.getInt(SimpleCalendarContract.Month.EXTRA_YEAR);
            int month = bundle.getInt(SimpleCalendarContract.Month.EXTRA_MONTH);
            int day = bundle.getInt(SimpleCalendarContract.Month.EXTRA_DAY);
            int solarTerm = bundle.getInt(SimpleCalendarContract.Month.EXTRA_LUNAR_SOLAR_TERM, SimpleCalendarContract.INVALID_ID);
            int lunarYear = bundle.getInt(SimpleCalendarContract.Month.EXTRA_LUNAR_YEAR);
            int lunarMonth = bundle.getInt(SimpleCalendarContract.Month.EXTRA_LUNAR_MONTH);
            int lunarDay = bundle.getInt(SimpleCalendarContract.Month.EXTRA_LUNAR_DAY);
            if (solarTerm >= 0) {
                mInfoTextView.setText(mContext.getResources().getStringArray(R.array.solar_term)[solarTerm]);
            } else {
                mInfoTextView.setText(formatLunarDayShortString(mContext, lunarMonth, lunarDay));
            }
            if (isToday(year, month, day)) {
                mTodayTextView.setText(R.string.month_day_today_text);
                mItemView.setActivated(true);
            } else {
                mTodayTextView.setText("");
            }
            mDateTextView.setText(String.valueOf(day));
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onClick mPosition is " + mPosition);
            v.setActivated(true);
        }
    }

    private static boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        if (year == calendar.get(Calendar.YEAR)
                && month == calendar.get(Calendar.MONTH)
                && day == calendar.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    private static String formatLunarDayShortString(Context context, int lunarMonth, int lunarDay) {
        Locale locale = Locale.getDefault();
        if (locale == Locale.CHINESE || locale == Locale.CHINA || locale == Locale.SIMPLIFIED_CHINESE
                || locale == Locale.TRADITIONAL_CHINESE || locale == locale.TAIWAN) {
            return formatChineseLunarDayShortString(context, lunarMonth, lunarDay);
        }
        return lunarMonth + "/" + lunarDay;
    }

    private static String formatChineseLunarDayShortString(Context context, int lunarMonth, int lunarDay) {
        if (lunarDay == 1) {
            return getChineseMonthName(context, lunarMonth);
        }
        StringBuilder sb = new StringBuilder();
        if (lunarDay <= 10) {
            sb.append(context.getString(R.string.chinese_day_prefix_name));
        }
        sb.append(getChineseNumber(context, lunarDay));
        return sb.toString();
    }

    public static String getChineseMonthName(Context context, int month) {
        String prefix = null;
        if (month == 1) {
            prefix = context.getString(R.string.chinese_first_month_prefix);
        } else if (month == 12) {
            prefix = context.getString(R.string.chinese_last_month_prefix);
        } else {
            prefix = getChineseNumber(context, month);
        }
        return prefix + context.getString(R.string.chinese_month_name);
    }

    public static String getChineseNumber(Context context, int number) {
        String[] array = context.getResources().getStringArray(R.array.chinese_number);
        if (number <= array.length) {
            return array[number - 1];
        }

        StringBuilder chineseNumber = new StringBuilder();
        int tensPlace = number / 10;
        if (tensPlace > 1) {
            chineseNumber.append(array[tensPlace - 1]);
        }
        chineseNumber.append(array[array.length - 1]);
        int unitPlace = number % 10;
        if (unitPlace != 0) {
            chineseNumber.append(array[unitPlace - 1]);
        }
        return chineseNumber.toString();
    }

}
