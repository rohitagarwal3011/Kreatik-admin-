package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.StockPoDetails;
import com.app.rbc.admin.models.StockProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 17/7/17.
 */

public class PO_vehicle_detail_adapter extends RecyclerView.Adapter<PO_vehicle_detail_adapter.MyViewHolder> {



    private List<StockPoDetails.VehicleDetail> data;
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


    public PO_vehicle_detail_adapter(List<StockPoDetails.VehicleDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public PO_vehicle_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_vehicle_info, parent, false);
        return new PO_vehicle_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PO_vehicle_detail_adapter.MyViewHolder holder, final int position) {
        StockPoDetails.VehicleDetail vehicleDetail = data.get(position);
        holder.source.setText(vehicleDetail.getSource());
        holder.destination.setText(vehicleDetail.getDestination());
        holder.transaction_status.setText(vehicleDetail.getStatus());
        holder.vehicle_number.setText(vehicleDetail.getVehicleNumber());
        holder.driver_name.setText(vehicleDetail.getDriver());
        holder.challan_number.setText(vehicleDetail.getChallanNum());
        holder.transactionQuantity.setText(vehicleDetail.getQuantity().toString());
        holder.transaction_status.setText(vehicleDetail.getDispatchDt());
        if (vehicleDetail.getSourceType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.sourceType);
        } else if(vehicleDetail.getSourceType().equalsIgnoreCase("Site")){

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.sourceType);
        }
        else {
            Picasso.with(context).load((R.drawable.user)).into(holder.sourceType);
        }

        if (vehicleDetail.getDestType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.destinationType);
        } else if(vehicleDetail.getDestType().equalsIgnoreCase("Site")){

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
