package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Requirement_create_new#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Requirement_create_new extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "Requirement_create_new";
    @BindView(R.id.requirement_title)
    EditText requirement_title;
    @BindView(R.id.requirement_category)
    TextView requirement_category;
    @BindView(R.id.product_table)
    TableLayout productTable;
    @BindView(R.id.purpose)
    EditText purpose;
    @BindView(R.id.site_id)
    TextView siteId;
    @BindView(R.id.submit_task)
    ActionProcessButton submitTask;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String category_selected;
    private String mParam2;


    JSONArray prod_list = new JSONArray();
    private int count;

    public Requirement_create_new() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Requirement_create_new.
     */
    // TODO: Rename and change types and number of parameters
    public static Requirement_create_new newInstance(String param1, String param2) {
        Requirement_create_new fragment = new Requirement_create_new();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requirement_create_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        count=1;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_data();



    }


    private void set_data() {

        requirement_category.setText(category_selected);
        try {

            for (String key : Product_selection.product_grid.keySet()) {
                JSONObject product1 = new JSONObject();
                product1.put("product", key);
                product1.put("quantity", Integer.parseInt(Product_selection.product_grid.get(key)));
                prod_list.put(product1);
                addrow(key,Product_selection.product_grid.get(key));
            }
            AppUtil.logger("Product list : ", prod_list.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addrow(String product,String quantity)
    {
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

        productTable.addView(tr, count);
        count++;
    }

    private Boolean verify() {

        Boolean flag = false;

        if(requirement_title.getText().toString().trim().length()==0)
        {
            requirement_title.setError("Enter Title");
            flag=false;
        }
        else if(purpose.getText().toString().trim().length()==0)
        {
            purpose.setError("Enter purpose");
            flag=false;
        }

        else {
            flag=true;
        }

        return flag;

    }


    @OnClick(R.id.submit_task)
    public  void submit(View view)
    {
        if (verify()) {
            submitTask.setProgress(1);
            submitTask.setEnabled(false);
            final ApiServices apiServices = RetrofitClient.getApiService();
            // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
            Call<ResponseBody> call = apiServices.create_req(requirement_title.getText().toString(), AppUtil.getString(getContext(), TagsPreferences.USER_ID), purpose.getText().toString(), "Site A", category_selected, prod_list);
            AppUtil.logger("Create a requirement ", ": " + call.request().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    submitTask.setEnabled(true);
                    submitTask.setProgress(0);
                    try {

                        try {
                            JSONObject obj = new JSONObject(response.body().string());
                            AppUtil.logger("Create a requirement: ", obj.toString());
                            if(obj.getJSONObject("meta").getInt("status")==2)
                            {
                                final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Requirement created");
                                pDialog.setContentText("Requirement has been successfully created");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        ((RequirementActivity) getContext()).show_tablayout();

                                    }
                                });

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


                @Override
                public void onFailure(Call<ResponseBody> call1, Throwable t) {

                    AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
