package com.app.rbc.admin.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Site;

import java.util.List;

/**
 * Created by jeet on 7/9/17.
 */

public class CustomSiteListAdapter extends RecyclerView.Adapter<CustomSiteListAdapter.MyViewHolder> {

    private List<Site> sites;
    private Context context;

    public CustomSiteListAdapter(Context context,List<Site> sites) {
        this.sites = sites;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_site_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.site_name.setText(sites.get(position).getName());
        holder.site_type.setText(sites.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return sites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView site_name;
        public TextView site_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            site_name = (TextView) itemView.findViewById(R.id.site_name);
            site_type = (TextView) itemView.findViewById(R.id.site_type);
        }
    }

    public void refreshAdapter(List<Site> data) {
        this.sites.clear();
        this.sites.addAll(data);
        notifyDataSetChanged();
    }
}