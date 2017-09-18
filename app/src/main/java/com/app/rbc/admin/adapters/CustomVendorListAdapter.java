package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Vendor;

import java.util.List;


public class CustomVendorListAdapter  extends RecyclerView.Adapter<CustomVendorListAdapter.MyViewHolder> {

    private List<Vendor> vendors;
    private Context context;

    public CustomVendorListAdapter(Context context,List<Vendor> vendors) {
        this.vendors = vendors;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_vendor_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.vendor_name.setText(vendors.get(position).getName());
        holder.vendor_add.setText(vendors.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return vendors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView vendor_name;
        public TextView vendor_add;

        public MyViewHolder(View itemView) {
            super(itemView);
            vendor_name = (TextView) itemView.findViewById(R.id.vendor_name);
            vendor_add = (TextView) itemView.findViewById(R.id.vendor_add);
        }
    }

    public void refreshAdapter(List<Vendor> data) {
        this.vendors.clear();
        this.vendors.addAll(data);
        notifyDataSetChanged();
    }
}