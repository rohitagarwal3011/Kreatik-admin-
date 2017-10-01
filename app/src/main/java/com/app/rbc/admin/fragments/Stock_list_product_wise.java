package com.app.rbc.admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.adapters.Stock_detail_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.StockListProductWise;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stock_list_product_wise extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    @BindView(R.id.stock_list)
    RecyclerView stockList;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String category_selected;
    private String mParam2;

    private StockListProductWise stockListProductWise;
    List<StockCategoryDetails.StockDetail> stockDetail = new ArrayList<>();

    JSONArray productlist = new JSONArray();

    public Stock_list_product_wise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment Stock_list_product_wise.
     */
    // TODO: Rename and change types and number of parameters
    public static Stock_list_product_wise newInstance(String param1) {
        Stock_list_product_wise fragment = new Stock_list_product_wise();
        Bundle args = new Bundle();
;        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_selected = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_list_product_wise, container, false);
        unbinder = ButterKnife.bind(this, view);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select a Site");

        return view;


    }

    SweetAlertDialog pDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            get_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            productlist = new JSONArray();
            get_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get_list() throws JSONException {
        for (int i = 0; i < RequirementDetailActivity.product_list.size(); i++) {
            JSONObject product = new JSONObject();
            product.put("product", RequirementDetailActivity.product_list.get(i));
            productlist.put(product);
        }

        get_data();
    }

    private void get_data() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
       // pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<StockListProductWise> call = apiServices.prod_wise_stock(productlist);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("StockList Product Wise ", "Get Stocks : " + call.request().toString() + " Product list : " + productlist);
        call.enqueue(new Callback<StockListProductWise>() {
            @Override
            public void onResponse(Call<StockListProductWise> call, Response<StockListProductWise> response) {

                if (response.body().getMeta().getStatus() == 2) {
                    //pDialog.dismiss();
                    stockDetail.clear();
                    stockListProductWise = new Gson().fromJson(new Gson().toJson(response.body()), StockListProductWise.class);
                    AppUtil.logger("Stock Product Wise response : ", new Gson().toJson(response.body()));
                    for(int i=0;i<stockListProductWise.getStockDetails().size();i++)
                    {
                        for(int j=0;j<stockListProductWise.getStockDetails().get(i).getStocks().size();j++)
                         stockDetail.add(stockListProductWise.getStockDetails().get(i).getStocks().get(j));
                    }
                    show_data();
                }

            }

            @Override
            public void onFailure(Call<StockListProductWise> call1, Throwable t) {

               // pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_data() {
        stockList.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockList.setLayoutManager(gridLayoutManager);
        stockList.setItemAnimator(new DefaultItemAnimator());
        stockList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        Stock_detail_adapter adapter = new Stock_detail_adapter(stockDetail, getContext());
        stockList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static String site_selected;
    public static String site_name;

    public void set_site_selected(String site , String name ) throws NullPointerException
    {
        site_selected=site;
        site_name=name;

        AppUtil.logger("Site_selected : ",site_selected);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Employee_list.newInstance(category_selected), Requirement_fulfill_task.TAG);

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
