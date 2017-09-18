package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.adapters.Stock_category_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Stock_categories#newInstance} factory method to
 * create an instance of this source_activity.
 */
public class Stock_categories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the source_activity initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Stock_categories";
    @BindView(R.id.stock_category_list)
    RecyclerView stockCategoryList;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String source_activity;
    private String mParam2;

    StockCategories stockCategories;

    public Stock_categories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this source_activity using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of source_activity Stock_categories.
     */
    // TODO: Rename and change types and number of parameters
    public static Stock_categories newInstance(String param1) {
        Stock_categories fragment = new Stock_categories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source_activity = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this source_activity
        View view = inflater.inflate(R.layout.fragment_stock_categories, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    SweetAlertDialog pDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_data();
    }


    private void get_data() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<StockCategories> call = apiServices.stock_category();
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Stock Category ", "Get Categories : " + call.request().toString());
        call.enqueue(new Callback<StockCategories>() {
            @Override
            public void onResponse(Call<StockCategories> call, Response<StockCategories> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST, new Gson().toJson(response.body()));
                    stockCategories = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST), StockCategories.class);
                    AppUtil.logger("Stock List : ", AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST));
                    set_category_list();

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
            public void onFailure(Call<StockCategories> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void set_category_list() {

        stockCategoryList.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockCategoryList.setLayoutManager(gridLayoutManager);
        stockCategoryList.setItemAnimator(new DefaultItemAnimator());
        stockCategoryList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        Stock_category_adapter adapter = new Stock_category_adapter(stockCategories.getCategoryList(),getContext(),source_activity);
        stockCategoryList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    String product_selected;

    public void set_product_type(String id) throws NullPointerException {
        product_selected = id;


        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

//                                                            ((TaskActivity)getContext()).setToolbar(toolbar_string);
//                                                            details_page=true;
//                                                            selectEmployee.setVisibility(View.GONE);
//                                                            taskDetailsPage.setVisibility(View.VISIBLE);
//                                                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
////stuff that updates ui

                                                          //  ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Stock_products().newInstance(product_selected), Stock_products.TAG);
                                                           if(source_activity.equalsIgnoreCase("StockActivity")) {
                                                               FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                               ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG));
                                                               ((StockActivity) getContext()).get_product_details(product_selected);
                                                           }
                                                           else if(source_activity.equalsIgnoreCase("RequirementActivity"))
                                                           {
                                                               FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                               ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG));
                                                               ((RequirementActivity) getContext()).get_category_requirements(product_selected);

                                                           }
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
