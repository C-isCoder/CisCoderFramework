package com.baichang.android.circle.widget.photopreview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by iscod.
 * Time:2016/9/27-9:45.
 */

public class PhotoGalleryViewPager extends ViewPager {
    public PhotoGalleryViewPager(Context context) {
        super(context);
    }

    public PhotoGalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
        }
        return false;
    }
}
