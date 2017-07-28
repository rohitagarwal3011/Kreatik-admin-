package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AttendanceActivity;
import com.app.rbc.admin.adapters.Employee_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.DatewiseAttendance;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.TodaysAbsentees;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Attendance_all extends Fragment implements DatePickerDialog.OnDateSetListener {


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
    @BindView(R.id.zero_absentees)
    TextView zeroAbsentees;
    @BindView(R.id.no_attendance)
    TextView noAttendance;
    RackMonthPicker rackMonthPicker;

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

        rackMonthPicker = new RackMonthPicker(getContext())
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String m = month<10?("0"+month):String.valueOf(month);
                        get_month_data(m,String.valueOf(year));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
        ((AttendanceActivity)getContext()).setToolbar("Attendance");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        empWiseAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Employee_list(), Employee_list.TAG);
            }
        });
        ((AttendanceActivity) getActivity()).fab.show();
        //toggle_view();
        getData();
    }



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

//                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//                    Date formated = null;
//                    try {
//                        formated = fmt.parse(year+"-"+month+"-01");
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
                    SimpleDateFormat fmtout = new SimpleDateFormat("MMMM");

//                    ((AttendanceActivity)getContext()).setToolbar(fmtout.format(Calendar.getInstance().getTime()));
                    totalLeaves.setText(fmtout.format(Calendar.getInstance().getTime())+"'s Status");
                    allEmployees.setVisibility(View.VISIBLE);
                    absent_grid.clear();
                    month_leave_grid.clear();
                    month_hd_count.clear();
                    month_present_count.clear();
                    hd_grid.clear();
                    todaysAbsentees = new Gson().fromJson(new Gson().toJson(response.body()), TodaysAbsentees.class);
                    set_total_absent_count();
                    if (todaysAbsentees.getToday_status()) {
                        set_absentees_list();
                    } else {
                        todays_Absentees.setVisibility(View.GONE);
                        todaysHd.setVisibility(View.GONE);
                        noAttendance.setVisibility(View.VISIBLE);
                    }



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

        if(absent_list.size()>0) {

            Employee_list_adapter absent_list_adapter = new Employee_list_adapter(absent_list, getContext(), TAG);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            absentEmployees.setLayoutManager(gridLayoutManager);
            absentEmployees.setItemAnimator(new DefaultItemAnimator());
            absentEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            absentEmployees.setAdapter(absent_list_adapter);

            absent_list_adapter.notifyDataSetChanged();
        }
        else {
            absentEmployees.setVisibility(View.GONE);
            zeroAbsentees.setVisibility(View.VISIBLE);
        }

        if(half_day.size()>0) {
            Employee_list_adapter hd_list_adapter = new Employee_list_adapter(half_day, getContext(), TAG);
            LinearLayoutManager gridLayoutManager_hd = new LinearLayoutManager(getContext());
            gridLayoutManager_hd.setOrientation(LinearLayoutManager.VERTICAL);
            hdEmployees.setLayoutManager(gridLayoutManager_hd);
            hdEmployees.setItemAnimator(new DefaultItemAnimator());
            hdEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            hdEmployees.setAdapter(hd_list_adapter);

            hd_list_adapter.notifyDataSetChanged();
        }
        else {
            hdEmployees.setVisibility(View.GONE);
            todaysHd.setVisibility(View.GONE);
        }
    }

    public static HashMap<String, String> month_leave_grid = new HashMap<>();
    public static HashMap<String, String> month_present_count= new HashMap<>();
    public static HashMap<String, String> month_hd_count= new HashMap<>();

    public static HashMap<String, String> absent_grid = new HashMap<>();
    public static HashMap<String, String> hd_grid = new HashMap<>();
    public static HashMap<String, String> present_grid = new HashMap<>();

    private void set_total_absent_count() {
        for (int i = 0; i < todaysAbsentees.getLeaveCnt().size(); i++) {
            month_leave_grid.put(todaysAbsentees.getLeaveCnt().get(i).getEmployee(), String.valueOf(todaysAbsentees.getLeaveCnt().get(i).getDcount()));
        }
        for (int i = 0; i < todaysAbsentees.getmPresentCnt().size(); i++) {
            month_present_count.put(todaysAbsentees.getmPresentCnt().get(i).getEmployee(), String.valueOf(todaysAbsentees.getmPresentCnt().get(i).getDcount()));
        }
        for (int i = 0; i < todaysAbsentees.getmHdCnt().size(); i++) {
            month_hd_count.put(todaysAbsentees.getmHdCnt().get(i).getEmployee(), String.valueOf(todaysAbsentees.getmHdCnt().get(i).getDcount()));
        }


        set_employee_list();
    }

    public void set_employee_list() {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);

        employee_list_adapter = new Employee_list_adapter(employee.getData(), getContext(), "MonthlyList");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allEmployees.setLayoutManager(gridLayoutManager);
        allEmployees.setItemAnimator(new DefaultItemAnimator());
        allEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        allEmployees.setAdapter(employee_list_adapter);

        employee_list_adapter.notifyDataSetChanged();
    }

    private void toggle_view() {
        todays_Absentees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (absentEmployees.getVisibility() == View.VISIBLE) {
                    absentEmployees.setVisibility(View.GONE);
                } else {
                    absentEmployees.setVisibility(View.VISIBLE);
                    hdEmployees.setVisibility(View.GONE);
                    allEmployees.setVisibility(View.GONE);
                }
            }
        });

        todaysHd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hdEmployees.getVisibility() == View.VISIBLE) {
                    hdEmployees.setVisibility(View.GONE);
                } else {
                    hdEmployees.setVisibility(View.VISIBLE);
                    absentEmployees.setVisibility(View.GONE);
                    allEmployees.setVisibility(View.GONE);
                }
            }
        });

        totalLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allEmployees.getVisibility() == View.VISIBLE) {
                    allEmployees.setVisibility(View.GONE);
                } else {
                    allEmployees.setVisibility(View.VISIBLE);
                    absentEmployees.setVisibility(View.GONE);
                    hdEmployees.setVisibility(View.GONE);
                }
            }
        });
    }

    String user_id_selected;

    public void set_employee_id(String id, final String user_name) throws NullPointerException {
        user_id_selected = id;


        new Timer().schedule(
                new TimerTask() {
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

                                                            ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Attendance_emp_wise().newInstance(user_id_selected,user_name), Attendance_emp_wise.TAG);

                                                        }
                                                    }

                        );

                    }
                },
                400
        );

    }

    public void get_month_data(final String month , final String year) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<TodaysAbsentees> call = apiServices.monthwise_search_admin(year,month);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Month Wise Attendance : " + call.request().toString() + "Month : "+month+"Year :"+ year);

        call.enqueue(new Callback<TodaysAbsentees>() {
            @Override
            public void onResponse(Call<TodaysAbsentees> call, Response<TodaysAbsentees> response) {
                pDialog.dismiss();

                if (response.body().getMeta().getStatus() == 2) {


                    absent_grid.clear();
                    month_leave_grid.clear();
                    month_hd_count.clear();
                    month_present_count.clear();
                    hd_grid.clear();

                    todays_Absentees.setVisibility(View.GONE);
                    todaysHd.setVisibility(View.GONE);
                    absentEmployees.setVisibility(View.GONE);
                    hdEmployees.setVisibility(View.GONE);
                    noAttendance.setVisibility(View.GONE);zeroAbsentees.setVisibility(View.GONE);
                    totalLeaves.setVisibility(View.VISIBLE);
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date formated = null;
                    try {
                        formated = fmt.parse(year+"-"+month+"-01");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    SimpleDateFormat fmtout = new SimpleDateFormat("MMMM");

                    ((AttendanceActivity)getContext()).setToolbar(fmtout.format(formated));
                    totalLeaves.setText(fmtout.format(formated)+"'s Status");
                    allEmployees.setVisibility(View.VISIBLE);

                    todaysAbsentees = new Gson().fromJson(new Gson().toJson(response.body()), TodaysAbsentees.class);
                    set_total_absent_count();
//                    if (todaysAbsentees.getToday_status()) {
//                        set_absentees_list();
//                    } else {
//                        todays_Absentees.setVisibility(View.GONE);
//                        todaysHd.setVisibility(View.GONE);
//                        noAttendance.setVisibility(View.VISIBLE);
//                    }



                }
                else if(response.body().getMeta().getStatus()==0) {

                    AppUtil.showToast(getContext(),response.body().getMeta().getMessage());
                    rackMonthPicker.show();

                }

            }

            @Override
            public void onFailure(Call<TodaysAbsentees> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_dialog()
    {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_attendance_mark_or_edit);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView mark_attendance = (TextView) dialog.findViewById(R.id.mark_attendance);
        mark_attendance.setText("Choose Date");
        TextView modify_attendance = (TextView) dialog.findViewById(R.id.modify_attendance);
        modify_attendance.setText("Choose Month");

        mark_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                show_date_selector();
               // ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("mark"),Attendance_mark.TAG);
            }
        });

        modify_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rackMonthPicker.show();
               //ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("modify"),Attendance_mark.TAG);
            }
        });



        dialog.show();
    }



    public void show_date_selector() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Attendance_all.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);

        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");


            }
        });

        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    String date_send, date_shown;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date_shown = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        date_send = "" + year + "-" + monthOfYear + "-" + dayOfMonth;
       // date.setText("Date : "+date_shown);
        AppUtil.logger(TAG, "Date : " + date_send);
        get_attendance_record();


    }


    SweetAlertDialog pDialog;

    public void get_attendance_record()
    {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<DatewiseAttendance> call = apiServices.daywise_search_admin(date_send);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance datewise for date : " + call.request().toString()+ "date :"+ date_send);
        call.enqueue(new Callback<DatewiseAttendance>() {
            @Override
            public void onResponse(Call<DatewiseAttendance> call, Response<DatewiseAttendance> response) {
                if(response.body().getMeta().getStatus()==0){
                    pDialog.dismiss();
                    AppUtil.showToast(getContext(),response.body().getMeta().getMessage());
                    show_date_selector();

                }
                else if(response.body().getMeta().getStatus()==2) {
                    pDialog.dismiss();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date formated = null;
                    try {
                        formated = fmt.parse(date_send);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    SimpleDateFormat fmtout = new SimpleDateFormat("dd MMMM , EEEE");

                    ((AttendanceActivity)getContext()).setToolbar(fmtout.format(formated));
//                    todays_Absentees.setText("Absentees for the day");
//                    todaysHd.setText("Half-day takers");
                    todays_Absentees.setVisibility(View.VISIBLE);
                    todaysHd.setVisibility(View.VISIBLE);
                    absentEmployees.setVisibility(View.VISIBLE);
                    hdEmployees.setVisibility(View.VISIBLE);
                    totalLeaves.setVisibility(View.VISIBLE);
                    allEmployees.setVisibility(View.VISIBLE);
                    zeroAbsentees.setVisibility(View.GONE);
                    noAttendance.setVisibility(View.GONE);
                    absent_grid.clear();
                    month_leave_grid.clear();
                    month_hd_count.clear();
                    month_present_count.clear();
                    hd_grid.clear();
                    present_grid.clear();
                    DatewiseAttendance datewiseAttendance = new Gson().fromJson(new Gson().toJson(response.body()), DatewiseAttendance.class);
                    List<DatewiseAttendance.SearchResult> attendance_data = datewiseAttendance.getSearchResult();
                    set_day_list(attendance_data);
//                    for (int i = 0; i < attendance_data.size(); i++) {
//                        if (attendance_data.get(i).getStatus().equalsIgnoreCase("Absent")) {
//                            Attendance_mark_adapter.attendance_grid.put(attendance_data.get(i).getEmployee(), "Absent");
//                        } else if (attendance_data.get(i).getStatus().equalsIgnoreCase("Half day")) {
//                            Attendance_mark_adapter.attendance_grid.put(attendance_data.get(i).getEmployee(), "Half day");
//                        }
//                    }
//                    set_employee_list();
                }
                else {
                    pDialog.dismiss();
                    AppUtil.showToast(getContext(),"Exception");
                }
            }

            @Override
            public void onFailure(Call<DatewiseAttendance> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }



    private void set_day_list(List<DatewiseAttendance.SearchResult> attendance_data) {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
        List<Employee.Data> absent_list = new ArrayList<>();
        List<Employee.Data> half_day = new ArrayList<>();
        List<Employee.Data> present_day = new ArrayList<>();

        for(int i =0 ;i<attendance_data.size();i++)
        {
            if (attendance_data.get(i).getStatus().equalsIgnoreCase("Absent")) {
                absent_grid.put(attendance_data.get(i).getEmployee(),"Absent");
            }

            else if (attendance_data.get(i).getStatus().equalsIgnoreCase("Half Day")) {
                hd_grid.put(attendance_data.get(i).getEmployee(),"Half day");
            }

            if (attendance_data.get(i).getStatus().equalsIgnoreCase("Present")) {
                present_grid.put(attendance_data.get(i).getEmployee(),"Present");
            }

        }

        for (int i = 0; i < employee.getData().size(); i++) {
            if (absent_grid.containsKey(employee.getData().get(i).getUserId())) {
                absent_list.add(employee.getData().get(i));
            } else if (hd_grid.containsKey(employee.getData().get(i).getUserId())) {
                half_day.add(employee.getData().get(i));
            }
            else {
                present_day.add(employee.getData().get(i));
            }

        }

        if(absent_list.size()>0) {

            Employee_list_adapter absent_list_adapter = new Employee_list_adapter(absent_list, getContext(), TAG);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            absentEmployees.setLayoutManager(gridLayoutManager);
            absentEmployees.setItemAnimator(new DefaultItemAnimator());
            absentEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            absentEmployees.setAdapter(absent_list_adapter);

            absent_list_adapter.notifyDataSetChanged();
        }
        else {
            absentEmployees.setVisibility(View.GONE);
            zeroAbsentees.setVisibility(View.VISIBLE);
        }

        if(half_day.size()>0) {
            Employee_list_adapter hd_list_adapter = new Employee_list_adapter(half_day, getContext(), TAG);
            LinearLayoutManager gridLayoutManager_hd = new LinearLayoutManager(getContext());
            gridLayoutManager_hd.setOrientation(LinearLayoutManager.VERTICAL);
            hdEmployees.setLayoutManager(gridLayoutManager_hd);
            hdEmployees.setItemAnimator(new DefaultItemAnimator());
            hdEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            hdEmployees.setAdapter(hd_list_adapter);

            hd_list_adapter.notifyDataSetChanged();
        }
        else {
            hdEmployees.setVisibility(View.GONE);
            todaysHd.setVisibility(View.GONE);
        }

        if (present_day.size()>0)
        {
            totalLeaves.setText("Present for the day");
            employee_list_adapter = new Employee_list_adapter(present_day, getContext(), TAG);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            allEmployees.setLayoutManager(gridLayoutManager);
            allEmployees.setItemAnimator(new DefaultItemAnimator());
            allEmployees.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            allEmployees.setAdapter(employee_list_adapter);

            employee_list_adapter.notifyDataSetChanged();
        }
        else {
            totalLeaves.setVisibility(View.GONE);
            allEmployees.setVisibility(View.GONE);
        }
    }


    public void show_mark_dialog()
    {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_attendance_mark_or_edit);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView mark_attendance = (TextView) dialog.findViewById(R.id.mark_attendance);
        TextView modify_attendance = (TextView) dialog.findViewById(R.id.modify_attendance);

        mark_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(todaysAbsentees.getToday_status())
                {
                    AppUtil.showToast(getContext(),"The attendance for the day has already been marked.");
                }
                else
                    ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("mark"),Attendance_mark.TAG);
            }
        });

        modify_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("modify"),Attendance_mark.TAG);
            }
        });



        dialog.show();
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
