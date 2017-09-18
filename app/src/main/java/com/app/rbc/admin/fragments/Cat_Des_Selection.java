package com.app.rbc.admin.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.rbc.admin.R;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.AllSiteList;
import com.app.rbc.admin.models.StockCategories;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cat_Des_Selection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cat_Des_Selection extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "Cat_Des_Selection";

    @BindView(R.id.select_category)
    Spinner selectCategory;
    @BindView(R.id.select_source)
    Spinner selectSource;
    @BindView(R.id.select_destination)
    Spinner selectDestination;
    @BindView(R.id.submit_button)
    Button submitButton;
    Unbinder unbinder;

    AllSiteList allSiteList;
    Vendors vendors;
    @BindView(R.id.radio_site)
    RadioButton radioSite;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.radio_po)
    RadioButton radioPo;
    @BindView(R.id.PO_number)
    EditText PONumber;
    @BindView(R.id.po_number_layout)
    LinearLayout poNumberLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static String source;
    public static Long destination;
    public static boolean vehicle_for_po ;
    public static String po_number;

    public Cat_Des_Selection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cat_Des_Selection.
     */
    // TODO: Rename and change types and number of parameters
    public static Cat_Des_Selection newInstance(String param1, String param2) {
        Cat_Des_Selection fragment = new Cat_Des_Selection();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat__des__selection, container, false);
        unbinder = ButterKnife.bind(this, view);
        vehicle_for_po = false;
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_site();
        //get_vendors();
        setSelectCategory();
        onclicks();
    }

    private void onclicks() {

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_po) {
                    poNumberLayout.setVisibility(View.VISIBLE);
                    vehicle_for_po = true;
                    setSourceAsVendor();

                }
                else {
                    poNumberLayout.setVisibility(View.GONE);
                    vehicle_for_po = false;
                    setSourceasSite();
                }
            }
        });

    }

    private void setSelectCategory() {
        StockCategories stockCategories = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST), StockCategories.class);
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < stockCategories.getCategoryList().size(); i++) {
            categories.add(stockCategories.getCategoryList().get(i).getCategory());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategory.setAdapter(spinnerArrayAdapter);
    }


    SweetAlertDialog pDialog;

    private void get_site() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<AllSiteList> call = apiServices.all_site_list();
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Stock Category ", "Get Categories : " + call.request().toString());
        call.enqueue(new Callback<AllSiteList>() {
            @Override
            public void onResponse(Call<AllSiteList> call, Response<AllSiteList> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.SITE_LIST, new Gson().toJson(response.body()));
                    allSiteList = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.SITE_LIST), AllSiteList.class);
                    AppUtil.logger("Site List : ", AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.SITE_LIST));
                    get_vendors();
                    setSourceasSite();
                    setSelectDestination();

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
            public void onFailure(Call<AllSiteList> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private void get_vendors() {
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

    private void setSelectDestination() {
        List<String> sites = new ArrayList<>();
        for (int i = 0; i < allSiteList.getSiteList().size(); i++) {
            sites.add(allSiteList.getSiteList().get(i).getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sites); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDestination.setAdapter(spinnerArrayAdapter);
    }

    private void setSourceasSite() {
        List<String> sites = new ArrayList<>();
        for (int i = 0; i < allSiteList.getSiteList().size(); i++) {
            sites.add(allSiteList.getSiteList().get(i).getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sites); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSource.setAdapter(spinnerArrayAdapter);
    }

    private void setSourceAsVendor() {
        List<String> vendorlist = new ArrayList<>();
        for (int i = 0; i < vendors.getVendorList().size(); i++) {
            vendorlist.add(vendors.getVendorList().get(i).getVendorName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, vendorlist); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSource.setAdapter(spinnerArrayAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.submit_button)
    public void onViewClicked() {

        po_number = PONumber.getText().toString();

        if(vehicle_for_po)
            source = vendors.getVendorList().get(selectSource.getSelectedItemPosition()).getVendorId();
        else
            source = ""+allSiteList.getSiteList().get(selectSource.getSelectedItemPosition()).getId();

        destination = allSiteList.getSiteList().get(selectDestination.getSelectedItemPosition()).getId();

        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Cat_Des_Requirement_List.newInstance(selectCategory.getSelectedItem().toString(), selectDestination.getSelectedItem().toString()), Dispatch_Vehicle.TAG);

    }
}
