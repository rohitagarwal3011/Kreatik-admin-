package com.app.rbc.admin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Employee_list_adapter;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance_emp_list extends Fragment {


    public static final String TAG = "Attendance_emp_list";
    @BindView(R.id.select_employee)
    RecyclerView selectEmployee;
    Unbinder unbinder;
    Employee_list_adapter employee_list_adapter;
    String user_id_selected;


    public Attendance_emp_list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_emp_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_employee_list();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void set_employee_list()
    {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);

        employee_list_adapter = new Employee_list_adapter(employee.getData(), getContext(),Attendance_emp_list.TAG);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectEmployee.setLayoutManager(gridLayoutManager);
        selectEmployee.setItemAnimator(new DefaultItemAnimator());
        selectEmployee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        selectEmployee.setAdapter(employee_list_adapter);

        employee_list_adapter.notifyDataSetChanged();
    }

    public void set_employee_id(String id) throws NullPointerException
    {
        user_id_selected=id;




        new java.util.Timer().schedule(
                new java.util.TimerTask() {
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

                                                            ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main,new Attendance_emp_wise().newInstance(user_id_selected,"ROhit"),Attendance_emp_wise.TAG);

                                                        }
                                                    }

                        );

                    }
                },
                400
        );

    }

}
