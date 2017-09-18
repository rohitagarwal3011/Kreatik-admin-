package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;


/**
 * Created by jeet on 4/9/17.
 */

public class CustomSetingsListAdapter extends RecyclerView.Adapter<CustomSetingsListAdapter.MyViewHolder> {

    private String[] titles;
    private Context context;
    public CustomSetingsListAdapter(Context context,String[] titles) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_settings_list_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.item_title.setText(titles[position]);
        int icon_res = 0;
        switch (position) {
            case 0:icon_res = R.drawable.user_border;
                break;
            case 1:icon_res = R.drawable.employees;
                break;
            case 2:icon_res = R.drawable.sites;
                break;
            case 3:icon_res = R.drawable.vendors;
                break;
            case 4:icon_res = R.drawable.categories;
                break;
        }
        holder.item_icon.setImageDrawable(context.getResources().getDrawable(icon_res));

        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IndentRegisterActivity)context).setFragment(position+1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView item_title;
        public ImageView item_icon;
        public Button item_click;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_icon = (ImageView) itemView.findViewById(R.id.item_icon);
            item_click = (Button) itemView.findViewById(R.id.item_click);

        }
    }
}