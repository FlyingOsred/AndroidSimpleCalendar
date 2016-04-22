/*
 * Copyright (c) 2016. Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */

package com.flyingosred.app.android.simplecalendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MonthRecyclerView extends RecyclerView {

    private static final String LOG_TAG = MonthRecyclerView.class.getSimpleName();

    private final GestureDetector mGestureDetector;

    private OnGestureListener mOnGestureListener;

    public MonthRecyclerView(Context context) {
        this(context, null);
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public void setOnGestureListener(OnGestureListener listener) {
        mOnGestureListener = listener;
    }

    public interface OnGestureListener {
        public void onSwipeLeft();

        public void onSwipeRight();

        public void onSwipeDown();

        public void onSwipeUp();
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            if (e2 != null && e1 != null) {
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                Log.d(LOG_TAG, "onSwipeRight");
                                if (mOnGestureListener != null) {
                                    mOnGestureListener.onSwipeRight();
                                }
                            } else {
                                Log.d(LOG_TAG, "onSwipeLeft");
                                if (mOnGestureListener != null) {
                                    mOnGestureListener.onSwipeLeft();
                                }
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            Log.d(LOG_TAG, "onSwipeDown");
                            if (mOnGestureListener != null) {
                                mOnGestureListener.onSwipeDown();
                            }
                        } else {
                            Log.d(LOG_TAG, "onSwipeUp");
                            if (mOnGestureListener != null) {
                                mOnGestureListener.onSwipeUp();
                            }
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return result;
        }
    }
}
