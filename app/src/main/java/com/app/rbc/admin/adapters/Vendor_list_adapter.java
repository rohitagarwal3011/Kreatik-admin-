package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.fragments.Stock_add_po_details;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.fragments.Vendor_list;
import com.app.rbc.admin.models.Vendors;

import java.util.List;

/**
 * Created by rohit on 24/8/17.
 */

public class Vendor_list_adapter extends RecyclerView.Adapter<com.app.rbc.admin.adapters.Vendor_list_adapter.MyViewHolder> {


    private List<Vendors.VendorList> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vendor_name;
        TextView vendor_address;

        public MyViewHolder(View view) {
            super(view);
            vendor_name=(TextView)view.findViewById(R.id.vendor_name);
            vendor_address= (TextView) view.findViewById(R.id.vendor_address);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Vendor_list info = (Vendor_list) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_add_po_details.TAG);
                    info.set_vendor_id(data.get(getAdapterPosition()).getVendorId());

                }
            });

        }
    }


    public Vendor_list_adapter(List<Vendors.VendorList> data , Context context  ) {
        this.data = data; this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_list ,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.vendor_name.setText(data.get(position).getVendorName());
        holder.vendor_address.setText(data.get(position).getVendorAdd());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

