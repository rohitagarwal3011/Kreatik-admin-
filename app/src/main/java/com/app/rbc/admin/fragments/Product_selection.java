package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
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

    public Product_selection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product_selection.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_selection, container, false);
        unbinder = ButterKnife.bind(this, view);
        product_grid.clear();
        count = 1;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_select_product_dialog();
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TAG.equalsIgnoreCase(Stock_po_create_task.TAG))
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main, Stock_po_create_task.newInstance(category_selected,user_selected),TAG);
                else if(TAG.equalsIgnoreCase(Stock_add_po_details.TAG))
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main, Stock_add_po_details.newInstance(category_selected,user_selected),TAG);
                else if(TAG.equalsIgnoreCase(Requirement_create_new.TAG))
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main, Requirement_create_new.newInstance(category_selected,TAG),TAG);
                else if(TAG.equalsIgnoreCase(Requirement_fulfill_task.TAG))
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main, Requirement_fulfill_task.newInstance(category_selected,user_selected),TAG);
                else if(TAG.equalsIgnoreCase(Dispatch_Vehicle.TAG))
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main, Dispatch_Vehicle.newInstance(category_selected,user_selected),TAG);


            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        get_product_list();
        show_select_product_dialog();
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

        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Select a product");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_product_selection);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final Spinner product_spinner = (Spinner) dialog.findViewById(R.id.select_product);
        final EditText quantity = (EditText) dialog.findViewById(R.id.quantity);
        Button submit = (Button) dialog.findViewById(R.id.submit_button);

        dialog.show();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, product_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

                    dialog.dismiss();
                    set_hash_map(product_spinner.getSelectedItem().toString(), quantity.getText().toString().trim());
                }
            }
        });


    }

    private void set_hash_map(String product, String quantity) {


        if(product_grid.containsKey(product))
        {
            for(int i=0;i<productTable.getChildCount();i++)
            {
                View view = productTable.getChildAt(i);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;
                    if( row.getChildAt(0) instanceof TextView  ) {
                       TextView tv = (TextView) row.getChildAt(0);
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




        product_grid.put(product, quantity);
        AppUtil.logger("Product_selection", " Product : " + product + " Quantity : " + quantity);



//         <TableRow
//        android:id="@+id/rowheading"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginTop="@dimen/_5sdp"
//        android:layout_marginBottom="@dimen/_5sdp"
//        android:padding="@dimen/_3sdp"
//                >
//                <TextView
//        android:layout_width="match_parent"
//        android:layout_height="@dimen/_15sdp"
//        android:layout_weight="1"
//        android:gravity="center"
//        android:text="Product"/>
//                <TextView
//        android:layout_width="match_parent"
//        android:layout_height="@dimen/_15sdp"
//        android:text="Quantity"
//        android:gravity="center"
//        android:layout_weight="1"/>
//            </TableRow>
        TableRow tr = new TableRow(getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen._5sdp), 0, (int) getResources().getDimensionPixelSize(R.dimen._5sdp));
        tr.setLayoutParams(layoutParams);
        tr.setPadding((int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp));

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.CENTER);
        tv.setText(product);

        tr.addView(tv, 0);

        TextView tv1 = new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        tv1.setGravity(Gravity.CENTER);
        tv1.setText(quantity);

        tr.addView(tv1, 1);

        productTable.addView(tr, count);
        count++;

        if(count>1)
            proceedButton.setVisibility(View.VISIBLE );
        AppUtil.logger("Product_selection", "Row has been added");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
