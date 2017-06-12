package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ryosuke on 17/06/06.
 */

public class NonSwipableViewPager extends ViewPager {
    private boolean SWIPE_LOCK = false;


    public NonSwipableViewPager(Context context) {
        super(context);
    }

    public NonSwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(SWIPE_LOCK == true){
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(SWIPE_LOCK == true){
            return super.onTouchEvent(ev);
        } else {
            return false;
        }

    }


}
