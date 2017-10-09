package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.fragments.Requirement_fulfill_task;
import com.app.rbc.admin.fragments.Stock_list_product_wise;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.db.models.Categoryproduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeet on 10/6/17.
 */

public class CustomStockSiteListAdapter extends RecyclerView.Adapter<CustomStockSiteListAdapter.MyViewHolder> {

    private List<StockCategoryDetails.StockDetail> data;
    private Context context;
    private List<String> sites;
    public CustomStockSiteListAdapter(Context context, List<StockCategoryDetails.StockDetail> data, List<String> sites) {
        this.data = data;
        this.context = context;
        this.sites = sites;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_stock_site_list_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.site_name.setText(sites.get(position));

        for(int i = 0 ; i < data.size() ; i++) {
            if(data.get(i).getMsitename().equalsIgnoreCase(sites.get(position))) {
                View view = ((StockActivity)context).getLayoutInflater().inflate(R.layout.stock_details,null);
                Button stock_type = (Button) view.findViewById(R.id.stock_type);
                TextView stock_product = (TextView) view.findViewById(R.id.stock_product);
                TextView stock_quantity = (TextView) view.findViewById(R.id.stock_quantity);

                String unit = "";
                List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                        "product = ?",data.get(i).getProduct());
                if(categoryproducts.size() != 0) {
                    unit = categoryproducts.get(0).getUnit();
                }
                stock_type.setText(data.get(i).getProduct().substring(0,1));
                stock_product.setText(data.get(i).getProduct());
                stock_quantity.setText(data.get(i).getQuantity()+" "+unit);
                final int pos = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(context instanceof RequirementDetailActivity)
                        {

                            final Stock_list_product_wise info = (Stock_list_product_wise) ((RequirementDetailActivity) context).getSupportFragmentManager().findFragmentByTag(Requirement_fulfill_task.TAG);
                            info.set_site_selected(data.get(pos).getWhere(),data.get(pos).getMsitename());

                        }
                        else {

                        }

                    }
                });

                holder.stock_list_table.addView(view);
            }
        }


    }

    @Override
    public int getItemCount() {
        return sites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView site_name;
        public TableLayout stock_list_table;


        public MyViewHolder(View itemView) {
            super(itemView);
            site_name = (TextView)itemView.findViewById(R.id.site_name);
            stock_list_table = (TableLayout) itemView.findViewById(R.id.stock_list_table);


        }
    }

    public void refreshAdapter(List<String> sites, List<StockCategoryDetails.StockDetail> data) {
        this.sites.clear();
        this.data.clear();
        this.sites.addAll(sites);
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}