package com.app.rbc.admin.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Attendance_mark_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.DatewiseAttendance;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.utils.AdapterWithCustomItem;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Attendance_mark extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TYPE = "type";
    public static final String TAG = "Attendance_mark";
    @BindView(R.id.select_employee)
    RecyclerView selectEmployee;
    @BindView(R.id.submit_attendance)
    ActionProcessButton submitAttendance;
    Unbinder unbinder;

    Attendance_mark_adapter attendance_mark_adapter;
    AdapterWithCustomItem dateAdapter;
    @BindView(R.id.date)
    TextView date;

    private String type;

    public Attendance_mark() {
        // Required empty public constructor
    }


    public static Attendance_mark newInstance(String type) {
        Attendance_mark fragment = new Attendance_mark();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
        }

//        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_mark, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Attendance_mark_adapter.attendance_grid.clear();
        submitAttendance.setMode(ActionProcessButton.Mode.ENDLESS);
        if (type.equalsIgnoreCase("modify")) {
            show_date_selector();
        }
        else {
            date.setText("Today's Attendance");
            date.setEnabled(false);
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date_selector();
            }
        });
        set_employee_list();
    }

    public void set_employee_list() {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);

        attendance_mark_adapter = new Attendance_mark_adapter(employee.getData(), getContext(), TAG);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectEmployee.setLayoutManager(gridLayoutManager);
        selectEmployee.setItemAnimator(new DefaultItemAnimator());
        selectEmployee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        selectEmployee.setAdapter(attendance_mark_adapter);

        attendance_mark_adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.submit_attendance)
    public void submit_attendance() {
        AppUtil.logger("Attendance", Attendance_mark_adapter.attendance_grid.toString());
        JSONArray data = new JSONArray();
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
        try {
            for (int i = 0; i < employee.getData().size(); i++) {

                String user_id = employee.getData().get(i).getUserId();
                JSONObject obj = new JSONObject();
                obj.put("user_id", user_id);
                if (Attendance_mark_adapter.attendance_grid.containsKey(user_id)) {
                    obj.put("status", Attendance_mark_adapter.attendance_grid.get(user_id));
                } else {
                    obj.put("status", "Present");
                }
                obj.put("remarks", "Pata nahi");
                data.put(obj);


            }
            send_attendance(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void send_attendance(JSONArray data) throws JSONException, IOException {
        submitAttendance.setProgress(1);
        submitAttendance.setEnabled(false);
        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        if (type.equalsIgnoreCase("mark"))
            date_send= String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

        Call<ResponseBody> call = apiServices.mark_attendance(data, date_send);
        AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Mark Attendance request: " + call.request().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                submitAttendance.setEnabled(true);
                submitAttendance.setProgress(0);
                try {

                    try {

                        JSONObject obj = new JSONObject(response.body().string());
                        AppUtil.logger("Mark Attendance response: ", obj.toString());
                        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Attendance Updated");
                        pDialog.setContentText("Attendance has been successfully marked");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.dismiss();


                            }
                        });
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call1, Throwable t) {

                submitAttendance.setEnabled(true);
                submitAttendance.setProgress(0);
                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void show_date_selector() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Attendance_mark.this,
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
        date.setText("Date : "+date_shown);
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

                }
                else if(response.body().getMeta().getStatus()==2) {
                    Attendance_mark_adapter.attendance_grid.clear();
                    pDialog.dismiss();
                    DatewiseAttendance datewiseAttendance = new Gson().fromJson(new Gson().toJson(response.body()), DatewiseAttendance.class);
                    List<DatewiseAttendance.SearchResult> attendance_data = datewiseAttendance.getSearchResult();
                    for (int i = 0; i < attendance_data.size(); i++) {
                        if (attendance_data.get(i).getStatus().equalsIgnoreCase("Absent")) {
                            Attendance_mark_adapter.attendance_grid.put(attendance_data.get(i).getEmployee(), "Absent");
                        } else if (attendance_data.get(i).getStatus().equalsIgnoreCase("Half day")) {
                            Attendance_mark_adapter.attendance_grid.put(attendance_data.get(i).getEmployee(), "Half day");
                        }
                    }
                    set_employee_list();
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

}
