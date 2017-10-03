package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.utils.AppUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rohit on 15/7/17.
 */

public class Transaction_detail_adapter extends RecyclerView.Adapter<Transaction_detail_adapter.MyViewHolder> {



    private List<StockCategoryDetails.TransactionDetail> data;
    private Context context;
    private LayoutInflater inflater;

    int count;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView source;
        TextView destination;
        TextView transactionDate,transaction_status;
        TextView vehicle_number,driver_name,challan_link;
        SimpleDraweeView challan_img,invoice_img,
        onreceive_img,unloaded_img;

        LinearLayout tableLinear;

        TableLayout productTable;

        public MyViewHolder(View view) {
            super(view);
            transactionDate = (TextView) view.findViewById(R.id.transaction_date);
            source = (TextView) view.findViewById(R.id.source);
            destination = (TextView) view.findViewById(R.id.destination);
            productTable = (TableLayout) view.findViewById(R.id.product_table);
            vehicle_number = (TextView) view.findViewById(R.id.vehicle_number);
            driver_name = (TextView) view.findViewById(R.id.driver_name);
            challan_link = (TextView) view.findViewById(R.id.challan_link);
            challan_img = (SimpleDraweeView) view.findViewById(R.id.challan_img);
            invoice_img = (SimpleDraweeView) view.findViewById(R.id.invoice_img);
            onreceive_img = (SimpleDraweeView) view.findViewById(R.id.onrecieve_img);
            unloaded_img = (SimpleDraweeView) view.findViewById(R.id.unloaded_img);
            tableLinear = (LinearLayout) view.findViewById(R.id.tableLinear);
            transaction_status = (TextView) view.findViewById(R.id.transaction_status);

            count=1;

        }
    }


    public Transaction_detail_adapter(List<StockCategoryDetails.TransactionDetail> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_vehicle_info, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        StockCategoryDetails.TransactionDetail transactionDetail = data.get(position);


        holder.source.setText(transactionDetail.getDetails().get(0).getSource());
        holder.destination.setText(transactionDetail.getDetails().get(0).getDestination());
        DateTime dateTime = new DateTime(transactionDetail.getDetails().get(0).getDispatchDt());
        holder.transactionDate.setText(dateTime.toString("MMM dd, yyyy"));

        if(!transactionDetail.getProducts().isEmpty()) {

            holder.productTable.setVisibility(View.VISIBLE);
            holder.productTable.removeAllViews();

            String unit = "";
            if(data.get(position).getProducts().size() != 0) {
                List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                        "product = ?",data.get(position).getProducts().get(0).getProduct());
                if(categoryproducts.size() != 0) {
                    unit = categoryproducts.get(0).getUnit();
                }
            }
            for (int i = 0; i < data.get(position).getProducts().size(); i++) {
                StockCategoryDetails.TransactionDetail.Product products = data.get(position).getProducts().get(i);
                String product = products.getProduct();
                String quantity = products.getQuantity().toString();

                TableRow tr = new TableRow(context);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, (int) context.getResources().getDimension(R.dimen._5sdp), 0, (int) context.getResources().getDimensionPixelSize(R.dimen._5sdp));
                tr.setLayoutParams(layoutParams);
                tr.setPadding((int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp), (int) context.getResources().getDimension(R.dimen._3sdp));

                TextView tv = new TextView(context);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv.setGravity(Gravity.LEFT);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(product);

                tr.addView(tv, 0);

                TextView tv1 = new TextView(context);
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                tv1.setGravity(Gravity.LEFT);
                tv1.setTextColor(Color.parseColor("#000000"));
                tv1.setText(quantity + " "+unit);

                tr.addView(tv1, 1);



                holder.productTable.addView(tr);
                count++;
            }
        }
        else {
            holder.tableLinear.setVisibility(View.GONE);
        }

        StockCategoryDetails.TransactionDetail.Detail detail = transactionDetail.getDetails().get(0);
        if(detail != null) {
            holder.driver_name.setText("Driver : " + detail.getDriver());
            holder.challan_link.setText("Challan No.\n" + detail.getChallanNum());
            holder.vehicle_number.setText(detail.getVehicleNumber());

            if (detail.getStatus().equalsIgnoreCase("Received")) {
                String[] urls = detail.getChallanImg().split("\\|");
                Uri challanUrl = Uri.parse(urls[0]);
                Uri invoiceUrl = Uri.parse(urls[1]);
                Uri onreceiveUrl = Uri.parse(urls[2]);
                Uri unloadedUrl = Uri.parse(urls[3]);

                holder.challan_img.setImageURI(challanUrl);
                holder.invoice_img.setImageURI(invoiceUrl);
                holder.onreceive_img.setImageURI(onreceiveUrl);
                holder.unloaded_img.setImageURI(unloadedUrl);

            }
        }
        holder.transaction_status.setText(detail.getStatus());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
