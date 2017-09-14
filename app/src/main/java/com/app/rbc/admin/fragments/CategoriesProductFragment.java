package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.adapters.CustomCategoryProductAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.orm.SugarDb;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoriesProductFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerView;
    private List<String> categories;
    private FloatingActionButton fab;
    private EditText category_name,product_name,category_unit;
    private TextView error;
    private Dialog addCategoryProductDialog;
    private SweetAlertDialog sweetAlertDialog;
    private CustomCategoryProductAdapter adapter;
    private Categoryproduct categoryproduct;
    private SwipeRefreshLayout swipeRefreshLayout;

    final GestureDetector mGestureDetector = new GestureDetector(getContext(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories_product, container, false);
        ((IndentRegisterActivity)getActivity()).getSupportActionBar().setTitle("Product Categories");

        initializeUI();
        return view;
    }

    private void initializeUI() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String query = "Select category from Categoryproduct group by category";
        categories = getCategories(query,getContext());


        // Recycler Listener
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);

                    ((IndentRegisterActivity)getActivity()).setFragment(9,categories.get(a));



                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        setRecyclerView(categories);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callCategoriesProductsFetchApi();
            }
        });
        if(categories.size() == 0) {
            callCategoriesProductsFetchApi();
        }



    }


    public void setRecyclerView(List<String> categories) {
        adapter = new CustomCategoryProductAdapter(getContext(), categories);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab : setAddCategoryProductDialog();
            break;
        }
    }

    private void setAddCategoryProductDialog() {
        addCategoryProductDialog = new Dialog(getActivity());
        addCategoryProductDialog.setContentView(inflateDialogLayout());
        addCategoryProductDialog.show();
    }


    private View inflateDialogLayout() {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_add_category_poduct,
                null);
        category_name = (EditText) dialogView.findViewById(R.id.category_name);
        product_name =(EditText) dialogView.findViewById(R.id.product_name);
        category_unit = (EditText) dialogView.findViewById(R.id.category_unit);


        error = (TextView) dialogView.findViewById(R.id.error);
        Button save = (Button) dialogView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCategoryProductForm(50);
            }
        });
        return dialogView;
    }

    private void validateCategoryProductForm(int code) {
        error.setText("");
        int validate = 1;
        if(category_name.getText().toString().length() < 3 || category_name.getText().toString().equals("")) {
            validate = 0;
            error.setText(error.getText()+"\nCategory name must be atleast 3 characters");
        }

        if(product_name.getText().toString().length() < 3 || product_name.getText().toString().equals("")) {
            validate = 0;
            error.setText(error.getText()+"\nProduct name must be atleast 3 characters");
        }
        if(product_name.getText().toString().equals("")) {
            validate = 0;
            error.setText(error.getText()+"\nProduct name must be atleast 3 characters");
        }

        if(validate  == 1) {
            addCategoryProductDialog.dismiss();
            callAddCategoryProductAAPI(code);
        }
    }

    private void callAddCategoryProductAAPI(int code) {
        categoryproduct = new Categoryproduct();
        categoryproduct.setCategory(category_name.getText().toString());
        categoryproduct.setProduct(product_name.getText().toString());
        categoryproduct.setUnit(category_unit.getText().toString());

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addCategoryProduct(categoryproduct);
    }


    private void callCategoriesProductsFetchApi() {
        swipeRefreshLayout.setRefreshing(true);
        APIController controller = new APIController(getContext(),51);
        controller.fetchCategoriesProducts();
    }



    public void publishAPIResponse(int status,int code,String... message) {
        if(sweetAlertDialog != null) {
            sweetAlertDialog.dismiss();
        }
        swipeRefreshLayout.setRefreshing(false);
        switch (code) {
            case 50 : switch (status) {
                case 2:
                    callCategoriesProductsFetchApi();
                    break;
                case 1:
                    addCategoryProductDialog.show();
                    error.setText(message[0]);
                    break;
                case 0:
                    addCategoryProductDialog.show();
                    error.setText("Request Failed");
                    break;
            }
            break;
            case 51 : switch(status) {
                case 2 :
                    refreshAdapter();
                    break;
                case 0:
                    Toast.makeText(getContext(),
                            message[0],
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void refreshAdapter() {
        String query = "Select category from Categoryproduct group by category";
        adapter.refreshAdapter(getCategories(query,getContext()));

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
