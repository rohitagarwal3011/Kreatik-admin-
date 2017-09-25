package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.utils.AppUtil;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 17/7/17.
 */

public class Vehicle_detail_adapter extends RecyclerView.Adapter<Vehicle_detail_adapter.MyViewHolder> {


    private List<VehicleDetail> data;
    private Context context;
    int count;
    ArrayList posters= new ArrayList(1);


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView sourceType;
        ImageView destinationType;
        TextView transactionDate;
        TextView transaction_status;
        TextView source;
        TextView destination;
        TextView vehicle_number;
        TextView driver_name;
        TextView challan_number;
        TableRow mRowheading;
        TableLayout productTable;
        SimpleDraweeView mImgChallan;
        SimpleDraweeView mImgInvoice;
        SimpleDraweeView mImgOnrecieve;
        SimpleDraweeView mImgUnloaded;


        public MyViewHolder(View view) {
            super(view);
            transactionDate = (TextView) view.findViewById(R.id.transaction_date);
            sourceType = (ImageView) view.findViewById(R.id.source_type);
            transaction_status = (TextView) view.findViewById(R.id.transaction_status);

            source = (TextView) view.findViewById(R.id.source);
            destinationType = (ImageView) view.findViewById(R.id.destination_type);
            destination = (TextView) view.findViewById(R.id.destination);
            vehicle_number = (TextView) view.findViewById(R.id.vehicle_number);
            driver_name = (TextView) view.findViewById(R.id.driver_name);
            challan_number = (TextView) view.findViewById(R.id.challan_link);
            mRowheading = (TableRow) view.findViewById(R.id.rowheading);
            productTable = (TableLayout) view.findViewById(R.id.product_table);
            count = 1;
            mImgChallan = (SimpleDraweeView) itemView.findViewById(R.id.challan_img);
            mImgInvoice = (SimpleDraweeView) itemView.findViewById(R.id.invoice_img);
            mImgOnrecieve = (SimpleDraweeView) itemView.findViewById(R.id.onrecieve_img);
            mImgUnloaded = (SimpleDraweeView) itemView.findViewById(R.id.unloaded_img);

//            final String [] images = data.get(getAdapterPosition()).getDetails().get(0).getChallanImg().split("\\|");


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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_vehicle_info, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        VehicleDetail vehicleDetail = data.get(position);


            if(vehicleDetail.getSiteDetails().get(0).getId()==Integer.parseInt(vehicleDetail.getDetails().get(0).getSource()))
            {
                holder.source.setText(vehicleDetail.getSiteDetails().get(0).getName());
                holder.destination.setText(vehicleDetail.getSiteDetails().get(1).getName());
            }
            else {
                holder.source.setText(vehicleDetail.getSiteDetails().get(1).getName());
                holder.destination.setText(vehicleDetail.getSiteDetails().get(0).getName());
            }


        holder.transaction_status.setText(vehicleDetail.getDetails().get(0).getStatus());
        holder.vehicle_number.setText(vehicleDetail.getDetails().get(0).getVehicleNumber());
        holder.driver_name.setText("Driver : " + vehicleDetail.getDetails().get(0).getDriver());
        holder.challan_number.setText("Challan No. \n"+vehicleDetail.getDetails().get(0).getChallanNum());
        holder.transactionDate.setText(vehicleDetail.getDetails().get(0).getDispatchDt());
//        holder.transactionQuantity.setText(vehicleDetail.getQuantity().toString());
//        holder..setText(vehicleDetail.getDispatchDt());
//        if (vehicleDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Stock")) {
//            Picasso.with(context).load((R.drawable.stock)).into(holder.sourceType);
//        } else if (vehicleDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Site")) {
//
//            Picasso.with(context).load((R.drawable.site_overview)).into(holder.sourceType);
//        } else {
//            Picasso.with(context).load((R.drawable.user)).into(holder.sourceType);
//        }
//
//        if (vehicleDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Stock")) {
//            Picasso.with(context).load((R.drawable.stock)).into(holder.destinationType);
//        } else if (vehicleDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Site")) {
//
//            Picasso.with(context).load((R.drawable.site_overview)).into(holder.destinationType);
//        } else {
//            Picasso.with(context).load((R.drawable.user)).into(holder.destinationType);
//        }

        if (!vehicleDetail.getProducts().isEmpty()) {

            holder.productTable.setVisibility(View.VISIBLE);
            holder.productTable.removeAllViews();
            TableRow tr0 = new TableRow(context);
            TableRow.LayoutParams layoutParams0 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            layoutParams0.setMargins(0, (int) context.getResources().getDimension(R.dimen._5sdp), 0, (int) context.getResources().getDimensionPixelSize(R.dimen._5sdp));
            tr0.setLayoutParams(layoutParams0);
            tr0.setPadding((int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp));

            TextView tv0 = new TextView(context);
            tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            tv0.setGravity(Gravity.CENTER_HORIZONTAL);
            tv0.setTextColor(Color.parseColor("#000000"));
            tv0.setText("Product");

            tr0.addView(tv0, 0);

            TextView tv01 = new TextView(context);
            tv01.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            tv01.setGravity(Gravity.CENTER_HORIZONTAL);
            tv01.setTextColor(Color.parseColor("#000000"));
            tv01.setText("Quantity");

            tr0.addView(tv01, 1);

            holder.productTable.addView(tr0, 0);
            count = 1;


            for (int i = 0; i < data.get(position).getProducts().size(); i++) {
                VehicleDetail.Product products = data.get(position).getProducts().get(i);

                String product = products.getProduct();
                String quantity = products.getQuantity().toString();
                TableRow tr = new TableRow(context);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, (int) context.getResources().getDimension(R.dimen._5sdp), 0, (int) context.getResources().getDimensionPixelSize(R.dimen._5sdp));
                tr.setLayoutParams(layoutParams);
                tr.setPadding((int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp));

                TextView tv = new TextView(context);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(product);

                tr.addView(tv, 0);

                TextView tv1 = new TextView(context);
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                tv1.setTextColor(Color.parseColor("#000000"));
                tv1.setText(quantity);

                tr.addView(tv1, 1);

                holder.productTable.addView(tr, count);
                count++;
                // AppUtil.logger("Transaction Detail Adapter : ", String.valueOf(count));
            }
        } else {
            holder.productTable.setVisibility(View.GONE);
        }

//        int color = context.getResources().getColor(R.color.black_overlay);
//        RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
//        roundingParams.setBorder(color, 1.0f);
//        roundingParams.setRoundAsCircle(true);
//
//
// holder.mImgChallan.getHierarchy().setRoundingParams(roundingParams);


        if(vehicleDetail.getDetails().get(0).getStatus().equalsIgnoreCase("Received")) {
            final String[] images = vehicleDetail.getDetails().get(0).getChallanImg().split("\\|");
            AppUtil.logger("Image links : ", images[0] + "--" + images[1] + "--" + images[2] + "--" + images[3]);

            holder.mImgChallan.setImageURI(images[0]);
            holder.mImgInvoice.setImageURI(images[1]);
            holder.mImgOnrecieve.setImageURI(images[2]);
            holder.mImgUnloaded.setImageURI(images[3]);

            holder.mImgChallan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(Uri.parse(images[0]));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });


            holder.mImgInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(Uri.parse(images[1]));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });

            holder.mImgUnloaded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(Uri.parse(images[2]));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });

            holder.mImgOnrecieve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(Uri.parse(images[3]));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });
        }






    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
