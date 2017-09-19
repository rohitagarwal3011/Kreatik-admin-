package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AddVehicleActivity;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.fragments.Cat_Des_Requirement_List;
import com.app.rbc.admin.fragments.Dispatch_Vehicle;
import com.app.rbc.admin.models.RequirementList;
import com.app.rbc.admin.utils.AppUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rohit on 27/8/17.
 */

public class Requirement_list_adapter extends RecyclerView.Adapter<Requirement_list_adapter.MyViewHolder> {


    private List<RequirementList.ReqList> data;
    private Context context;


    private void initView() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView requirementSite;

        TextView purpose;

        TextView requirementStatus;

        TextView createdOn;
         TextView mSiteRequirement;
         TextView mQuantityTotal;
         TextView mQuantityRemaining;

        public MyViewHolder(View view) {
            super(view);
            requirementSite = (TextView) view.findViewById(R.id.requirement_site);

            purpose = (TextView) view.findViewById(R.id.purpose);
            requirementStatus = (TextView) view.findViewById(R.id.requirement_status);
            createdOn = (TextView) view.findViewById(R.id.created_on);
            mSiteRequirement = (TextView) view.findViewById(R.id.requirement_site);
            mQuantityTotal = (TextView) view.findViewById(R.id.total_quantity);
            mQuantityRemaining = (TextView) view.findViewById(R.id.remaining_quantity);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    final Vendor_list info = (Vendor_list) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(Stock_add_po_details.TAG);
//                    info.set_vendor_id(data.get(getAdapterPosition()).getVendorId());

                    if (context instanceof RequirementActivity) {
                        ((RequirementActivity) context).hide_tablayout();
                        //  Fragment fragment= ((StockActivity) context).getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + ((StockActivity)context).mViewPager.getCurrentItem());
                        ((RequirementActivity) context).mSectionsPagerAdapter.onclick_method(data.get(getAdapterPosition()).getRqId());
                    } else if (context instanceof AddVehicleActivity) {
                        final Cat_Des_Requirement_List info = (Cat_Des_Requirement_List) ((AddVehicleActivity) context).getSupportFragmentManager().findFragmentByTag(Dispatch_Vehicle.TAG);
                        info.set_requirement_id(data.get(getAdapterPosition()).getRqId(), getAdapterPosition());

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


        holder.requirementSite.setText(data.get(position).getSiteDetails().get(0).getName());
        holder.purpose.setText(requirement.getPurpose());
        holder.requirementStatus.setText(requirement.getStatus());

        String date= requirement.getCreatedOn();
        AppUtil.logger("Date substring: ",date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formated = fmt.parse(date);
            SimpleDateFormat fmtout = new SimpleDateFormat("EEE, MMM dd");
            AppUtil.logger("Final date : ", fmtout.format(formated));

            holder.createdOn.setText(fmtout.format(formated));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long total_qty = 0L;
        Long rem_qty=0L;
        
        for(int i = 0 ; i<data.get(position).getProducts().size();i++)
        {
            total_qty=total_qty+data.get(position).getProducts().get(i).getQuantity();
            rem_qty=rem_qty+data.get(position).getProducts().get(i).getRemQuantity();
        }
        holder.mQuantityTotal.setText(total_qty.toString()+" bags");
        holder.mQuantityRemaining.setText(rem_qty.toString()+" rem.");




    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

