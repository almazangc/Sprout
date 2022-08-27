package com.example.sprout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

public class CustomTimePicker extends TimePicker {
    public CustomTimePicker(Context context){
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

    }

    public CustomTimePicker(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);

    }

    //Override this method to fix the problem of scrolling a time picker
    //when is inside a scrollView.
    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent){

        if ( motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){

            ViewParent viewParent = getParent();

            if ( viewParent != null )
                viewParent.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
