package com.app.rbc.admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Vendor;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddVendorFragment extends Fragment implements View.OnClickListener{

    private View view;
    private EditText vendor_name,vendor_address,vendor_phone;
    private TextView error;
    private SweetAlertDialog sweetAlertDialog;

    private static boolean edit = false;
    private static long editId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_vendor, container, false);
        if(edit) {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Edit");
        }
        else {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Add a vendor");
        }

        initializeUI();
        return view;
    }

    private void initializeUI() {
        vendor_name = (EditText) view.findViewById(R.id.form_vendor_name);
        vendor_address = (EditText)view.findViewById(R.id.form_vendor_address);
        vendor_phone = (EditText)view.findViewById(R.id.form_vendor_phone);

        Button save = (Button) view.findViewById(R.id.save);
        Button save_add_another = (Button) view.findViewById(R.id.save_add_another);
        error = (TextView) view.findViewById(R.id.error);



        // Listeners
        save.setOnClickListener(this);
        save_add_another.setOnClickListener(this);

        if(edit == true) {
            Vendor vendor = Vendor.findById(Vendor.class,editId);
            vendor_name.setText(vendor.getName());
            vendor_address.setText(vendor.getAddress());
            vendor_phone.setText(vendor.getPhone());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.form_employee_image:
                break;
            case R.id.save:
                if(edit != true) {
                    validateVendorAddForm(80);
                }
                break;
            case R.id.save_add_another:
                if(edit != true) {
                    validateVendorAddForm(81);
                }
                break;
        }
    }

    private void validateVendorAddForm(int code) {
        error.setText("");
        int validate = 1;
        if(vendor_name.getText().toString().equals("") || vendor_name.getText().toString().length() < 5) {
            error.setText(error.getText()+"\nName must be atleast 5 characters");
            validate = 0;
        }
        if(vendor_address.getText().toString().equals("") || vendor_address.getText().toString().length() < 5) {
            validate = 0;
            error.setText(error.getText()+"\nEnter the full address");
        }
        if(vendor_phone.getText().toString().length() < 10 || vendor_phone.getText().toString().length() > 10 ||
                vendor_phone.getText().equals("")) {
            error.setText(error.getText()+"\nEnter a valid phone number");
            validate = 0;
        }
        if(validate == 1) {
            callVendorAddAPI(code);
        }
    }

    private void callVendorAddAPI(int code) {

        Vendor vendor = new Vendor();
        vendor.setName(vendor_name.getText().toString());
        vendor.setAddress(vendor_address.getText().toString());
        vendor.setPhone(vendor_phone.getText().toString());

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addVendor(vendor);
    }

    private void refreshUI() {
        vendor_name.setText("");
        vendor_address.setText("");
        vendor_phone.setText("");
    }

    private void callVendorsFetchApi() {
        new Thread() {
            public void run() {
                APIController controller = new APIController(getContext(),40);
                controller.fetchVendors();
            }
        }.start();

    }

    public static AddVendorFragment newInstance(Object... data) {
        AddVendorFragment addVendorFragment = new AddVendorFragment();
        if(data.length != 0) {
            addVendorFragment.edit = true;
            addVendorFragment.editId = (long)data[0];
        }
        else {
            addVendorFragment.edit = false;
            addVendorFragment.editId = 0;
        }
        return addVendorFragment;
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 :
                if(code == 81) {
                    refreshUI();
                }
                else if(code == 80){
                    callVendorsFetchApi();
                    ((IndentRegisterActivity)getActivity()).popBackStack();
                }
                break;
            case 1 : error.setText(message[0]);
                break;
            case 0: error.setText("Request Failed");
                break;
        }
    }

}
