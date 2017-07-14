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
import android.widget.TextView;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AttendanceActivity;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.fragments.Attendance_all;
import com.app.rbc.admin.fragments.Attendance_emp_list;
import com.app.rbc.admin.fragments.Task_create;
import com.app.rbc.admin.models.Employee;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class Employee_list_adapter extends RecyclerView.Adapter<Employee_list_adapter.MyViewHolder> {


    private List<Employee.Data> data;
    private Context context;
    private String fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView profile_pic;
        public TextView employee_name;
        public TextView leave_count;
        public TextView present_count;
        public TextView hd_count;
        public TextView role;

        public MyViewHolder(View view) {
            super(view);
            profile_pic=(SimpleDraweeView) view.findViewById(R.id.profile_pic);
            employee_name=(TextView)view.findViewById(R.id.employee_name);
            leave_count=(TextView)view.findViewById(R.id.leave_count);
            present_count=(TextView)view.findViewById(R.id.present_count);
            hd_count=(TextView)view.findViewById(R.id.hd_count);
            role=(TextView)view.findViewById(R.id.role);

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


    public Employee_list_adapter(List<Employee.Data> data , Context context , String fragment ) {
        this.data = data; this.context = context; this.fragment = fragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_list ,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.employee_name.setText(data.get(position).getUserName());

        int color = context.getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        holder.profile_pic.getHierarchy().setRoundingParams(roundingParams);


        holder.profile_pic.setImageURI(Uri.parse(data.get(position).getMpic_url()));

        holder.role.setText(data.get(position).getRole());
        if(fragment.equalsIgnoreCase("MonthlyList"))
        {
            holder.leave_count.setVisibility(View.VISIBLE);
            if(Attendance_all.month_leave_grid.containsKey(data.get(position).getUserId()))
            holder.leave_count.setText("\u25CF Absent : "+Attendance_all.month_leave_grid.get(data.get(position).getUserId())+" days");
            else
                holder.leave_count.setText("\u25CF"+ " No leaves taken");


            holder.present_count.setVisibility(View.VISIBLE);
            if(Attendance_all.month_present_count.containsKey(data.get(position).getUserId()))
                holder.present_count.setText("\u25CF Present : "+Attendance_all.month_present_count.get(data.get(position).getUserId())+" days");
            else
                holder.present_count.setText("\u25CF"+ "Present : 0 days");


            holder.hd_count.setVisibility(View.VISIBLE);
            if(Attendance_all.month_hd_count.containsKey(data.get(position).getUserId()))
                holder.hd_count.setText("\u25CF Half day : "+Attendance_all.month_hd_count.get(data.get(position).getUserId())+" days");
            else
                holder.hd_count.setText("\u25CF"+ " No half days");

        }
        else {
            holder.leave_count.setVisibility(View.GONE);
            holder.present_count.setVisibility(View.GONE);
            holder.hd_count.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment.equalsIgnoreCase(Task_create.TAG)) {
                    final Task_create info = (Task_create) ((TaskActivity) context).getSupportFragmentManager().findFragmentByTag(Task_create.TAG);
                    info.set_employee_id(data.get(position).getUserId());
                }
                else if(fragment.equalsIgnoreCase(Attendance_all.TAG)||fragment.equalsIgnoreCase("MonthlyList")){
                    final Attendance_all info = (Attendance_all) ((AttendanceActivity) context).getSupportFragmentManager().findFragmentByTag(Attendance_all.TAG);
                    info.set_employee_id(data.get(position).getUserId(),data.get(position).getUserName());
                }
            }
        });
        //Picasso.with(context).load(data.get(position).getMpic_url()).into(holder.profile_pic);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
