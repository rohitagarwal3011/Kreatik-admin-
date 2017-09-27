package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;

import java.util.List;



public class CustomCategoryProductAdapter extends RecyclerView.Adapter<CustomCategoryProductAdapter.MyViewHolder> {

    private List<String> categories;
    private Context context;
    public CustomCategoryProductAdapter(Context context,List<String> categories) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_category_product_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.category_title.setText(categories.get(position));
        holder.category_icon.setText(categories.get(position).substring(0,1));

        holder.category_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IndentRegisterActivity)context).setFragment(9,categories.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView category_title;
        public Button category_click;
        public Button category_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_click = (Button) itemView.findViewById(R.id.category_click);
            category_icon = (Button) itemView.findViewById(R.id.category_icon);

        }
    }

    public void refreshAdapter(List<String> data) {
        this.categories.clear();
        this.categories.addAll(data);
        notifyDataSetChanged();
    }
}