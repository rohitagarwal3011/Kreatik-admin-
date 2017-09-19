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
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.models.db.models.site_overview.Stock;
import com.squareup.picasso.Picasso;

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
        holder.requirementTitle.setText(requirements.get(position).getTitle());
        holder.requirementSite.setText(requirements.get(position).getSite());
        holder.purpose.setText(requirements.get(position).getPurpose());
        holder.requirementStatus.setText(requirements.get(position).getStatus());
        holder.createdOn.setText(requirements.get(position).getCreatedon());

    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView requirementTitle;
        TextView requirementSite;
        TextView purpose;
        TextView requirementStatus;
        TextView createdOn;

        public MyViewHolder(View itemView) {
            super(itemView);
            requirementTitle = (TextView) itemView.findViewById(R.id.requirement_title);
            requirementSite = (TextView) itemView.findViewById(R.id.requirement_site);
            purpose = (TextView) itemView.findViewById(R.id.purpose);
            requirementStatus = (TextView) itemView.findViewById(R.id.requirement_status);
            createdOn = (TextView) itemView.findViewById(R.id.created_on);

        }
    }

    public void refreshAdapter(List<Requirement> data) {
        this.requirements.clear();
        this.requirements.addAll(data);
        notifyDataSetChanged();
    }
}