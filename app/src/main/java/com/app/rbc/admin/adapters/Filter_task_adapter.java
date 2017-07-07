package com.app.rbc.admin.adapters;

/**
 * Created by rohit on 25/4/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.app.rbc.admin.R;

import java.util.ArrayList;

public class Filter_task_adapter extends RecyclerView.Adapter<Filter_task_adapter.MyViewHolder> {


    private ArrayList data;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button filter;

        public MyViewHolder(View view) {
            super(view);
            filter=(Button) view.findViewById(R.id.filter_task_button);

        }
    }


    public Filter_task_adapter(ArrayList data) {
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filters ,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.filter.setText(data.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


