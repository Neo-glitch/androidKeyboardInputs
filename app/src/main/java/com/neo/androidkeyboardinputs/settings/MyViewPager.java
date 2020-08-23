package com.neo.androidkeyboardinputs.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * custom View Pager class that doesn't allow d pad left and right to swap btw fragments
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * method overridden to disable dpad nav to tabs
     */
    @Override
    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        return false;
    }
}
