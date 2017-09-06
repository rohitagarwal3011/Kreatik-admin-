package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AddVehicleActivity;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.fragments.Cat_Des_Requirement_List;
import com.app.rbc.admin.fragments.Dispatch_Vehicle;
import com.app.rbc.admin.fragments.Vendor_list;
import com.app.rbc.admin.models.CatDesRequirementList;
import com.app.rbc.admin.models.RequirementList;

import java.util.List;

import butterknife.BindView;

/**
 * Created by rohit on 27/8/17.
 */

public class Requirement_list_adapter extends RecyclerView.Adapter<Requirement_list_adapter.MyViewHolder> {




    private List<RequirementList.ReqList> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView requirementTitle;

        TextView requirementSite;

        TextView purpose;

        TextView requirementStatus;

        TextView createdOn;

        public MyViewHolder(View view) {
            super(view);
            requirementTitle = (TextView) view.findViewById(R.id.requirement_title);
            requirementSite = (TextView) view.findViewById(R.id.requirement_site);

            purpose = (TextView) view.findViewById(R.id.purpose);
            requirementStatus = (TextView) view.findViewById(R.id.requirement_status);
            createdOn = (TextView) view.findViewById(R.id.created_on);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    final Vendor_list info = (Vendor_list) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_add_po_details.TAG);
//                    info.set_vendor_id(data.get(getAdapterPosition()).getVendorId());

                    if(context instanceof RequirementActivity) {
                        ((RequirementActivity) context).hide_tablayout();
                        //  Fragment fragment= ((StockActivity) context).getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + ((StockActivity)context).mViewPager.getCurrentItem());
                        ((RequirementActivity) context).mSectionsPagerAdapter.onclick_method(data.get(getAdapterPosition()).getRqId());
                    }
                    else if (context instanceof AddVehicleActivity)
                    {
                        final Cat_Des_Requirement_List info = (Cat_Des_Requirement_List) ((AddVehicleActivity) context).getSupportFragmentManager().findFragmentByTag(Dispatch_Vehicle.TAG);
                      info.set_requirement_id(data.get(getAdapterPosition()).getRqId(),getAdapterPosition());

                    }
                }
            });

        }
    }


    public Requirement_list_adapter(List<RequirementList.ReqList> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requirement_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        RequirementList.ReqList.Detail requirement = data.get(position).getDetails().get(0);

        holder.requirementTitle.setText(requirement.getTitle());
        holder.requirementSite.setText(requirement.getSite());
        holder.purpose.setText(requirement.getPurpose());
        holder.requirementStatus.setText(requirement.getStatus());
        holder.createdOn.setText(requirement.getCreatedOn());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

