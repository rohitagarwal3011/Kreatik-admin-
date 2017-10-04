package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.activities.SiteOverviewActivity;
import com.app.rbc.admin.adapters.Vehicle_detail_adapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.api.APIInterface;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Product;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.collect.Table;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

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
    Dialog approveDialog;

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
                    count = 1;

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

    private void setApproveDialog() {
        String unit = "";
        List<Categoryproduct> categoryproductList = Categoryproduct.find(Categoryproduct.class,
                "category = ?",category_selected);
        if(categoryproductList.size() != 0) {

            unit = categoryproductList.get(0).getUnit();
        }

        approveDialog  = new Dialog(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_approve_dialog,null);

        final TableLayout product_table = (TableLayout) dialogView.findViewById(R.id.product_table);
        final Button approve = (Button) dialogView.findViewById(R.id.approve);

        final com.app.rbc.admin.models.RequirementDetails.ReqDetail reqDetail = requirementDetails.getReqDetails().get(0);
        final int count = reqDetail.getProducts().size();

        for(int i = 0 ; i < reqDetail.getProducts().size() ; i++) {
            View tr = ((RequirementDetailActivity)getContext()).getLayoutInflater().inflate(R.layout.custom_approve_dialog_product_now,null);
            String product = reqDetail.getProducts().get(i).getProduct();
            String quantity = reqDetail.getProducts().get(i).getQuantity()+"";
            TextView productText = (TextView) tr.findViewById(R.id.product);
            TextView unitText = (TextView) tr.findViewById(R.id.unit);
            EditText quantityText = (EditText) tr.findViewById(R.id.quantity);

            productText.setText(product);
            quantityText.setText(quantity);
            unitText.setText(unit);


            product_table.addView(tr);
        }

        //Listeners
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validate = 1;
                JSONArray prod_list = new JSONArray();
                try {

                    for (int i = 0; i < count; i++) {
                        View view = product_table.getChildAt(i + 1);
                        if (view instanceof TableRow) {
                            // then, you can remove the the row you want...
                            // for instance...
                            TableRow row = (TableRow) view;
                            TextView product = (TextView) row.findViewById(R.id.product);
                            EditText quantity = (EditText) row.findViewById(R.id.quantity);
                            if (quantity.getText().toString().trim().equalsIgnoreCase("")) {
                                quantity.setError("Please enter a valid quantity");
                                quantity.requestFocus();
                                validate = 0;
                                break;
                            }
                            else {
                                JSONObject productObj = new JSONObject();
                                productObj.put("product",product.getText().toString());
                                productObj.put("quantity",Float.parseFloat(quantity.getText().toString()));
                                prod_list.put(productObj);
                            }
                        }
                    }
                    Log.e("List",prod_list.toString());
                }catch (Exception e) {
                    Log.e("Prod List Formation",e.toString());
                }

                if(validate == 1) {
                    callApproveReqQtyApi(prod_list,reqDetail.getRqId());
                    approveDialog.dismiss();
                }

            }
        });

        approveDialog.setContentView(dialogView);
        approveDialog.show();
    }

    private void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction().detach(this).attach(this).commit(); 
    }

    private void callApproveReqQtyApi(JSONArray prod_list,String rq_id) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        APIController controller = new APIController(getContext(),100,100);
        APIInterface apiInterface = controller.getInterface();
        Call<String> call = apiInterface.approveRequirementQuantity(rq_id,prod_list.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                pDialog.dismiss();
                try {
                    if (response.errorBody() == null) {
                        JSONObject body = new JSONObject(response.body().toString());
                        AppUtil.logger("Requirement Detail Approve",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");

                        if (status != 2) {
                            approveDialog.show();
                        }
                        else {
                            reloadFragment();
                        }
                        Toast.makeText(getContext(),
                                message,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        approveDialog.show();
                        AppUtil.logger("Requirement Detail Approve", response.errorBody().string());
                    }
                }
                catch (Exception e) {
                    AppUtil.logger("Requirement Approve Parsing",e.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.dismiss();
                approveDialog.show();
                AppUtil.logger("Requirement Detail Approve",t.toString());
                Toast.makeText(getContext(),
                        "Service ecnountered an error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        AppUtil.logger("RequirementDetails : ", "Show Details");


        com.app.rbc.admin.models.RequirementDetails.ReqDetail.Detail reqDetail = requirementDetails.getReqDetails().get(0).getDetails().get(0);
        if(reqDetail.getmStatus().equalsIgnoreCase("Created")) {
            setApproveDialog();
        }

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
            fulfillmentDesc.setVisibility(View.GONE);
        }
        else {
            descriptionHeading.setVisibility(View.VISIBLE);
            fulfillmentDesc.setVisibility(View.VISIBLE);
            fulfillmentDesc.setText(reqDetail.getmDesc());
        }

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

        String unit="";
        List<Categoryproduct> categoryproducts = Categoryproduct.find(Categoryproduct.class,
                "product = ?", product+"");
        if(categoryproducts.size() != 0) {
            unit = categoryproducts.get(0).getUnit();
        }
        View tr = ((RequirementDetailActivity)getContext()).getLayoutInflater().inflate(R.layout.custom_product_row_layout,null);

        TextView productText = (TextView) tr.findViewById(R.id.product);
        TextView quantityText = (TextView) tr.findViewById(R.id.quantity);
        Button product_icon = (Button) tr.findViewById(R.id.product_icon);
        TextView rem_qty=(TextView) tr.findViewById(R.id.remaining_quantity);

        productText.setText(product);
        quantityText.setText(quantity+" "+unit);
        rem_qty.setText(rem_quantity+" "+unit);
        product_icon.setText(product.substring(0,1));


        productTable.addView(tr,count);
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
            vehicleInfo.setNestedScrollingEnabled(false);
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
