package com.app.rbc.admin.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by rohit on 1/6/17.
 */

public class MySpinner extends android.support.v7.widget.AppCompatSpinner {
    OnItemSelectedListener listener;

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }
}