package com.flyingosred.app.android.simplecalendar;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.flyingosred.app.android.simplecalendar.adapter.MonthRecyclerViewAdapter;
import com.flyingosred.app.android.simplecalendar.view.MonthRecyclerView;

import java.util.Calendar;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MonthSelectDialog.OnMonthSelectedListener, MonthRecyclerView.OnGestureListener {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;

    private MonthRecyclerViewAdapter mMonthViewAdapter = null;

    private MenuItem mMonthMenuItem = null;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Log.d(LOG_TAG, "onCreate called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        MonthRecyclerView monthView = (MonthRecyclerView) view.findViewById(R.id.month_recycler_view);
        monthView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        monthView.setOnGestureListener(this);
        monthView.setLayoutManager(layoutManager);
        mMonthViewAdapter = new MonthRecyclerViewAdapter(layoutManager);
        monthView.setAdapter(mMonthViewAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        Log.d(LOG_TAG, "onActivityCreated called");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionsMenu called");
        getActivity().getMenuInflater().inflate(R.menu.menu_month_fregment, menu);
        mMonthMenuItem = menu.findItem(R.id.action_current_month);
        updateMonth();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d(LOG_TAG, "option selected " + id);

        if (id == R.id.action_today) {
            getMonthCursorLoader().setToday();
            return true;
        } else if (id == R.id.action_current_month) {
            Calendar calendar = getMonthCursorLoader().getDate();
            MonthSelectDialog dialog = new MonthSelectDialog(getContext(),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), this);
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader called");
        return new MonthCursorLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (data != null) {
            Log.d(LOG_TAG, "onLoadFinished data count is " + data.getCount() + " column count is " + data.getColumnCount());
        }
        updateMonth();
        mMonthViewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset called");
        mMonthViewAdapter.swapCursor(null);
    }

    private void updateMonth() {
        if (mMonthMenuItem != null && getMonthCursorLoader() != null) {
            Calendar calendar = getMonthCursorLoader().getDate();
            String date = DateUtils.formatDateRange(
                    getContext(),
                    calendar.getTimeInMillis(),
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY
                            | DateUtils.FORMAT_SHOW_YEAR).toString().toUpperCase(Locale.getDefault());
            mMonthMenuItem.setTitle(date);
        }
    }

    @Override
    public void onMonthSelected(int year, int month) {
        Log.d(LOG_TAG, "onMonthSelected year=" + year + ", month=" + month);
        getMonthCursorLoader().setMonth(year, month);
    }

    private MonthCursorLoader getMonthCursorLoader() {
        MonthCursorLoader monthCursorLoader = null;
        Loader<Cursor> loader = getActivity().getSupportLoaderManager().getLoader(LOADER_ID);
        if (loader != null) {
            monthCursorLoader = (MonthCursorLoader) loader;
        }
        return monthCursorLoader;
    }

    @Override
    public void onSwipeLeft() {
        getMonthCursorLoader().nextMonth();
    }

    @Override
    public void onSwipeRight() {
        getMonthCursorLoader().previousMonth();
    }

    @Override
    public void onSwipeDown() {
        getMonthCursorLoader().previousMonth();
    }

    @Override
    public void onSwipeUp() {
        getMonthCursorLoader().nextMonth();
    }
}
