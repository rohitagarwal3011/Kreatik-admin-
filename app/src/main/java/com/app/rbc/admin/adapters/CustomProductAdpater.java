package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Categoryproduct;

import java.util.List;

/**
 * Created by jeet on 6/9/17.
 */

public class CustomProductAdpater extends RecyclerView.Adapter<CustomProductAdpater.MyViewHolder> {

    private List<Categoryproduct> categoryproducts;
    private Context context;
    public CustomProductAdpater(Context context,List<Categoryproduct> categoryproducts) {
        this.categoryproducts = categoryproducts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_product_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.product_title.setText(categoryproducts.get(position).getProduct());

    }

    @Override
    public int getItemCount() {
        return categoryproducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView product_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_title = (TextView) itemView.findViewById(R.id.product_title);

        }
    }

    public void refreshAdapter(List<Categoryproduct> data) {
        this.categoryproducts.clear();
        this.categoryproducts.addAll(data);
        notifyDataSetChanged();
    }
}