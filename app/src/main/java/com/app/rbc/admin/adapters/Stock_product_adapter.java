package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.fragments.Stock_products;
import com.app.rbc.admin.models.StockCategories;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Stock_product_adapter  extends RecyclerView.Adapter<com.app.rbc.admin.adapters.Stock_product_adapter.MyViewHolder> {


        private List<StockCategories.CategoryList.Product> data;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView count;
            TextView product_name;

            public MyViewHolder(View view) {
                super(view);
                product_name=(TextView)view.findViewById(R.id.product_name);
                count= (TextView) view.findViewById(R.id.count);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Stock_products info = (Stock_products) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_products.TAG);
                    info.set_product_type(data.get(getAdapterPosition()).getProduct());

                }
            });

            }
        }


        public Stock_product_adapter(List<StockCategories.CategoryList.Product> data , Context context  ) {
            this.data = data; this.context = context;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stock_product_list ,parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.count.setText(String.valueOf(position + 1) + ".");
            holder.product_name.setText(data.get(position).getProduct());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
