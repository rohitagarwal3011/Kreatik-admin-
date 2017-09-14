package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.adapters.CustomProductAdpater;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Categoryproduct;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Categoryproduct> products;
    private static String category;
    private CustomProductAdpater adapter;
    private Dialog addProductDialog;
    private TextView error,form_title;
    private EditText product_name;
    private Categoryproduct categoryproduct;
    private SweetAlertDialog sweetAlertDialog;
    private EditText category_title;
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ((IndentRegisterActivity)getActivity()).getSupportActionBar().setTitle(category);
        initializeUI();
        return view;
    }

    public static AddProductFragment newInstance(String category) {
        AddProductFragment addProductFragment = new AddProductFragment();
        addProductFragment.category = category;
        return addProductFragment;
    }

    private void initializeUI() {
        category_title = (EditText) view.findViewById(R.id.category_title);
        category_title.setText(category);
        recyclerView =  (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        products = Categoryproduct.find(Categoryproduct.class,"category = ?",this.category);
        setRecyclerView(products);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }


    public void setRecyclerView(final List<Categoryproduct> products) {
        adapter = new CustomProductAdpater(getContext(), products);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    //Log.e("Listener Id",jobList.get(a).getId()+"");

                    setAddProductDialog(products.get(a).getId());

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
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                setAddProductDialog();
                break;
        }
    }

    private void setAddProductDialog(Object... data) {
        addProductDialog = new Dialog(getActivity());
        addProductDialog.setContentView(inflateDialogLayout(data));
        addProductDialog.show();
    }


    private View inflateDialogLayout(Object... data) {
        View dialogView;
        if(data.length == 0) {
            dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_add_product,
                    null);
            product_name = (EditText) dialogView.findViewById(R.id.product_name);
            error = (TextView) dialogView.findViewById(R.id.error);
            form_title = (TextView) dialogView.findViewById(R.id.form_title);
            Button save = (Button) dialogView.findViewById(R.id.save);


            form_title.setText("Add a Product to " + this.category);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateProductForm(90);
                }
            });
        }
        else {
            dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_add_category_poduct,
                    null);
            TextView form_title = (TextView) dialogView.findViewById(R.id.form_title);
            EditText category_name = (EditText) dialogView.findViewById(R.id.category_name);
            EditText product_name = (EditText) dialogView.findViewById(R.id.product_name);
            Button save = (Button) dialogView.findViewById(R.id.save);


            Categoryproduct editProduct = Categoryproduct.findById(Categoryproduct.class,(long)data[0]);


            form_title.setText("Edit "+editProduct.getProduct());
            product_name.setText(editProduct.getProduct());
            category_name.setText(editProduct.getCategory());
        }
        return dialogView;
    }

    private void validateProductForm(int code) {
        error.setText("");
        int validate = 1;
        if(product_name.getText().toString().equals("") || product_name.getText().toString().length() < 5) {
            validate = 0;
            error.setText("Product name must be atleast 5 character");
        }

        if(validate == 1) {
            addProductDialog.dismiss();
            callAddCategoryProductAAPI(code);
        }
    }

    private void callAddCategoryProductAAPI(int code) {
        categoryproduct = new Categoryproduct();
        categoryproduct.setCategory(this.category);
        categoryproduct.setProduct(product_name.getText().toString());

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addCategoryProduct(categoryproduct);
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2:
                adapter.refreshAdapter(Categoryproduct.find(Categoryproduct.class,"category = ?",this.category));
                break;
            case 1 :
                addProductDialog.show();
                error.setText(message[0]);
                break;
            case 0:
                addProductDialog.show();
                error.setText("Request Failed");
                break;
        }
    }

}
