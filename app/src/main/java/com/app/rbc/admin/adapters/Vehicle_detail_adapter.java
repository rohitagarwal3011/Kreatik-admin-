package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.VehicleDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 17/7/17.
 */

public class Vehicle_detail_adapter extends RecyclerView.Adapter<Vehicle_detail_adapter.MyViewHolder> {



    private List<VehicleDetail> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView sourceType;
        ImageView destinationType;
        TextView transactionQuantity;
        TextView transaction_status;
        TextView source;
        TextView destination;
        TextView vehicle_number;
        TextView driver_name;
        TextView challan_number;

        public MyViewHolder(View view) {
            super(view);
            transactionQuantity = (TextView) view.findViewById(R.id.transaction_quantity);
            sourceType = (ImageView) view.findViewById(R.id.source_type);
            transaction_status = (TextView) view.findViewById(R.id.transaction_status);

            source = (TextView) view.findViewById(R.id.source);
            destinationType = (ImageView) view.findViewById(R.id.destination_type);
            destination = (TextView) view.findViewById(R.id.destination);
            vehicle_number=(TextView) view.findViewById(R.id.vehicle_number);
            driver_name =(TextView) view.findViewById(R.id.driver_name);
            challan_number=(TextView) view.findViewById(R.id.challan_link);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    final Stock_categories info = (Stock_categories) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG);
//                    info.set_product_type(getAdapterPosition());
//
//                }
//            });

        }
    }


    public Vehicle_detail_adapter(List<VehicleDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public Vehicle_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_vehicle_info, parent, false);
        return new Vehicle_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Vehicle_detail_adapter.MyViewHolder holder, final int position) {
        VehicleDetail vehicleDetail = data.get(position);
        holder.source.setText(vehicleDetail.getDetails().get(0).getSource());
        holder.destination.setText(vehicleDetail.getDetails().get(0).getDestination());
        holder.transaction_status.setText(vehicleDetail.getDetails().get(0).getStatus());
        holder.vehicle_number.setText(vehicleDetail.getDetails().get(0).getVehicleNumber());
        holder.driver_name.setText(vehicleDetail.getDetails().get(0).getDriver());
        holder.challan_number.setText(vehicleDetail.getDetails().get(0).getChallanNum());
//        holder.transactionQuantity.setText(vehicleDetail.getQuantity().toString());
//        holder..setText(vehicleDetail.getDispatchDt());
        if (vehicleDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.sourceType);
        } else if(vehicleDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Site")){

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.sourceType);
        }
        else {
            Picasso.with(context).load((R.drawable.user)).into(holder.sourceType);
        }

        if (vehicleDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.destinationType);
        } else if(vehicleDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Site")){

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.destinationType);
        }
        else {
            Picasso.with(context).load((R.drawable.user)).into(holder.destinationType);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
