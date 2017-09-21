package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Transaction_detail_adapter extends RecyclerView.Adapter<Transaction_detail_adapter.MyViewHolder> {



    private List<StockCategoryDetails.TransactionDetail> data;
    private Context context;

    int count;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView sourceType;
        ImageView destinationType;
        TextView transactionDate;
        TextView source;
        TextView destination;
        TableLayout productTable;
        TextView transaction_quantity;

        public MyViewHolder(View view) {
            super(view);
            sourceType = (ImageView) view.findViewById(R.id.source_type);
            transactionDate = (TextView) view.findViewById(R.id.transaction_date);

            source = (TextView) view.findViewById(R.id.source);
            destinationType = (ImageView) view.findViewById(R.id.destination_type);
            destination = (TextView) view.findViewById(R.id.destination);
            productTable = (TableLayout) view.findViewById(R.id.product_table);
            transaction_quantity = (TextView) view.findViewById(R.id.transaction_quantity);
            count=1;

        }
    }


    public Transaction_detail_adapter(List<StockCategoryDetails.TransactionDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_transaction_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        StockCategoryDetails.TransactionDetail transactionDetail = data.get(position);


        holder.source.setText(transactionDetail.getDetails().get(0).getSource());
        holder.destination.setText(transactionDetail.getDetails().get(0).getDestination());
        holder.transactionDate.setText(transactionDetail.getDetails().get(0).getDispatchDt());
        if (transactionDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.sourceType);
        } else if(transactionDetail.getDetails().get(0).getSourceType().equalsIgnoreCase("Site")){

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.sourceType);
        }
        else {
            Picasso.with(context).load((R.drawable.user)).into(holder.sourceType);
        }

        if (transactionDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Stock")) {
            Picasso.with(context).load((R.drawable.stock)).into(holder.destinationType);
        } else if(transactionDetail.getDetails().get(0).getDestType().equalsIgnoreCase("Site")){

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.destinationType);
        }
        else {
            Picasso.with(context).load((R.drawable.user)).into(holder.destinationType);
        }

        if(!transactionDetail.getProducts().isEmpty()) {

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
            count =1;

            int totalQuantity = 0;
            String unit = "";
            if(data.get(position).getProducts().size() != 0) {
                List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                        "product = ?",data.get(position).getProducts().get(0).getProduct());
                if(categoryproducts.size() != 0) {
                    unit = categoryproducts.get(0).getUnit();
                }
            }
            for (int i = 0; i < data.get(position).getProducts().size(); i++) {
                StockCategoryDetails.TransactionDetail.Product products = data.get(position).getProducts().get(i);
                String product = products.getProduct();
                String quantity = products.getQuantity().toString()+" "+unit;
                totalQuantity += Float.valueOf(quantity);
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
            holder.transaction_quantity.setText(totalQuantity+" "+unit);
        }
        else {
            holder.productTable.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
