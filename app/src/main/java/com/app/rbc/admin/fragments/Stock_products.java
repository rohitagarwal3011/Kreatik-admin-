package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.adapters.Stock_category_adapter;
import com.app.rbc.admin.adapters.Stock_product_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.StockProductDetails;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
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
 * Use the {@link Stock_products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Stock_products extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORY = "CATEGORY";
    public static final String TAG = "Stock_products";
   // private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.stock_product_list)
    RecyclerView stockProductList;
    Unbinder unbinder;
    private int category;
    StockCategories stockCategories;


    public Stock_products() {
        // Required empty public constructor
    }


    public static Stock_products newInstance(int category) {
        Stock_products fragment = new Stock_products();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           category = getArguments().getInt(CATEGORY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_products, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    SweetAlertDialog pDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_product_list();
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
                    set_product_list();

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

    public void set_product_list() {

        stockCategories = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST), StockCategories.class);
        stockProductList.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockProductList.setLayoutManager(gridLayoutManager);


        Stock_product_adapter adapter = new Stock_product_adapter(stockCategories.getCategoryList().get(category).getProducts(),getContext());
        stockProductList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    String product_selected;

    public void set_product_type(final String product) throws NullPointerException {
        product_selected = product;


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
                                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                            ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag(Stock_products.TAG));
                                                            ((StockActivity)getContext()).get_attendance_record(product_selected);
                                                            //ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Stock_products().newInstance(product_selected), Stock_products.TAG);

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
