package com.flyingosred.app.android.simplecalendar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.flyingosred.app.android.simplecalendar.adapter.MonthRecyclerViewAdapter;
import com.flyingosred.app.android.simplecalendar.provider.SimpleCalendarContract;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 1;

    private Calendar mCalendar = Calendar.getInstance();

    private MonthRecyclerViewAdapter mMonthViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        RecyclerView monthView = (RecyclerView) findViewById(R.id.month_recycler_view);
        monthView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);
        monthView.setLayoutManager(layoutManager);
        mMonthViewAdapter = new MonthRecyclerViewAdapter(layoutManager);
        monthView.setAdapter(mMonthViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {String.valueOf(mCalendar.get(Calendar.YEAR)), String.valueOf(mCalendar.get(Calendar.MONTH))};
        return new CursorLoader(this, SimpleCalendarContract.Month.CONTENT_URI, null, null, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (data != null) {
            Log.d(LOG_TAG, "onLoadFinished data count is " + data.getCount() + " column count is " + data.getColumnCount());
        }
        mMonthViewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
