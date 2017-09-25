package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.adapters.Stock_category_adapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.api.APIInterface;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;
import com.orm.SugarDb;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
 * Use the {@link Stock_categories#newInstance} factory method to
 * create an instance of this source_activity.
 */
public class Stock_categories extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the source_activity initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Stock_categories";

    @BindView(R.id.stock_category_list)
    RecyclerView stockCategoryList;
    Unbinder unbinder;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_relative)
    RelativeLayout empty_relative;

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
        fab.setOnClickListener(this);
        return view;
    }

    SweetAlertDialog pDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_data();
    }


    private void get_data() {


        if(AppUtil.getString(getContext(),TagsPreferences.STOCK_LIST).isEmpty())
        {
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

                        if(stockCategories.getCategoryList().size() != 0) {
                            empty_relative.setVisibility(View.GONE);
                        }
                        else {
                            empty_relative.setVisibility(View.VISIBLE);
                        }

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
                    empty_relative.setVisibility(View.VISIBLE);
                    pDialog.dismiss();


                    AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
                }
            });
        }
        else {
            stockCategories = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST), StockCategories.class);
            set_category_list();
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab :
                showUpdateStockDialog();
                break;
        }
    }


    private void showUpdateStockDialog() {
        Dialog dialog = new Dialog(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_update_stock_dialog,
                null);

        final Spinner category_spinner = (Spinner) dialogView.findViewById(R.id.category_spinner);
        final Spinner product_spinner = (Spinner) dialogView.findViewById(R.id.product_spinner);
        final Spinner site_spinner = (Spinner) dialogView.findViewById(R.id.site_spinner);
        final EditText quantity = (EditText) dialogView.findViewById(R.id.quantity);
        Button save = (Button) dialogView.findViewById(R.id.save);

        final ArrayList<String> categories = new ArrayList<>()
                ,products = new ArrayList<>()
                ,sites = new ArrayList<>();

        categories.add("--Category--");
        products.add("--Product--");
        sites.add("--Site--");

        String query = "Select category from Categoryproduct group by category";

        List<String> categoryproductList = getCategories(query,getContext());
        for(int i = 0 ; i < categoryproductList.size() ; i++) {
            categories.add(categoryproductList.get(i));
        }

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(getContext(),
                R.layout.custom_spinner_text,
                categories);
        category_spinner.setAdapter(category_adapter);

        final List<Site> siteList = Site.listAll(Site.class);

        for(int i = 0 ; i < siteList.size() ; i++) {
            sites.add(siteList.get(i).getName());
        }

        ArrayAdapter<String> site_adapter = new ArrayAdapter<>(getContext(),
                R.layout.custom_spinner_text,
                sites);
        site_spinner.setAdapter(site_adapter);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                products.clear();
                products.add("--Product--");
                List<Categoryproduct> productsArr = Categoryproduct.find(Categoryproduct.class,
                        "category = ?",categories.get(position));

                for(int i = 0 ; i < productsArr.size() ; i++) {
                    products.add(productsArr.get(i).getProduct());
                }

                ArrayAdapter<String> product_adapter = new ArrayAdapter<>(getContext(),
                        R.layout.custom_spinner_text,
                        products);
                product_spinner.setAdapter(product_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validate = 1;
                if(category_spinner.getSelectedItemPosition() == 0) {
                    validate = 0;
                    ((TextView)category_spinner.getSelectedView()).setError("Select a category");
                    category_spinner.requestFocus();
                }
                if(product_spinner.getSelectedItemPosition() == 0) {
                    validate = 0;
                    ((TextView)product_spinner.getSelectedView()).setError("Select a product");
                    product_spinner.requestFocus();
                }

                if(site_spinner.getSelectedItemPosition() == 0) {
                    validate = 0;
                    ((TextView)site_spinner.getSelectedView()).setError("Select a site");
                    site_spinner.requestFocus();
                }

                if(quantity.getText().toString().trim().equalsIgnoreCase("")) {
                    validate = 0;
                    quantity.setError("Please enter a valid quantity");
                    quantity.requestFocus();
                }


                if(validate == 1) {
                    Order order = new Order();
                    order.setCategory(categories.get(category_spinner.getSelectedItemPosition()));
                    order.setProduct(products.get(product_spinner.getSelectedItemPosition()));

                    order.setSite(siteList.get(site_spinner.getSelectedItemPosition()-1).getId());
                    order.setQuantity(quantity.getText().toString().trim());

                    addStock(order,siteList.get(site_spinner.getSelectedItemPosition()-1).getType());
                }
            }
        });


        dialog.setContentView(dialogView);
        dialog.show();

    }

    public void addStock(Order order,String site_type) {
        Log.e(order.getSite()+" "+order.getCategory()+" "+
        order.getProduct()+" "+order.getQuantity(),site_type+"");
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        APIController controller = new APIController(getContext(),
                1,
                1);


        Call<String> call = controller.getInterface().addStock(order.getSite()+"",
                site_type,
                order.getCategory(),
                order.getProduct(),
                order.getQuantity());



        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                pDialog.dismiss();

                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                    }catch (Exception e) {
                        Toast.makeText(getContext(),
                                "Service Encountered an error",
                                Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    try {
                        Log.e("Error body", response.errorBody().string());
                    }
                    catch (Exception e) {
                        Log.e("Error error parsing",e.toString());
                    }
                    Toast.makeText(getContext(),
                            "Service Encountered an error",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.dismiss();

                Log.e("Failure",t.getLocalizedMessage());
                Toast.makeText(getContext(),
                        "Service Encountered an error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<String> getCategories(String rawQuery, Context mContext) {
        List<String> stringList = new ArrayList<>();
        if (mContext != null) {
            SugarDb sugarDb = new SugarDb(mContext);
            SQLiteDatabase database = sugarDb.getDB();

            try {
                Cursor cursor = database.rawQuery(rawQuery, null);
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            stringList.add(cursor.getString(0));
                        } while (cursor.moveToNext());
                    }
                } finally {
                    try {
                        cursor.close();
                    } catch (Exception ignore) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringList;
    }
}
