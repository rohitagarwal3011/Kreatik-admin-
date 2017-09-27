package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.fragments.Requirement_fulfill_task;
import com.app.rbc.admin.fragments.Stock_list_product_wise;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Stock_detail_adapter  extends RecyclerView.Adapter<Stock_detail_adapter.MyViewHolder> {


    private List<StockCategoryDetails.StockDetail> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView stock_type;
        TextView stock_location;
        TextView stock_quantity;
        TextView stock_product;

        public MyViewHolder(View view) {
            super(view);
            stock_location=(TextView)view.findViewById(R.id.stock_location);
            stock_type= (ImageView)view.findViewById(R.id.stock_type);
            stock_quantity = (TextView)view.findViewById(R.id.stock_quantity);
            stock_product = (TextView)view.findViewById(R.id.stock_product);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(context instanceof RequirementDetailActivity)
                    {

                        final Stock_list_product_wise info = (Stock_list_product_wise) ((RequirementDetailActivity) context).getSupportFragmentManager().findFragmentByTag(Requirement_fulfill_task.TAG);
                        info.set_site_selected(data.get(getAdapterPosition()).getWhere(),data.get(getAdapterPosition()).getMsitename());

                    }
                    else
                    {

                    }
//                    final Stock_categories info = (Stock_categories) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG);
//                    info.set_product_type(getAdapterPosition());

                }
            });

        }
    }


    public Stock_detail_adapter(List<StockCategoryDetails.StockDetail> data , Context context  ) {
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
        holder.stock_location.setText(data.get(position).getMsitename());
        holder.stock_product.setText(data.get(position).getProduct().toString());



        List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                "product = ?",data.get(position).getProduct().toString());

        if(categoryproducts.size() != 0) {
            holder.stock_quantity.setText(data.get(position).getQuantity().toString()+" "+
            categoryproducts.get(0).getUnit());
        }

        if(data.get(position).getMstock_type().equalsIgnoreCase("Stock"))
        {
            Picasso.with(context).load((R.drawable.stock)).into(holder.stock_type);
        }
        else {

            Picasso.with(context).load((R.drawable.crane)).into(holder.stock_type);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
