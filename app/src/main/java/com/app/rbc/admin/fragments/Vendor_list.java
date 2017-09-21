package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.adapters.Stock_category_adapter;
import com.app.rbc.admin.adapters.Vendor_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Vendors;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Vendor_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Vendor_list extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.select_vendor)
    RecyclerView selectVendor;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String category_selected;
    private String TAG;
    private Vendors vendors;
    private List<Vendors.VendorList> vendorLists = new ArrayList<>();


    public Vendor_list() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Vendor_list newInstance(String param1) {
        Vendor_list fragment = new Vendor_list();
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
            TAG = getTag();
        }
        if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG))
        {
            StockActivity.show_tabs=true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    SweetAlertDialog pDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_data();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG))
        {
            StockActivity.show_tabs=true;

        }
    }

    private void get_data() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<Vendors> call = apiServices.total_vendor_list();
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Vendor_list ", "Get Vendors : " + call.request().toString());
        call.enqueue(new Callback<Vendors>() {
            @Override
            public void onResponse(Call<Vendors> call, Response<Vendors> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.VENDORS_LIST, new Gson().toJson(response.body()));
                    vendors = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.VENDORS_LIST), Vendors.class);
                    AppUtil.logger("Vendors List : ", AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.VENDORS_LIST));
                    set_vendors_list();

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
            public void onFailure(Call<Vendors> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void set_vendors_list() {

        selectVendor.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectVendor.setLayoutManager(gridLayoutManager);
        selectVendor.setItemAnimator(new DefaultItemAnimator());
        selectVendor.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        Vendor_list_adapter adapter = new Vendor_list_adapter(vendors.getVendorList(),getContext());
        selectVendor.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    String user_id_selected;

    public void set_vendor_id(String id ) throws NullPointerException
    {
        user_id_selected=id;

        AppUtil.logger("Employee_selected : ",user_id_selected);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                             if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG))
                                                            {
                                                                StockActivity.show_tabs=false;
                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(category_selected,user_id_selected ), Stock_add_po_details.TAG);
                                                            }
//stuff that updates ui

                                                        }
                                                    }

                        );

                    }
                },
                400
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
