package com.app.rbc.admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Site;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddSiteFragment extends Fragment implements View.OnClickListener{

    private EditText site_name;
    private Spinner site_type_spinner;
    private TextView error;
    private String[] site_types;
    private SweetAlertDialog sweetAlertDialog;

    private static boolean edit =false;
    private static long editId;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_site, container, false);
        if(edit) {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Edit");
        }
        else {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Add a site");
        }
        initializeUI();
        return view;
    }

    private void initializeUI() {
        site_name = (EditText) view.findViewById(R.id.form_site_name);
        site_type_spinner = (Spinner) view.findViewById(R.id.form_site_type_spinner);

        Button save = (Button) view.findViewById(R.id.save);
        Button save_add_another = (Button) view.findViewById(R.id.save_add_another);
        error = (TextView) view.findViewById(R.id.error);

        // Setting data

        site_types = getActivity().getResources().getStringArray(R.array.site_types);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                site_types);
        site_type_spinner.setAdapter(spinnerAdapter);

        // Listeners
        save.setOnClickListener(this);
        save_add_another.setOnClickListener(this);

        if(edit == true) {
            Site site = Site.findById(Site.class,editId);
            site_name.setText(site.getName());
            for(int i = 0 ; i < site_types.length ; i++) {
                if(site_types[i].equals(site.getType())) {
                    site_type_spinner.setSelection(i);
                    break;
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.form_employee_image:
                break;
            case R.id.save:
                if(edit != true) {
                    validateSiteAddForm(70);
                }
                break;
            case R.id.save_add_another:
                if(edit != true) {
                    validateSiteAddForm(71);
                }
                break;
        }
    }

    private void validateSiteAddForm(int code) {
        error.setText("");
        int validate = 1;
        if(site_name.getText().equals("") || site_name.getText().toString().length() < 5) {
            validate = 0;
            error.setText("Site name must atleast be 5 characters");
        }
        if(validate == 1) {
            callSiteAddAPI(code);
        }
    }

    private void callSiteAddAPI(int code) {

        Site site = new Site();
        site.setName(site_name.getText().toString());
        site.setType(site_types[site_type_spinner.getSelectedItemPosition()]);

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addSite(site);
    }

    private void refreshUI() {
        site_name.setText("");
        site_type_spinner.setSelection(0);
    }

    private void callSitesFetchApi() {
        new Thread(){
            public void run() {
                APIController controller = new APIController(getContext(),30);
                controller.fetchSites();
            }
        }.start();

    }

    public static AddSiteFragment newInstance(Object... data) {
        AddSiteFragment addSiteFragment = new AddSiteFragment();
        if(data.length != 0) {
            addSiteFragment.edit = true;
            addSiteFragment.editId = (long) data[0];
        }
        else {
            addSiteFragment.edit = false;
            addSiteFragment.editId = 0;
        }
        return addSiteFragment;
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 :
                if(code == 71) {
                    refreshUI();
                }
                else if(code == 70){
                    callSitesFetchApi();
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
