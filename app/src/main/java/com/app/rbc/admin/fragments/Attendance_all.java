package com.app.rbc.admin.fragments;

import android.graphics.Color;
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
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Employee_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.TodaysAbsentees;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Attendance_all extends Fragment {


    public static final String TAG = "Attendance_all";
    @BindView(R.id.emp_wise_attendance)
    TextView empWiseAttendance;
    Unbinder unbinder;
    @BindView(R.id.todays_absentees)
    TextView todays_Absentees;
    @BindView(R.id.absent_employees)
    RecyclerView absentEmployees;
    @BindView(R.id.total_leaves)
    TextView totalLeaves;
    @BindView(R.id.all_employees)
    RecyclerView allEmployees;
    TodaysAbsentees todaysAbsentees;

    Employee_list_adapter employee_list_adapter;
    @BindView(R.id.todays_hd)
    TextView todaysHd;
    @BindView(R.id.hd_employees)
    RecyclerView hdEmployees;

    public Attendance_all() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        absentEmployees.setNestedScrollingEnabled(false);
        hdEmployees.setNestedScrollingEnabled(false);
        allEmployees.setNestedScrollingEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        empWiseAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Attendance_emp_list(), Attendance_emp_list.TAG);
            }
        });
        //toggle_view();
        getData();
    }

    SweetAlertDialog pDialog;

    private void getData() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<TodaysAbsentees> call = apiServices.absent_hd_status();
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance : " + call.request().toString());
        call.enqueue(new Callback<TodaysAbsentees>() {
            @Override
            public void onResponse(Call<TodaysAbsentees> call, Response<TodaysAbsentees> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {

                    todaysAbsentees = new Gson().fromJson(new Gson().toJson(response.body()), TodaysAbsentees.class);
                    set_absentees_list();
                    set_total_absent_count();




                }

            }

            @Override
            public void onFailure(Call<TodaysAbsentees> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private void set_absentees_list() {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
        List<Employee.Data> absent_list = new ArrayList<>();
        List<Employee.Data> half_day = new ArrayList<>();

        for (int i = 0; i < todaysAbsentees.getTodayAbsent().size(); i++) {
            absent_grid.put(todaysAbsentees.getTodayAbsent().get(i).getEmployee(), "Absent");
        }
        for (int i = 0; i < todaysAbsentees.getTodayHd().size(); i++) {
            hd_grid.put(todaysAbsentees.getTodayHd().get(i).getEmployee(), "Absent");
        }
        for (int i = 0; i < employee.getData().size(); i++) {
            if (absent_grid.containsKey(employee.getData().get(i).getUserId())) {
                absent_list.add(employee.getData().get(i));
            } else if (hd_grid.containsKey(employee.getData().get(i).getUserId())) {
                half_day.add(employee.getData().get(i));
            }
        }

        Employee_list_adapter absent_list_adapter = new Employee_list_adapter(absent_list, getContext(), "Absent");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        absentEmployees.setLayoutManager(gridLayoutManager);
        absentEmployees.setItemAnimator(new DefaultItemAnimator());
        absentEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        absentEmployees.setAdapter(absent_list_adapter);

        absent_list_adapter.notifyDataSetChanged();


        Employee_list_adapter hd_list_adapter = new Employee_list_adapter(half_day, getContext(), "Half day");
        LinearLayoutManager gridLayoutManager_hd = new LinearLayoutManager(getContext());
        gridLayoutManager_hd.setOrientation(LinearLayoutManager.VERTICAL);
        hdEmployees.setLayoutManager(gridLayoutManager_hd);
        hdEmployees.setItemAnimator(new DefaultItemAnimator());
        hdEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        hdEmployees.setAdapter(hd_list_adapter);

        hd_list_adapter.notifyDataSetChanged();
    }

    public static HashMap<String, String> leave_grid = new HashMap<>();
    public static HashMap<String, String> absent_grid = new HashMap<>();
    public static HashMap<String, String> hd_grid = new HashMap<>();

    private void set_total_absent_count() {
        for (int i = 0; i < todaysAbsentees.getLeaveCnt().size(); i++) {
            leave_grid.put(todaysAbsentees.getLeaveCnt().get(i).getEmployee(), String.valueOf(todaysAbsentees.getLeaveCnt().get(i).getDcount()));
        }

        set_employee_list();
    }

    public void set_employee_list() {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);

        employee_list_adapter = new Employee_list_adapter(employee.getData(), getContext(), TAG);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allEmployees.setLayoutManager(gridLayoutManager);
        allEmployees.setItemAnimator(new DefaultItemAnimator());
        allEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        allEmployees.setAdapter(employee_list_adapter);

        employee_list_adapter.notifyDataSetChanged();
    }

    private void toggle_view()
    {
        todays_Absentees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(absentEmployees.getVisibility()==View.VISIBLE)
                {
                    absentEmployees.setVisibility(View.GONE);
                }
                else
                {
                    absentEmployees.setVisibility(View.VISIBLE);
                    hdEmployees.setVisibility(View.GONE);
                    allEmployees.setVisibility(View.GONE);
                }
            }
        });

        todaysHd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hdEmployees.getVisibility()==View.VISIBLE)
                {
                    hdEmployees.setVisibility(View.GONE);
                }
                else
                {
                    hdEmployees.setVisibility(View.VISIBLE);
                    absentEmployees.setVisibility(View.GONE);
                    allEmployees.setVisibility(View.GONE);
                }
            }
        });

        totalLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allEmployees.getVisibility()==View.VISIBLE)
                {
                    allEmployees.setVisibility(View.GONE);
                }
                else
                {
                    allEmployees.setVisibility(View.VISIBLE);
                    absentEmployees.setVisibility(View.GONE);
                    hdEmployees.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
