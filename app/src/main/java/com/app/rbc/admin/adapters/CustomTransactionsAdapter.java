package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public CustomTransactionsAdapter(Context context,List<Trans> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_transaction_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        String date = transactions.get(position).getDispatchdt();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formated = fmt.parse(date);
            SimpleDateFormat fmtout = new SimpleDateFormat("EEE, MMM dd");
            AppUtil.logger("Final date : ", fmtout.format(formated));

            holder.transactionDate.setText(fmtout.format(formated));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (transactions.get(position).getSourcetype().equalsIgnoreCase("Stock")) {
            holder.source.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getSource())).getName());
            Picasso.with(context).load((R.drawable.stock)).into(holder.sourceType);
        } else if(transactions.get(position).getSourcetype().equalsIgnoreCase("Site")){
            holder.source.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getSource())).getName());
            Picasso.with(context).load((R.drawable.site_overview)).into(holder.sourceType);
        }
        else {

            holder.source.setText(Vendor.find(Vendor.class,"vendorid = ?",transactions.get(position)
                    .getSource()).get(0).getName());
            Picasso.with(context).load((R.drawable.user)).into(holder.sourceType);
        }

        if (transactions.get(position).getDesttype().equalsIgnoreCase("Stock")) {
            holder.destination.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getDestination())).getName());
            Picasso.with(context).load((R.drawable.stock)).into(holder.destinationType);
        } else if(transactions.get(position).getDesttype().equalsIgnoreCase("Site")){
            holder.destination.setText(Site.findById(Site.class,Long.valueOf(transactions.get(position).getDestination())).getName());
            Picasso.with(context).load((R.drawable.site_overview)).into(holder.destinationType);
        }
        else {
            holder.destination.setText(Vendor.find(Vendor.class,"vendorid = ?",transactions.get(position)
            .getDestination()).get(0).getName());
            Picasso.with(context).load((R.drawable.user)).into(holder.destinationType);
        }
        float quantity = 0;
        String unit = "";
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

            holder.productTable.setVisibility(View.VISIBLE);
            holder.productTable.removeAllViews();


            for(int i = 0 ; i < products.length ; i++) {
                View tr = ((SiteOverviewActivity)context).getLayoutInflater().inflate(R.layout.custom_requirement_table_row,null);

                TextView productText = (TextView) tr.findViewById(R.id.product);
                TextView quantityText = (TextView) tr.findViewById(R.id.quantity);

                productText.setText(products[i]);
                quantityText.setText(quantities[i]+" "+unit);

                holder.productTable.addView(tr);
                quantity += Float.valueOf(quantities[i]);
            }
        }
        else {
            holder.tablelinear.setVisibility(View.GONE);
        }

        holder.transaction_quantity.setText(quantity+" "+unit);

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView sourceType;
        ImageView destinationType;
        TextView transactionDate;
        TextView source;
        TextView destination;
        TableLayout productTable;
        LinearLayout tablelinear;
        TextView transaction_quantity;


        public MyViewHolder(View itemView) {
            super(itemView);
            sourceType = (ImageView) itemView.findViewById(R.id.source_type);
            transactionDate = (TextView) itemView.findViewById(R.id.transaction_date);

            source = (TextView) itemView.findViewById(R.id.source);
            destinationType = (ImageView) itemView.findViewById(R.id.destination_type);
            destination = (TextView) itemView.findViewById(R.id.destination);
            productTable = (TableLayout) itemView.findViewById(R.id.product_table);
            tablelinear = (LinearLayout) itemView.findViewById(R.id.tablelinear);
            transaction_quantity = (TextView) itemView.findViewById(R.id.transaction_quantity);


        }
    }

    public void refreshAdapter(List<Trans> data) {
        this.transactions.clear();
        this.transactions.addAll(data);
        notifyDataSetChanged();
    }
}