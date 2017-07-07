package com.app.rbc.admin.adapters;

/**
 * Created by rohit on 14/6/17.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.app.rbc.admin.R;
import com.app.rbc.admin.models.Employee;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

public class Attendance_mark_adapter extends RecyclerView.Adapter<Attendance_mark_adapter.MyViewHolder> {


    private List<Employee.Data> data;
    private Context context;
    private String fragment;
    public static HashMap<String,String> attendance_grid= new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView profile_pic;
        public TextView employee_name;
        public CheckBox absent;
        public CheckBox half_day;

        public MyViewHolder(final View view) {
            super(view);
            profile_pic=(SimpleDraweeView) view.findViewById(R.id.profile_pic);
            employee_name=(TextView)view.findViewById(R.id.employee_name);
            absent=(CheckBox)view.findViewById(R.id.absent);
            half_day=(CheckBox)view.findViewById(R.id.half_day);


            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(half_day.isChecked())
                    {
                        half_day.setChecked(false);
                    }
                    if(attendance_grid.containsKey(data.get(getAdapterPosition()).getUserId()))
                    {
                        attendance_grid.remove(data.get(getAdapterPosition()).getUserId());

                    }
                    if(absent.isChecked())
                    attendance_grid.put(data.get(getAdapterPosition()).getUserId(),"Absent");


                }
            });
            half_day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(absent.isChecked())
                    {
                        absent.setChecked(false);
                    }
                    if(attendance_grid.containsKey(data.get(getAdapterPosition()).getUserId()))
                    {
                        attendance_grid.remove(data.get(getAdapterPosition()).getUserId());

                    }
                    if(half_day.isChecked())
                    attendance_grid.put(data.get(getAdapterPosition()).getUserId(),"Half day");
                }
            });


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    task_create.set_employee_id(data.get(getAdapterPosition()).getUserId());
//
//                }
//            });

        }
    }


    public Attendance_mark_adapter(List<Employee.Data> data , Context context , String fragment ) {
        this.data = data; this.context = context; this.fragment = fragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_mark_list ,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(attendance_grid.containsKey(data.get(position).getUserId()))
        {
            if(attendance_grid.get(data.get(position).getUserId()).equalsIgnoreCase("Absent"))
            {
                holder.absent.setChecked(true);
                holder.half_day.setChecked(false);
            }
            else {
                holder.absent.setChecked(false);
                holder.half_day.setChecked(true);
            }
        }
        holder.employee_name.setText(data.get(position).getUserName());

        int color = context.getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        holder.profile_pic.getHierarchy().setRoundingParams(roundingParams);


        holder.profile_pic.setImageURI(Uri.parse(data.get(position).getMpic_url()));

        //Picasso.with(context).load(data.get(position).getMpic_url()).into(holder.profile_pic);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
