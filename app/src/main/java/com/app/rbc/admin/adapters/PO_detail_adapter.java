package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.models.StockProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

/**
 * Created by rohit on 15/7/17.
 */

public class PO_detail_adapter extends RecyclerView.Adapter<PO_detail_adapter.MyViewHolder> {


    private List<StockProductDetails.PoDetail> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {


       TextView PO_number;
        TextView PO_date;
        TextView PO_amount;
        TextView PO_quantity;
        TextView PO_status;

        public MyViewHolder(View view) {
            super(view);
            PO_number = (TextView) view.findViewById(R.id.PO_number);
            PO_date = (TextView) view.findViewById(R.id.PO_date);

            PO_amount = (TextView) view.findViewById(R.id.PO_amount);
            PO_quantity = (TextView) view.findViewById(R.id.PO_quantity);
            PO_status = (TextView) view.findViewById(R.id.PO_status);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    final StockActivity.PlaceholderFragment info = (StockActivity.PlaceholderFragment) ((StockActivity) context).getSupportFragmentManager().findFragmentByTag(StockActivity.PlaceholderFragment.TAG);
//                    info.set_product_type(data.get(getAdapterPosition()).getPoNum());

                    ((StockActivity)context).hide_tablayout();
                  //  Fragment fragment= ((StockActivity) context).getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + ((StockActivity)context).mViewPager.getCurrentItem());
                    ((StockActivity)context).mSectionsPagerAdapter.onclick_method(data.get(getAdapterPosition()).getPoNum());

                }
            });

        }
    }


    public PO_detail_adapter(List<StockProductDetails.PoDetail> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public PO_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_po_details, parent, false);
        return new PO_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PO_detail_adapter.MyViewHolder holder, final int position) {
        StockProductDetails.PoDetail poDetail = data.get(position);
        holder.PO_number.setText(poDetail.getPoNum());
        holder.PO_date.setText(poDetail.getCreationDt());
        holder.PO_amount.setText(poDetail.getPrice().toString());
        holder.PO_status.setText(poDetail.getStatus());
        holder.PO_quantity.setText(poDetail.getQuantity().toString());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
