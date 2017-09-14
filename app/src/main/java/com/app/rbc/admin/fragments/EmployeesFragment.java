package com.app.rbc.admin.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.adapters.CustomEmployeeListAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Employee;

import java.util.List;


public class EmployeesFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerView;
    private CustomEmployeeListAdapter adapter;
    private List<Employee> employees;
    SwipeRefreshLayout swipeRefreshLayout;

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
        view = inflater.inflate(R.layout.fragment_employees, container, false);
        ((IndentRegisterActivity)getActivity()).getSupportActionBar().setTitle("Employees");
        initializeViews();
        return view;
    }

    private void initializeViews() {
        RelativeLayout add_employee_container = (RelativeLayout) view.findViewById(R.id.add_employee_container);
        TextView add_employee_title = (TextView) view.findViewById(R.id.add_employee_title);
        ImageView add_employee_icon = (ImageView) view.findViewById(R.id.add_employee_icon);
        Button add_employee_next = (Button) view.findViewById(R.id.add_employee_next);

        add_employee_container.setOnClickListener(this);
        add_employee_icon.setOnClickListener(this);
        add_employee_next.setOnClickListener(this);
        add_employee_title.setOnClickListener(this);

        // Swipe Refresh Layout

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callEmployeeFetchApi();
            }
        });

        // Initializing Recycler
        initializeCheckRecycler();
    }

    private void initializeCheckRecycler() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        employees = Employee.listAll(Employee.class);
        setRecyclerView(employees);

        if(employees.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            callEmployeeFetchApi();
        }

        // Recycler Listener

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    //Log.e("Listener Id",jobList.get(a).getId()+"");

                    ((IndentRegisterActivity)getContext()).setFragment(6,employees.get(a).getId());



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

    public void setRecyclerView(List<Employee> employees) {
        adapter = new CustomEmployeeListAdapter(getContext(),employees);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_employee_container:
            case R.id.add_employee_icon:
            case R.id.add_employee_title:
            case R.id.add_employee_next:
                ((IndentRegisterActivity)getActivity()).setFragment(6);
                break;
        }
    }


    private void callEmployeeFetchApi() {
        APIController controller = new APIController(getContext(),20);
        controller.fetchEmp();
    }

    public void publishAPIResponse(int status,int code,String... message) {
        swipeRefreshLayout.setRefreshing(false);
        switch(status) {
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

    private void refreshAdapter() {
        adapter.refreshAdapter(Employee.listAll(Employee.class));
    }

}
