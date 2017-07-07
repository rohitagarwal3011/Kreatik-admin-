package com.app.rbc.admin.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by rohit on 14/4/17.
 */

public class AdapterWithCustomItem extends ArrayAdapter<String>
{
    private final static int POSITION_USER_DEFINED = 2;

    private String mCustomText = "";

    public AdapterWithCustomItem(Context context , String[] OPTIONS){
        super(context, android.R.layout.simple_spinner_dropdown_item, OPTIONS);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position == POSITION_USER_DEFINED) {
            TextView tv = (TextView)view.findViewById(android.R.id.text1);
            tv.setText(mCustomText);
        }

        return view;
    }

    public void setCustomText(String customText) {
        // Call to set the text that must be shown in the spinner for the custom option.
        mCustomText = customText;
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // No need for this override, actually. It's just to clarify the difference.
        return super.getDropDownView(position, convertView, parent);
    }
}
