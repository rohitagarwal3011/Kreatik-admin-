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

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Requirement_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.CatDesRequirementList;
import com.app.rbc.admin.models.RequirementList;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
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
 * Use the {@link Cat_Des_Requirement_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cat_Des_Requirement_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    String TAG;

    // TODO: Rename and change types of parameters
    private String category_selected;
    private String destination;

    private CatDesRequirementList catDesRequirementList;

    public Cat_Des_Requirement_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cat_Des_Requirement_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Cat_Des_Requirement_List newInstance(String param1, String param2) {
        Cat_Des_Requirement_List fragment = new Cat_Des_Requirement_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_selected = getArguments().getString(ARG_PARAM1);
            destination = getArguments().getString(ARG_PARAM2);
        }
        TAG = getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat__des__requirement__list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_category_requirements();
    }

    SweetAlertDialog pDialog;


    public void get_category_requirements() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<CatDesRequirementList> call = apiServices.prod_req_list(destination,category_selected);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Requirement List ", "Get Category Requirements : " + call.request().toString() + "Category :" + category_selected +" Destination : "+destination);
        call.enqueue(new Callback<CatDesRequirementList>() {
            @Override
            public void onResponse(Call<CatDesRequirementList> call, Response<CatDesRequirementList> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    //AppUtil.putString(getApplicationContext(), TagsPreferences.REQUIREMENT_LIST, new Gson().toJson(response.body()));
                    catDesRequirementList = new Gson().fromJson( new Gson().toJson(response.body()), CatDesRequirementList.class);
                    //AppUtil.logger("Product Details : ", AppUtil.getString(getContext(), TagsPreferences.REQUIREMENT_LIST));
                    show_requirement_list(catDesRequirementList.getReqList());
                }

            }

            @Override
            public void onFailure(Call<CatDesRequirementList> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_requirement_list(List<RequirementList.ReqList> reqLists) {

        // List<RequirementList.ReqList> reqLists = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.REQUIREMENT_LIST), RequirementList.class).getReqList();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        Requirement_list_adapter adapter = new Requirement_list_adapter(reqLists, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static List<String> product_list = new ArrayList<>();

    private void setProduct_list() {

    }


    String rq_id;

    public void set_requirement_id(String id , final int position) throws NullPointerException
    {
        rq_id =id;

        AppUtil.logger("Requirement_selected : ", rq_id);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG))
                                                            {
                                                                product_list.clear();
                                                                for (int i = 0; i < catDesRequirementList.getReqList().get(position).getProducts().size(); i++) {
                                                                    product_list.add(catDesRequirementList.getReqList().get(position).getProducts().get(i).getProduct());
                                                                }
                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(category_selected, rq_id), Dispatch_Vehicle.TAG);
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
