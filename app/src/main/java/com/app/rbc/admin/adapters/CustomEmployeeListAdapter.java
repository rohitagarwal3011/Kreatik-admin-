package com.app.rbc.admin.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Employee;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by jeet on 7/9/17.
 */

public class CustomEmployeeListAdapter extends RecyclerView.Adapter<CustomEmployeeListAdapter.MyViewHolder> {

    private List<Employee> employees;
    private Context context;

    public CustomEmployeeListAdapter(Context context,List<Employee> employees) {
        this.employees = employees;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_employee_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.employee_name.setText(employees.get(position).getUserName());
        holder.employee_role.setText(employees.get(position).getRole());

        // Overlay Round
        int borderColor = context.getResources().getColor(R.color.dark_background);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(100f);
        roundingParams.setBorderColor(borderColor);
        roundingParams.setBorderWidth(1f);
        holder.employee_pic.setHierarchy(new GenericDraweeHierarchyBuilder(context.getResources())
                .setRoundingParams(roundingParams)
                .build());
        Uri imageUri = Uri.parse(employees.get(position).getPicUrl());
        holder.employee_pic.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView employee_name;
        public TextView employee_role;
        public SimpleDraweeView employee_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            employee_name = (TextView) itemView.findViewById(R.id.employee_name);
            employee_role = (TextView) itemView.findViewById(R.id.employee_role);
            employee_pic = (SimpleDraweeView) itemView.findViewById(R.id.employee_pic);
        }
    }

    public void refreshAdapter(List<Employee> data) {
        this.employees.clear();
        this.employees.addAll(data);
        notifyDataSetChanged();
    }
}