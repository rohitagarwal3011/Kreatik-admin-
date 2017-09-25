package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.adapters.Vehicle_detail_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Product;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequirementDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequirementDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "RequirementDetails";
    @BindView(R.id.site_name)
    TextView site_name;
    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;
    @BindView(R.id.employee_name)
    TextView employeeName;
    @BindView(R.id.role)
    TextView role;
    @BindView(R.id.requirement_date)
    TextView requirementDate;
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

    Unbinder unbinder;
    @BindView(R.id.rowheading)
    TableRow rowheading;
    @BindView(R.id.product_table)
    TableLayout productTable;
    @BindView(R.id.vehicle_info)
    RecyclerView vehicleInfo;
    @BindView(R.id.requirement_details_layout)
    LinearLayout requirementDetailsLayout;
    @BindView(R.id.description_heading)
    TextView descriptionHeading;
    @BindView(R.id.vehicle_heading)
    TextView vehicleHeading;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String category_selected;

    int count;

    public com.app.rbc.admin.models.RequirementDetails requirementDetails;

    public RequirementDetails() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RequirementDetails newInstance(String param1) {
        RequirementDetails fragment = new RequirementDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_selected = getArguments().getString(ARG_PARAM1);
        }
        RequirementActivity.show_tabs = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requirement_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count = 1;
        get_data();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {

        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Stock_list_product_wise.newInstance(category_selected), Requirement_fulfill_task.TAG);

    }

    SweetAlertDialog pDialog;

    public void get_data() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<com.app.rbc.admin.models.RequirementDetails> call = apiServices.req_details(RequirementDetailActivity.rq_id);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Requirement details ", "Get Req details : " + call.request().toString() + "RQ_ID : " + RequirementDetailActivity.rq_id);
        call.enqueue(new Callback<com.app.rbc.admin.models.RequirementDetails>() {
            @Override
            public void onResponse(Call<com.app.rbc.admin.models.RequirementDetails> call, Response<com.app.rbc.admin.models.RequirementDetails> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    // AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.PO_DETAILS, new Gson().toJson(response.body()));
                    requirementDetails = new Gson().fromJson(new Gson().toJson(response.body()), com.app.rbc.admin.models.RequirementDetails.class);
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
            public void onFailure(Call<com.app.rbc.admin.models.RequirementDetails> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private void setData() {
        AppUtil.logger("RequirementDetails : ", "Show Details");

        com.app.rbc.admin.models.RequirementDetails.ReqDetail.Detail reqDetail = requirementDetails.getReqDetails().get(0).getDetails().get(0);

        if (reqDetail.getmRaisedBy().toString().trim().equalsIgnoreCase(AppUtil.getString(getContext(), TagsPreferences.USER_ID))) {

            employeeName.setText(AppUtil.getString(getContext(), TagsPreferences.NAME));
            role.setText(AppUtil.getString(getContext(), TagsPreferences.ROLE));
            int color = getContext().getResources().getColor(R.color.black_overlay);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            profilePic.getHierarchy().setRoundingParams(roundingParams);


            profilePic.setImageURI(AppUtil.getString(getContext(), TagsPreferences.PROFILE_IMAGE));

        } else {
            String[] user = AppUtil.get_employee_from_user_id(getContext(), reqDetail.getmRaisedBy().toString().trim());


            show_profile_pic(user);
            employeeName.setText(user[0]);
            role.setText(user[2]);
        }


        String date = reqDetail.getmCreatedOn();
        AppUtil.logger("Date substring: ", date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formated = fmt.parse(date);
            SimpleDateFormat fmtout = new SimpleDateFormat("EEE, MMM dd");
            AppUtil.logger("Final date : ", fmtout.format(formated));

            requirementDate.setText(fmtout.format(formated));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        purpose.setText(reqDetail.getmPurpose());
        requirementStatus.setText(reqDetail.getmStatus());
        if(reqDetail.getmDesc().toString().trim().length()==0)
        {
            descriptionHeading.setVisibility(View.GONE);
        }
        else
            fulfillmentDesc.setText(reqDetail.getmDesc());

        ((RequirementDetailActivity) getContext()).setToolbar(reqDetail.getmTitle());
        RequirementDetailActivity.req_site_name = requirementDetails.getReqDetails().get(0).getSiteDetails().get(0).getName();
        RequirementDetailActivity.req_site_id=String.valueOf(requirementDetails.getReqDetails().get(0).getSiteDetails().get(0).getId());
        site_name.setText(RequirementDetailActivity.req_site_name);
        set_vehicle_info();

    }

    public void show_profile_pic(String user[]) {

        int color = getContext().getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        profilePic.getHierarchy().setRoundingParams(roundingParams);


        profilePic.setImageURI(Uri.parse(user[1]));

    }

    private void setProduct_list() {
        RequirementDetailActivity.product_list.clear();
        for (int i = 0; i < requirementDetails.getReqDetails().get(0).getProducts().size(); i++) {
            RequirementDetailActivity.product_list.add(requirementDetails.getReqDetails().get(0).getProducts().get(i).getProduct());
            Product reqDetail = requirementDetails.getReqDetails().get(0).getProducts().get(i);
            addrow(reqDetail.getProduct(), reqDetail.getQuantity().toString(), reqDetail.getRemQuantity().toString());
        }

    }

    private void addrow(String product, String quantity, String rem_quantity) {
        TableRow tr = new TableRow(getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen._5sdp), 0, (int) getResources().getDimensionPixelSize(R.dimen._5sdp));
        tr.setLayoutParams(layoutParams);
        tr.setPadding((int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp));

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText(product);

        tr.addView(tv, 0);

        TextView tv1 = new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextColor(Color.parseColor("#000000"));
        tv1.setText(quantity);

        tr.addView(tv1, 1);

        TextView tv2 = new TextView(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv2.setGravity(Gravity.LEFT);
        tv2.setTextColor(Color.parseColor("#000000"));
        tv2.setText(rem_quantity);

        tr.addView(tv2, 2);
        productTable.addView(tr, count);
        count++;
    }

    private void set_vehicle_info() {
        AppUtil.logger("RequirementDetails : ", "Show Vehicle Details");
        List<VehicleDetail> vehicleDetails = new ArrayList<>(requirementDetails.getVehicleDetails());
        if (vehicleDetails.isEmpty()) {
            vehicleHeading.setVisibility(View.GONE);
        }
        else {
            vehicleHeading.setVisibility(View.VISIBLE);
            vehicleDetails.addAll(requirementDetails.getPoReqVehicleDetails());
            vehicleInfo.setHasFixedSize(true);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            vehicleInfo.setLayoutManager(gridLayoutManager);
            vehicleInfo.setItemAnimator(new DefaultItemAnimator());
            // vehicleInfo.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

            Vehicle_detail_adapter adapter = new Vehicle_detail_adapter(vehicleDetails, getContext());
            vehicleInfo.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
