package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AddVehicleActivity;
import com.app.rbc.admin.activities.RequirementActivity;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.models.db.models.site_overview.Stock;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Product_selection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_selection extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rowheading)
    TableRow rowheading;
    @BindView(R.id.product_table)
    TableLayout productTable;
    Unbinder unbinder;

    StockCategories stockCategories;
    @BindView(R.id.proceed_button)
    Button proceedButton;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    // TODO: Rename and change types of parameters
    private String category_selected;
    private int category_id;
    private String TAG;
    private String user_selected;
    private List<String> product_list = new ArrayList<>();
    private List<StockCategories.CategoryList.Product> products;

    public static HashMap<String, String> product_grid = new HashMap<>();

    private int count;

    private int save_state_store;
    public Product_selection() {
        // Required empty public constructor
    }

    private Requirement state_store_req;


    public static Product_selection newInstance(String param1, String param2) {
        Product_selection fragment = new Product_selection();
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
            user_selected = getArguments().getString(ARG_PARAM2);
            TAG = getTag();
        }
        product_grid.clear();

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(TAG.equalsIgnoreCase(Requirement_create_new.TAG)) {
                        ((RequirementActivity)getActivity()).finalForm = null;
                        getActivity().onBackPressed();
                    }


                    else if(TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG)) {
                        ((RequirementDetailActivity)getActivity()).finalform = null;
                        getActivity().onBackPressed();
                    }

                    else if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG)) {
                        ((StockActivity)getActivity()).details_finalform = null;
                        getActivity().onBackPressed();
                    }

                    else if(TAG.equalsIgnoreCase(Stock_po_create_task.TAG)) {
                        ((StockActivity)getActivity()).task_finalform = null;
                        getActivity().onBackPressed();
                    }

                    else if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG)) {
                        ((AddVehicleActivity)getActivity()).finalform = null;
                        getActivity().onBackPressed();
                    }
                    else {
                        getActivity().onBackPressed();
                    }
                    return true;



                }

                return true;
            }
        });


        if(TAG.equalsIgnoreCase(Requirement_create_new.TAG)){

            RequirementActivity.show_tabs=true;

        }
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_selection, container, false);
        unbinder = ButterKnife.bind(this, view);
        count = 1;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_select_product_dialog();
            }
        });




        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select Products");


        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1) {
                    if (TAG.equalsIgnoreCase(Stock_po_create_task.TAG))
                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Stock_po_create_task.newInstance(category_selected, user_selected), TAG);
                    else if (TAG.equalsIgnoreCase(Stock_add_po_details.TAG))
                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Stock_add_po_details.newInstance(category_selected, user_selected), TAG);
                    else if (TAG.equalsIgnoreCase(Requirement_create_new.TAG)) {
                        RequirementActivity.show_tabs = false;
                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Requirement_create_new.newInstance(category_selected, TAG), TAG);
                    } else if (TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG))
                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Requirement_fulfill_task.newInstance(
                                category_selected, user_selected, state_store_req), TAG);
                    else if (TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG))
                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Dispatch_Vehicle.newInstance(category_selected, user_selected), TAG);
                }
                else
                {
                 AppUtil.showToast(getContext(),"Add a product to proceed");
                }

            }
        });
        return view;
    }


    private void initializeStateStore() {
        if(TAG.equalsIgnoreCase(Requirement_create_new.TAG)) {
            if (category_selected != null) {
                List<Order> orders = ((RequirementActivity)getActivity()).orders;
                Log.e("count",orders.size()+"");
                for(int i = 0 ; i < orders.size() ; i++) {
                    Log.e(orders.get(i).getCategory(),category_selected);
                    if((orders.get(i).getCategory().equalsIgnoreCase(category_selected))) {
                        Log.e("Order","added");
                        product_grid.put(orders.get(i).getProduct(),orders.get(i).getQuantity());
                    }
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG)) {
            if (category_selected != null) {
                List<Order> orders = ((RequirementDetailActivity)getActivity()).orders;
                Log.e("count",orders.size()+"");
                for(int i = 0 ; i < orders.size() ; i++) {
                    Log.e(orders.get(i).getCategory(),category_selected);
                    if((orders.get(i).getCategory().equalsIgnoreCase(category_selected))) {
                        Log.e("Order","added");
                        product_grid.put(orders.get(i).getProduct(),orders.get(i).getQuantity());
                    }
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG)) {
            if (category_selected != null) {
                List<Order> orders = ((StockActivity)getActivity()).po_details;
                Log.e("count",orders.size()+"");
                for(int i = 0 ; i < orders.size() ; i++) {
                    Log.e(orders.get(i).getCategory(),category_selected);
                    if((orders.get(i).getCategory().equalsIgnoreCase(category_selected))) {
                        Log.e("Order","added");
                        product_grid.put(orders.get(i).getProduct(),orders.get(i).getQuantity());
                    }
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Stock_po_create_task.TAG)) {
            if (category_selected != null) {
                List<Order> orders = ((StockActivity)getActivity()).po_task;
                Log.e("count",orders.size()+"");
                for(int i = 0 ; i < orders.size() ; i++) {
                    Log.e(orders.get(i).getCategory(),category_selected);
                    if((orders.get(i).getCategory().equalsIgnoreCase(category_selected))) {
                        Log.e("Order","added");
                        product_grid.put(orders.get(i).getProduct(),orders.get(i).getQuantity());
                    }
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG)) {
            if (category_selected != null) {
                List<Order> orders = ((AddVehicleActivity)getActivity()).orders;
                Log.e("count",orders.size()+"");
                for(int i = 0 ; i < orders.size() ; i++) {
                    Log.e(orders.get(i).getCategory(),category_selected);
                    if((orders.get(i).getCategory().equalsIgnoreCase(category_selected))) {
                        Log.e("Order","added");
                        product_grid.put(orders.get(i).getProduct(),orders.get(i).getQuantity());
                    }
                }
            }
        }


    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeStateStore();

        // initializeStateStore();
        if(product_grid.isEmpty()) {
            product_list.clear();
            get_product_list();
            show_select_product_dialog();

        }
        else
        {
            product_list.clear();
            get_product_list();
            set_data();
        }

    }

    private void set_data() {

            for (String key :product_grid.keySet()) {

                show_data(key,product_grid.get(key));
            }

    }


    public void get_product_list() {
        if(getTag().equalsIgnoreCase(Requirement_fulfill_task.TAG))
        {
            product_list.add("Choose a product");
            for (int i = 0; i < RequirementDetailActivity.product_list.size(); i++) {
                product_list.add(RequirementDetailActivity.product_list.get(i));
            }

        }
        else if(getTag().equalsIgnoreCase(Dispatch_Vehicle.TAG))
        {
            product_list.add("Choose a product");
            for (int i = 0; i < Cat_Des_Requirement_List.product_list.size(); i++) {
                product_list.add(Cat_Des_Requirement_List.product_list.get(i));
            }
        }
        else {
            stockCategories = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.STOCK_LIST), StockCategories.class);

            for (int i = 0; i < stockCategories.getCategoryList().size(); i++) {
                if (stockCategories.getCategoryList().get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    category_id = i;
                    break;
                }
            }

            products = stockCategories.getCategoryList().get(category_id).getProducts();
            product_list.add("Choose a product");


            for (int i = 0; i < products.size(); i++) {
                product_list.add(products.get(i).getProduct());
            }

        }

    }


    public void show_select_product_dialog() {
        final Dialog dialog = new Dialog(getContext());

        dialog.setTitle("Select a product");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_selection);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final Spinner product_spinner = (Spinner) dialog.findViewById(R.id.select_product);
        final EditText quantity = (EditText) dialog.findViewById(R.id.quantity);
        Button submit = (Button) dialog.findViewById(R.id.submit_button);

        dialog.show();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_text, product_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_text);
        product_spinner.setAdapter(spinnerArrayAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product_spinner.getSelectedItemId() == 0) {
                    AppUtil.showToast(getContext(), "Select a product");
                } else if (quantity.getText().toString().trim().length() == 0) {
                    quantity.requestLayout();
                    quantity.setError("Enter quantity");
                } else {


                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(quantity.getWindowToken(), 0);
                    dialog.dismiss();
                    set_hash_map(product_spinner.getSelectedItem().toString(), quantity.getText().toString().trim());
                }
            }
        });


    }

    private void setStateStoreProQuan(String product,String quantity) {
        if(TAG.equalsIgnoreCase(Requirement_create_new.TAG)) {
            Order order = new Order();
            Log.e("Category",category_selected);
            order.setCategory(category_selected);
            order.setProduct(product);
            order.setQuantity(quantity);
            ((RequirementActivity)getActivity()).orders.add(order);
        }
        else if(TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG)) {
            Order order = new Order();
            Log.e("Category",category_selected);
            order.setCategory(category_selected);
            order.setProduct(product);
            order.setQuantity(quantity);
            ((RequirementDetailActivity)getActivity()).orders.add(order);
        }

        else if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG)) {
            Order order = new Order();
            Log.e("Category",category_selected);
            order.setCategory(category_selected);
            order.setProduct(product);
            order.setQuantity(quantity);
            ((StockActivity)getActivity()).po_details.add(order);
        }

        else if(TAG.equalsIgnoreCase(Stock_po_create_task.TAG)) {
            Order order = new Order();
            Log.e("Category",category_selected);
            order.setCategory(category_selected);
            order.setProduct(product);
            order.setQuantity(quantity);
            ((StockActivity)getActivity()).po_task.add(order);
        }

        else if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG)) {
            Order order = new Order();
            Log.e("Category",category_selected);
            order.setCategory(category_selected);
            order.setProduct(product);
            order.setQuantity(quantity);
            ((AddVehicleActivity)getActivity()).orders.add(order);
        }
    }


    private void set_hash_map(String product, String quantity) {


        if(product_grid.containsKey(product))
        {
            AppUtil.logger(TAG,"Product already selected");
            for(int i=0;i<productTable.getChildCount();i++)
            {
                View view = productTable.getChildAt(i);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;

                    if( row.getChildAt(1) instanceof TextView  ) {
                       TextView tv = (TextView) row.getChildAt(1);
                        if(tv.getText().toString().equalsIgnoreCase(product))
                        {
                            productTable.removeViewAt(i);
                            count--;
                            break;
                        }
                    }
                }
            }
            product_grid.remove(product);
        }



        setStateStoreProQuan(product,quantity);
        product_grid.put(product, quantity);

        show_data(product,quantity);

    }

    private void show_data(final String product , String quantity)
    {
        AppUtil.logger("Product_selection", " Product : " + product + " Quantity : " + quantity);

        View tr = getActivity().getLayoutInflater().inflate(R.layout.custom_requirement_table_row,null);

        TextView productText = (TextView) tr.findViewById(R.id.product);
        TextView quantityText = (TextView) tr.findViewById(R.id.quantity);
        ImageView delete = (ImageView) tr.findViewById(R.id.delete_icon);
        delete.setId(count);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = productTable.getChildAt(v.getId());
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;

                    if( row.getChildAt(1) instanceof TextView  ) {
                        TextView tv = (TextView) row.getChildAt(1);
                        productTable.removeViewAt(v.getId());
                        count--;
                        deleteStateStore(tv.getText().toString());
                        product_grid.remove(tv.getText().toString());



                    }
                }
            }
        });

        productText.setText(product);
        quantityText.setText(quantity);

        productTable.addView(tr, count);
        count++;



        AppUtil.logger("Product_selection", "Row has been added");
    }

    private void deleteStateStore(String product) {
        if(TAG.equalsIgnoreCase(Requirement_create_new.TAG)) {
            List<Order> orders = ((RequirementActivity)getActivity()).orders;
            for(int i = 0 ; i < orders.size() ; i++) {
                Log.e(product,orders.get(i).getProduct());
                if(orders.get(i).getProduct().equalsIgnoreCase(product)&&
                        orders.get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    ((RequirementActivity)getActivity()).orders.remove(i);
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG)) {
            List<Order> orders = ((RequirementDetailActivity)getActivity()).orders;
            for(int i = 0 ; i < orders.size() ; i++) {
                Log.e(product,orders.get(i).getProduct());
                if(orders.get(i).getProduct().equalsIgnoreCase(product)&&
                        orders.get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    ((RequirementDetailActivity)getActivity()).orders.remove(i);
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG)) {
            List<Order> orders = ((StockActivity)getActivity()).po_details;
            for(int i = 0 ; i < orders.size() ; i++) {
                Log.e(product,orders.get(i).getProduct());
                if(orders.get(i).getProduct().equalsIgnoreCase(product)&&
                        orders.get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    ((StockActivity)getActivity()).po_details.remove(i);
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Stock_po_create_task.TAG)) {
            List<Order> orders = ((StockActivity)getActivity()).po_task;
            for(int i = 0 ; i < orders.size() ; i++) {
                Log.e(product,orders.get(i).getProduct());
                if(orders.get(i).getProduct().equalsIgnoreCase(product)&&
                        orders.get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    ((StockActivity)getActivity()).po_task.remove(i);
                }
            }
        }

        else if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG)) {
            List<Order> orders = ((AddVehicleActivity)getActivity()).orders;
            for(int i = 0 ; i < orders.size() ; i++) {
                Log.e(product,orders.get(i).getProduct());
                if(orders.get(i).getProduct().equalsIgnoreCase(product)&&
                        orders.get(i).getCategory().equalsIgnoreCase(category_selected)) {
                    ((AddVehicleActivity)getActivity()).orders.remove(i);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
