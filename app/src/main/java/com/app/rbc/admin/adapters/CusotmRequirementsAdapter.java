package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.models.db.models.site_overview.Stock;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jeet on 19/9/17.
 */

public class CusotmRequirementsAdapter extends RecyclerView.Adapter<CusotmRequirementsAdapter.MyViewHolder> {

    private List<Requirement> requirements;
    private Context context;
    public CusotmRequirementsAdapter(Context context,List<Requirement> requirements) {
        this.requirements = requirements;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.requirement_card,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("Setting",position+"");
        Site site = Site.findById(Site.class,Long.valueOf(requirements.get(position).getSite()));
        if(site != null) {
            holder.requirementSite.setText(site.getName());
        }
        holder.purpose.setText(requirements.get(position).getPurpose());
        holder.requirementStatus.setText(requirements.get(position).getStatus());

        if(!(requirements.get(position).getProducts().equalsIgnoreCase(""))) {

            String[] products = requirements.get(position).getProducts().split("\\|");
            String[] quantities = requirements.get(position).getQuantities().split("\\|");
            String[] rem_quantities = requirements.get(position).getRemquantities().split("\\|");

            Log.e("Quantitites", Arrays.toString(quantities));
            Log.e("Rem Quantitites", Arrays.toString(rem_quantities));
            float quantity = 0, rem_quantity = 0;
            for(int i = 0 ; i < quantities.length ; i++) {
                quantity += Float.valueOf(quantities[i]);
                rem_quantity += Float.valueOf(rem_quantities[i]);

            }

            List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                    "product = ?", products[0]);
            if (categoryproducts.size() != 0) {
                holder.total_quantity.setText(quantity + " " + categoryproducts.get(0).getUnit());
                holder.remaining_quantity.setText(rem_quantity + " " + categoryproducts.get(0).getUnit());
            }
        }

        holder.createdOn.setText(requirements.get(position).getCreatedon());

    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView total_quantity;
        TextView requirementSite;
        TextView purpose;
        TextView requirementStatus;
        TextView createdOn;
        TextView remaining_quantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            total_quantity = (TextView) itemView.findViewById(R.id.total_quantity);
            requirementSite = (TextView) itemView.findViewById(R.id.requirement_site);
            purpose = (TextView) itemView.findViewById(R.id.purpose);
            requirementStatus = (TextView) itemView.findViewById(R.id.requirement_status);
            createdOn = (TextView) itemView.findViewById(R.id.created_on);
            remaining_quantity = (TextView) itemView.findViewById(R.id.remaining_quantity);


        }
    }

    public void refreshAdapter(List<Requirement> data) {
        this.requirements.clear();
        this.requirements.addAll(data);
        notifyDataSetChanged();
    }
}