package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Requirement_create_new extends Fragment {

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
    Spinner siteId;
    @BindView(R.id.submit_task)
    ActionProcessButton submitTask;
    Unbinder unbinder;

    private String category_selected;
    private String mParam2;

    //Spinner Data
    private List<Site> sites;


    JSONArray prod_list = new JSONArray();
    private int count;

    public Requirement_create_new() {
        // Required empty public constructor
    }


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

    private void setSiteSpinner() {
        List<String> siteNames = new ArrayList<>();
        siteNames.add("--Select a site--");
        sites = Site.listAll(Site.class);

        for(int i = 0 ; i < sites.size() ; i++) {
            siteNames.add(sites.get(i).getName());
        }

        ArrayAdapter<String> site_adapter = new ArrayAdapter<String>(getContext(),
                R.layout.custom_spinner_text,
                siteNames);
        siteId.setAdapter(site_adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requirement_create_new, container, false);
        unbinder = ButterKnife.bind(this, view);

        count=1;
        Bundle bundle = ((RequirementActivity)getActivity()).finalForm;
        if(bundle != null) {
            requirement_title.setText(bundle.getString("title"));
            purpose.setText(bundle.getString("purpose"));
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create New Requirement");

        AppBarLayout.LayoutParams toolbarParams = ( AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        toolbarParams.setScrollFlags(0);
        toolbar.setLayoutParams(toolbarParams);
        setSiteSpinner();
        return view;
    }


    @Override
    public void onPause() {
        if(((RequirementActivity)getActivity()).finalForm == null) {
            ((RequirementActivity)getActivity()).finalForm = new Bundle();
        }
        Bundle bundle = ((RequirementActivity) getActivity()).finalForm;
        bundle.putString("title", requirement_title.getText().toString());
        bundle.putString("purpose", purpose.getText().toString());
        super.onPause();
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

        Log.e("Product",product);
        String unit = "";
        List<Categoryproduct> categoryproductList = Categoryproduct.find(Categoryproduct.class,
                "product = ?",product);
        Log.e("Categoryproduct",categoryproductList.size()+"");
        if(categoryproductList.size() != 0) {

            unit = categoryproductList.get(0).getUnit();
            Log.e("Unit",unit);
        }

        View tr = getActivity().getLayoutInflater().inflate(R.layout.custom_requirement_table_row,null);

        TextView productText = (TextView) tr.findViewById(R.id.product);
        TextView quantityText = (TextView) tr.findViewById(R.id.quantity);
        Button product_icon = (Button) tr.findViewById(R.id.product_icon);

        productText.setText(product);
        quantityText.setText(quantity+" "+unit);
        product_icon.setText(product.substring(0,1));

        productTable.addView(tr);
        count++;

    }

    private Boolean verify() {

        Boolean flag = false;

        if(purpose.getText().toString().trim().length()==0)
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

            Call<ResponseBody> call = apiServices.create_req( AppUtil.getString(getContext(),
                    TagsPreferences.USER_ID), purpose.getText().toString(),
                    sites.get(siteId.getSelectedItemPosition()-1).getId()+"",
                    category_selected, prod_list);

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
                                        ((RequirementActivity) getContext()).get_category_requirements(category_selected);

                                    }
                                });

                            }
                            else
                            {
                                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
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
