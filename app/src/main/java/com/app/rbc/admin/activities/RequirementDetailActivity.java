package com.app.rbc.admin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Vehicle_detail_adapter;
import com.app.rbc.admin.fragments.Requirement_fulfill_task;
import com.app.rbc.admin.fragments.Stock_list_product_wise;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Product;
import com.app.rbc.admin.models.RequirementDetails;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirementDetailActivity extends AppCompatActivity {

    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;
    @BindView(R.id.employee_name)
    TextView employeeName;
    @BindView(R.id.role)
    TextView role;
    @BindView(R.id.requirement_date)
    TextView requirementDate;
    @BindView(R.id.total_quantity)
    TextView totalQuantity;
    @BindView(R.id.remaining_quantity)
    TextView remainingQuantity;
    @BindView(R.id.purpose)
    TextView purpose;
    @BindView(R.id.Requirement_status)
    TextView requirementStatus;
    @BindView(R.id.fulfillment_desc)
    TextView fulfillmentDesc;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public RequirementDetails requirementDetails;
    Toolbar toolbar;
    public static String rq_id;
    public static String req_site;
    public static List<String> product_list = new ArrayList<>();
    @BindView(R.id.frame_main)
    FrameLayout frameMain;
    @BindView(R.id.vehicle_info)
    RecyclerView vehicleInfo;
    @BindView(R.id.rowheading)
    TableRow rowheading;
    @BindView(R.id.product_table)
    TableLayout productTable;
    @BindView(R.id.requirement_details_layout)
    LinearLayout requirementDetailsLayout;
    private String category_selected;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_detail);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        rq_id = intent.getStringExtra("rq_id");
        category_selected = intent.getStringExtra("category_selected");
        count = 1;
        RequirementActivity.show_tabs = true;
        get_data();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {

        requirementDetailsLayout.setVisibility(View.GONE);
        frameMain.setVisibility(View.VISIBLE);
        ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, Stock_list_product_wise.newInstance(category_selected), Requirement_fulfill_task.TAG);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    SweetAlertDialog pDialog;

    public void get_data() {
        pDialog = new SweetAlertDialog(RequirementDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<RequirementDetails> call = apiServices.req_details(rq_id);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Requirement details ", "Get Req details : " + call.request().toString() + "RQ_ID : " + rq_id);
        call.enqueue(new Callback<RequirementDetails>() {
            @Override
            public void onResponse(Call<RequirementDetails> call, Response<RequirementDetails> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    // AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.PO_DETAILS, new Gson().toJson(response.body()));
                    requirementDetails = new Gson().fromJson(new Gson().toJson(response.body()), RequirementDetails.class);
                    AppUtil.logger("PO Details : ", requirementDetails.toString());
                    setProduct_list();
                    setData();

//                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//                    Date formated = null;
//                    try {
//                        formated = fmt.parse(year+"-"+month+"-01");
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }


                }

            }

            @Override
            public void onFailure(Call<RequirementDetails> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(RequirementDetailActivity.this, "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private void setData() {
        AppUtil.logger("RequirementDetails : ", "Show Details");

        RequirementDetails.ReqDetail.Detail reqDetail = requirementDetails.getReqDetails().get(0).getDetails().get(0);
        requirementDate.setText(reqDetail.getmTitle());
        purpose.setText(reqDetail.getmPurpose());
        requirementStatus.setText(reqDetail.getmStatus());
        fulfillmentDesc.setText(reqDetail.getmDesc());
        toolbar.setTitle(reqDetail.getmTitle());
        req_site = reqDetail.getmSite();

        set_vehicle_info();

    }

    private void setProduct_list() {
        product_list.clear();
        for (int i = 0; i < requirementDetails.getReqDetails().get(0).getProducts().size(); i++) {
            product_list.add(requirementDetails.getReqDetails().get(0).getProducts().get(i).getProduct());
            Product reqDetail = requirementDetails.getReqDetails().get(0).getProducts().get(i);
            addrow(reqDetail.getProduct(), reqDetail.getQuantity().toString(), reqDetail.getRemQuantity().toString());
        }

    }

    private void addrow(String product, String quantity, String rem_quantity) {
        TableRow tr = new TableRow(RequirementDetailActivity.this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen._5sdp), 0, (int) getResources().getDimensionPixelSize(R.dimen._5sdp));
        tr.setLayoutParams(layoutParams);
        tr.setPadding((int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp));

        TextView tv = new TextView(RequirementDetailActivity.this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText(product);

        tr.addView(tv, 0);

        TextView tv1 = new TextView(RequirementDetailActivity.this);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setTextColor(Color.parseColor("#000000"));
        tv1.setText(quantity);

        tr.addView(tv1, 1);

        TextView tv2 = new TextView(RequirementDetailActivity.this);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv2.setTextColor(Color.parseColor("#000000"));
        tv2.setText(rem_quantity);

        tr.addView(tv2, 2);
        productTable.addView(tr, count);
        count++;
    }

    private void set_vehicle_info() {
        AppUtil.logger("RequirementDetails : ", "Show Vehicle Details");
        List<VehicleDetail> vehicleDetails = new ArrayList<>(requirementDetails.getVehicleDetails());
        vehicleDetails.addAll(requirementDetails.getPoReqVehicleDetails());
        vehicleInfo.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(RequirementDetailActivity.this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vehicleInfo.setLayoutManager(gridLayoutManager);
        vehicleInfo.setItemAnimator(new DefaultItemAnimator());
        vehicleInfo.addItemDecoration(new DividerItemDecoration(RequirementDetailActivity.this, LinearLayoutManager.VERTICAL));

        Vehicle_detail_adapter adapter = new Vehicle_detail_adapter(vehicleDetails, RequirementDetailActivity.this);
        vehicleInfo.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
