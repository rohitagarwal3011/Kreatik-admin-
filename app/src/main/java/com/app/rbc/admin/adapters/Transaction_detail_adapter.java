package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Transaction_detail_adapter extends RecyclerView.Adapter<Transaction_detail_adapter.MyViewHolder> {



    private List<StockCategoryDetails.TransactionDetail> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView sourceType;
        ImageView destinationType;
        TextView transactionDate;
        TextView source;
        TextView destination;

        public MyViewHolder(View view) {
            super(view);
            sourceType = (ImageView) view.findViewById(R.id.source_type);
            transactionDate = (TextView) view.findViewById(R.id.transaction_date);

            source = (TextView) view.findViewById(R.id.source);
            destinationType = (ImageView) view.findViewById(R.id.destination_type);
            destination = (TextView) view.findViewById(R.id.destination);
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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
