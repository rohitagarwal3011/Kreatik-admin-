package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.StockProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Stock_detail_adapter  extends RecyclerView.Adapter<Stock_detail_adapter.MyViewHolder> {


    private List<StockProductDetails.StockDetail> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView stock_type;
        TextView stock_location;
        TextView stock_quantity;

        public MyViewHolder(View view) {
            super(view);
            stock_location=(TextView)view.findViewById(R.id.stock_location);
            stock_type= (ImageView)view.findViewById(R.id.stock_type);
            stock_quantity = (TextView)view.findViewById(R.id.stock_quantity);

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


    public Stock_detail_adapter(List<StockProductDetails.StockDetail> data , Context context  ) {
        this.data = data; this.context = context;
    }


    @Override
    public Stock_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_details ,parent, false);
        return new Stock_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Stock_detail_adapter.MyViewHolder holder, final int position) {
        holder.stock_location.setText(data.get(position).getWhere());
        holder.stock_quantity.setText(data.get(position).getQuantity().toString());
        if(data.get(position).getMstock_type().equalsIgnoreCase("Stock"))
        {
            Picasso.with(context).load((R.drawable.stock)).into(holder.stock_type);
        }
        else {

            Picasso.with(context).load((R.drawable.site_overview)).into(holder.stock_type);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
