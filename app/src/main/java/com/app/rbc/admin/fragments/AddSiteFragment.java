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
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.Site;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddSiteFragment extends Fragment implements View.OnClickListener{

    private EditText site_name,site_location;
    private Spinner site_type_spinner,site_incharge_spinner;
    private TextView error;
    private String[] site_types;
    private SweetAlertDialog sweetAlertDialog;

    private static boolean edit =false;
    private static long editId;
    private Site editSite;
    private View view;
    private List<String> incharge_names,incharge_ids;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        site_location = (EditText) view.findViewById(R.id.form_site_location);
        site_type_spinner = (Spinner) view.findViewById(R.id.form_site_type_spinner);
        site_incharge_spinner = (Spinner) view.findViewById(R.id.form_site_incharge_spinner);

        Button save = (Button) view.findViewById(R.id.save);
        Button save_add_another = (Button) view.findViewById(R.id.save_add_another);
        error = (TextView) view.findViewById(R.id.error);

        // Setting data

        site_types = getActivity().getResources().getStringArray(R.array.site_types);
        ArrayAdapter<String> site_type_spinner_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                site_types);
        site_type_spinner.setAdapter(site_type_spinner_adapter);

        List<Employee> employees = Employee.find(Employee.class,
                "role = ?","Site Incharge");
        incharge_names = new ArrayList<>();
        incharge_ids = new ArrayList<>();
        for(int i = 0 ; i < employees.size() ; i++) {
            incharge_names.add(employees.get(i).getUserName());
            incharge_ids.add(employees.get(i).getUserid());
        }

        ArrayAdapter<String> site_incharge_spinner_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                incharge_names);

        site_incharge_spinner.setAdapter(site_incharge_spinner_adapter);

        // Listeners
        save.setOnClickListener(this);
        save_add_another.setOnClickListener(this);

        if(edit == true) {
            editSite = Site.findById(Site.class,editId);
            site_name.setText(editSite.getName());
            site_location.setText(editSite.getLocation());
            for(int i = 0 ; i < site_types.length ; i++) {
                if(site_types[i].equals(editSite.getType())) {
                    site_type_spinner.setSelection(i);
                    break;
                }
            }

            for(int i = 0 ; i < employees.size() ; i++) {
                if(employees.get(i).getUserid().equalsIgnoreCase(editSite.getIncharge())) {
                    site_incharge_spinner.setSelection(i);
                    break;
                }
            }


            TextView title_or = (TextView) view.findViewById(R.id.title_or);
            title_or.setText("");
            ViewGroup.LayoutParams params = save_add_another.getLayoutParams();
            params.height = 0;
            save_add_another.setLayoutParams(params);


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
                else {
                    validateSiteAddForm(72);
                }
                break;
            case R.id.save_add_another:
                validateSiteAddForm(71);
                break;
        }
    }

    private void validateSiteAddForm(int code) {
        error.setText("");
        int validate = 1;
        if(site_name.getText().equals("") || site_name.getText().toString().length() < 5) {
            validate = 0;
            site_name.setError("Site name must atleast be 5 characters");
            site_name.requestFocus();
        }
        if (site_location.getText().toString().equals("")) {
            validate = 0;
            site_location.setError("Please input a valid location");
            site_location.requestFocus();
        }
        if(validate == 1) {
            if(edit != true) {
                callSiteAddAPI(code);
            }
            else {
                callUpdateSiteAPI(code);
            }
        }
    }

    private void callSiteAddAPI(int code) {

        Site site = new Site();
        site.setName(site_name.getText().toString());
        site.setType(site_types[site_type_spinner.getSelectedItemPosition()]);
        site.setLocation(site_location.getText().toString());
        site.setIncharge(incharge_ids.get(site_incharge_spinner.getSelectedItemPosition()));

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addSite(site);
    }

    private void callUpdateSiteAPI(int code) {
        editSite.setName(site_name.getText().toString());
        editSite.setType(site_types[site_type_spinner.getSelectedItemPosition()]);
        editSite.setLocation(site_location.getText().toString());
        editSite.setIncharge(incharge_ids.get(site_incharge_spinner.getSelectedItemPosition()));

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.updateSite(editSite);
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
                else if(code == 72) {
                    editSite.save();
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
