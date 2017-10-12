package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;

import com.app.rbc.admin.activities.SiteOverviewActivity;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.Vendor;
import com.app.rbc.admin.models.db.models.site_overview.Trans;
import com.app.rbc.admin.utils.AppUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jeet on 19/9/17.
 */

public class CustomTransactionsAdapter extends RecyclerView.Adapter<CustomTransactionsAdapter.MyViewHolder> {

    private List<Trans> transactions;
    private Context context;
    ArrayList posters= new ArrayList(1);

    public CustomTransactionsAdapter(Context context,List<Trans> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stock_vehicle_info,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        DateTime dateTime = new DateTime(transactions.get(position).getDispatchdt());
        holder.transactionDate.setText(dateTime.toString("MMM dd, yyyy"));


        if (transactions.get(position).getSourcetype().equalsIgnoreCase("Stock")) {
            holder.source.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getSource())).getName());
        } else if(transactions.get(position).getSourcetype().equalsIgnoreCase("Site")){
            holder.source.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getSource())).getName());
        }
        else {

            holder.source.setText(Vendor.find(Vendor.class,"vendorid = ?",transactions.get(position)
                    .getSource()).get(0).getName());
        }

        if (transactions.get(position).getDesttype().equalsIgnoreCase("Stock")) {
            holder.destination.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getDestination())).getName());
        } else if(transactions.get(position).getDesttype().equalsIgnoreCase("Site")){
            holder.destination.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getDestination())).getName());
        }
        else {
            holder.destination.setText(Vendor.find(Vendor.class,"vendorid = ?",transactions.get(position)
            .getDestination()).get(0).getName());
        }
        String unit = "";
        holder.productTable.removeAllViews();

        if(transactions.get(position).getProducts() != null && !(transactions.get(position).getProducts().equals(""))) {
            String[] products = transactions.get(position).getProducts().split("\\|");
            String[] quantities = transactions.get(position).getQuantites().split("\\|");

                List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                        "product = ?", products[0]+"");
            if(categoryproducts.size() != 0) {
                unit = categoryproducts.get(0).getUnit();
            }
            Log.e("Products", Arrays.toString(products));
            Log.e("Quantities",Arrays.toString(quantities));

            holder.tableLinear.setVisibility(View.VISIBLE);


            for(int i = 0 ; i < products.length ; i++) {
                TableRow tr = new TableRow(context);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, (int) context.getResources().getDimension(R.dimen._5sdp), 0, (int) context.getResources().getDimensionPixelSize(R.dimen._5sdp));
                tr.setLayoutParams(layoutParams);
                tr.setPadding((int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp));

                TextView tv = new TextView(context);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv.setGravity(Gravity.LEFT);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(products[i]);

                tr.addView(tv, 0);

                TextView tv1 = new TextView(context);
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv1.setGravity(Gravity.LEFT);
                tv1.setTextColor(Color.parseColor("#000000"));
                tv1.setText(Math.round(Float.valueOf(quantities[i]))+" "+unit);

                tr.addView(tv1, 1);


                holder.productTable.addView(tr);
            }
        }
        else {
            holder.tableLinear.setVisibility(View.GONE);
        }

        holder.driver_name.setText("Driver : "+transactions.get(position).getDriver());
        holder.challan_link.setText("Challan No.\n"+transactions.get(position).getChallannum());
        holder.vehicle_number.setText(transactions.get(position).getVehiclenumber());
        if(transactions.get(position).getStatus().equalsIgnoreCase("Received")) {

            holder.images_layout.setVisibility(View.VISIBLE);
            String[] urls = transactions.get(position).getChallanimg().split("\\|");

            final Uri challanUrl = Uri.parse(urls[0]);
            final Uri invoiceUrl = Uri.parse(urls[1]);
            final Uri onreceiveUrl = Uri.parse(urls[2]);
            final Uri unloadedUrl = Uri.parse(urls[3]);

            holder.challan_img.setImageURI(challanUrl);
            holder.invoice_img.setImageURI(invoiceUrl);
            holder.onreceive_img.setImageURI(onreceiveUrl);
            holder.unloaded_img.setImageURI(unloadedUrl);

            holder.challan_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(challanUrl);

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });


            holder.invoice_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(invoiceUrl);

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });

            holder.onreceive_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(onreceiveUrl);

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });

            holder.unloaded_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(unloadedUrl);

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });
        }
        else {
            holder.images_layout.setVisibility(View.GONE);
        }
        holder.transaction_status.setText(transactions.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView source;
        TextView destination;
        TextView transactionDate;
        TextView vehicle_number,driver_name,challan_link;
        SimpleDraweeView challan_img,invoice_img,
                onreceive_img,unloaded_img;
        LinearLayout tableLinear;
        TextView transaction_status;

        TableLayout productTable;
        LinearLayout images_layout;

        public MyViewHolder(View view) {
            super(view);
            transactionDate = (TextView) view.findViewById(R.id.transaction_date);
            source = (TextView) view.findViewById(R.id.source);
            destination = (TextView) view.findViewById(R.id.destination);
            productTable = (TableLayout) view.findViewById(R.id.product_table);
            vehicle_number = (TextView) view.findViewById(R.id.vehicle_number);
            driver_name = (TextView) view.findViewById(R.id.driver_name);
            challan_link = (TextView) view.findViewById(R.id.challan_link);
            challan_img = (SimpleDraweeView) view.findViewById(R.id.challan_img);
            invoice_img = (SimpleDraweeView) view.findViewById(R.id.invoice_img);
            onreceive_img = (SimpleDraweeView) view.findViewById(R.id.onrecieve_img);
            unloaded_img = (SimpleDraweeView) view.findViewById(R.id.unloaded_img);
            tableLinear = (LinearLayout) view.findViewById(R.id.tableLinear);
            transaction_status = (TextView) view.findViewById(R.id.transaction_status);
            images_layout = (LinearLayout) itemView.findViewById(R.id.images_layout);


        }
    }

    public void refreshAdapter(List<Trans> data) {
        this.transactions.clear();
        this.transactions.addAll(data);
        notifyDataSetChanged();
    }
}