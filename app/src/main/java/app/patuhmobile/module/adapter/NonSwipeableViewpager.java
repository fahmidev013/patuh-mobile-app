package app.patuhmobile.module.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Fahmi Hakim on 03/06/2018.
 * for SERA
 */

public class NonSwipeableViewpager extends ViewPager {
    private boolean swipeable;



    public NonSwipeableViewpager(Context context) {

        super(context);

    }


    public NonSwipeableViewpager(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.swipeable = true;

    }


    @Override

    public boolean onTouchEvent(MotionEvent event) {

        if (this.swipeable) {

            return super.onTouchEvent(event);

        }


        return false;

    }


    @Override

    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.swipeable) {

            return super.onInterceptTouchEvent(event);

        }


        return false;

    }


    public void setSwipeable(boolean swipeable) {

        this.swipeable = swipeable;

    }
}
