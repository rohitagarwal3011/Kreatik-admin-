package com.app.rbc.admin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.StockActivity;
import com.app.rbc.admin.activities.TaskActivity;
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
public class Employee_list extends Fragment {


    public static final String TAG = "TAG";
    private static final String TASK_TYPE = "task_type";
    @BindView(R.id.select_employee)
    RecyclerView selectEmployee;
    Unbinder unbinder;
    Employee_list_adapter employee_list_adapter;
    String user_id_selected,tag,task_type;


    public Employee_list() {
        // Required empty public constructor
    }

    public static Employee_list newInstance(String task_type) {
        Employee_list fragment = new Employee_list();
        Bundle args = new Bundle();
        args.putString(TASK_TYPE, task_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task_type = getArguments().getString(TASK_TYPE);
            tag=getTag();
        }

        if(tag.equalsIgnoreCase(Task_create.TAG)){
        ((TaskActivity)getContext()).setToolbar("Select Employee");
        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
        TaskActivity.visible_fragment = "Employee_list";}
        else if(tag.equalsIgnoreCase(Stock_po_create_task.TAG))
        {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tag.equalsIgnoreCase(Task_create.TAG)) {
            TaskActivity.visible_fragment = "Employee_list";
            ((TaskActivity) getContext()).setToolbar("Select Employee");
        }
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
    Menu bar_menu = null;


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        bar_menu=menu;
        MenuItem item = menu.findItem(R.id.attachment);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.status);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.completed);
        item2.setVisible(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void set_employee_list()
    {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);

        employee_list_adapter = new Employee_list_adapter(employee.getData(), getContext(), tag);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectEmployee.setLayoutManager(gridLayoutManager);
        selectEmployee.setItemAnimator(new DefaultItemAnimator());
        selectEmployee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        selectEmployee.setAdapter(employee_list_adapter);

        employee_list_adapter.notifyDataSetChanged();
    }

    public void set_employee_id(String id ,final String tag) throws NullPointerException
    {
        user_id_selected=id;

        AppUtil.logger("Employee_selected : ",user_id_selected);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if(tag.equalsIgnoreCase(Task_create.TAG)) {
                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Task_create.newInstance(task_type, user_id_selected), Task_create.TAG);
                                                                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                                                            }
                                                            else if(tag.equalsIgnoreCase(Stock_po_create_task.TAG))
                                                            {
                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(task_type,user_id_selected ), Stock_po_create_task.TAG);
                                                            }
                                                            else if(tag.equalsIgnoreCase(Requirement_fulfill_task.TAG))
                                                            {
                                                                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(task_type,user_id_selected ), Requirement_fulfill_task.TAG);
                                                            }
//stuff that updates ui

                                                        }
                                                    }

                        );

                    }
                },
                400
        );

    }




}
