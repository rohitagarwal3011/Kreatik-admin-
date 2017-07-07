package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.AttendanceActivity;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.EmployeewiseAttendance;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance_emp_wise extends Fragment  {

    private static final String USER_ID = "user_id";
    public static final String TAG = "Attendance_emp_wise";
    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;
    @BindView(R.id.attendance_status)
    TextView attendanceStatus;
    @BindView(R.id.attendance_remarks)
    TextView attendanceRemarks;
    @BindView(R.id.week_chart)
    PieChart weekChart;
    public static final int[] weeks_colors = {
            rgb("#87da87"), rgb("#5cb85c"), rgb("#357935"), rgb("#3498db")
    };

    public static final int[] months_colors = {
            rgb("#f37872"), rgb("#ea4b43"), rgb("#d2463f"), rgb("#3498db")
    };

    ArrayList<PieEntry> week_entries;
    PieDataSet week_pieDataSet;
    PieData week_pieData;
    Legend week_legend;

    ArrayList<PieEntry> month_entries;
    PieDataSet month_pieDataSet;
    PieData month_pieData;
    Legend month_legend;

    Unbinder unbinder;
    EmployeewiseAttendance employeewiseAttendance;
    @BindView(R.id.month_chart)
    PieChart monthChart;
    @BindView(R.id.week_present)
    TextView weekPresent;
    @BindView(R.id.week_absent)
    TextView weekAbsent;
    @BindView(R.id.week_half_day)
    TextView weekHalfDay;
    @BindView(R.id.month_present)
    TextView monthPresent;
    @BindView(R.id.month_absent)
    TextView monthAbsent;
    @BindView(R.id.month_half_day)
    TextView monthHalfDay;

    public Attendance_emp_wise() {
        // Required empty public constructor
    }

    String user_id;

    public static Attendance_emp_wise newInstance(String user_id) {
        Attendance_emp_wise fragment = new Attendance_emp_wise();
        Bundle args = new Bundle();
        args.putString(USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(USER_ID);
        }

//        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_emp_wise, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AttendanceActivity) getActivity()).fab.hide();
        show_profile_pic();
        get_attendance_record();
    }

    public void show_profile_pic() {
        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
        for (int i = 0; i < employee.getData().size(); i++) {
            if (employee.getData().get(i).getUserId().equalsIgnoreCase(user_id)) {
                int color = getContext().getResources().getColor(R.color.black_overlay);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setBorder(color, 1.0f);
                roundingParams.setRoundAsCircle(true);
                profilePic.getHierarchy().setRoundingParams(roundingParams);


                profilePic.setImageURI(Uri.parse(employee.getData().get(i).getMpic_url()));

                break;
            }
        }
    }

    SweetAlertDialog pDialog;

    public void get_attendance_record() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<EmployeewiseAttendance> call = apiServices.emp_wise_attendance(user_id);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance for Employee : " + call.request().toString() + "USER_ID :" + user_id);
        call.enqueue(new Callback<EmployeewiseAttendance>() {
            @Override
            public void onResponse(Call<EmployeewiseAttendance> call, Response<EmployeewiseAttendance> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {

                    employeewiseAttendance = new Gson().fromJson(new Gson().toJson(response.body()), EmployeewiseAttendance.class);
                    set_data();

                }

            }

            @Override
            public void onFailure(Call<EmployeewiseAttendance> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private void set_data() {
        set_todays_data();
        set_weeks_data();
        set_month_data();

    }


    private void set_todays_data() {
        //Set today's data

        if (employeewiseAttendance.getTodayAttendance().equalsIgnoreCase("Marked")) {
            attendanceStatus.setText("You have been marked " + employeewiseAttendance.getTodayData().get(0).getStatus());
            if (employeewiseAttendance.getTodayData().get(0).getStatus().equalsIgnoreCase("Present")) {
                attendanceRemarks.setVisibility(View.GONE);
            } else {
                attendanceRemarks.setText("Remarks : " + employeewiseAttendance.getTodayData().get(0).getRemarks());
            }
        } else {
            attendanceStatus.setText("Your attendance for today is not yet uploaded");
            attendanceRemarks.setVisibility(View.GONE);
        }
    }

    private void set_weeks_data() {


        float present_days = employeewiseAttendance.getWeekPresentCnt();
        final float absent_days = employeewiseAttendance.getWeekAbsentCnt();
        final float half_days = employeewiseAttendance.getWeekHdCnt();

        float[] count = {half_days,absent_days,present_days};

        weekPresent.setText("Present : "+(int)present_days);
        weekAbsent.setText("Absent : "+(int)absent_days);
        weekHalfDay.setText("Half-days : "+(int)half_days);

        week_entries = new ArrayList<>();
        int j=0;
        for(int i=0;i<3;i++)
        {
            if(count[i]>0)
            {
                week_entries.add(new PieEntry(count[i], j));
                j++;
            }
        }

//        week_entries.add(new PieEntry(half_days, 0));
//        week_entries.add(new PieEntry(absent_days, 1));
//        week_entries.add(new PieEntry(present_days, 2));



        week_legend = weekChart.getLegend();
        week_legend.setEnabled(false);


        week_pieDataSet = new PieDataSet(week_entries, "");

        week_pieData = new PieData(week_pieDataSet);


        week_pieDataSet.setColors(weeks_colors);
        weekChart.setData(week_pieData);

        weekChart.animateY(3000);



        weekChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (absent_days > 0) {
                    if (half_days > 0) {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show half days
                            show_week_dialog("Half day");

                        } else if (Integer.parseInt(e.getData().toString()) == 1) {
                            //show absent days
                            show_week_dialog("Absent");
                        }
                    } else {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show absent days
                            show_week_dialog("Absent");

                        }
                    }
                } else {
                    if (half_days > 0) {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show half days
                            show_week_dialog("Half day");
                        }
                    }
                }
            }


            @Override
            public void onNothingSelected() {

            }
        });




    }


    private void show_week_dialog(String leave_type)
    {

        final Dialog dialog = new Dialog(getContext());
        if (leave_type.equalsIgnoreCase("Absent"))
            dialog.setTitle("You were Absent on ");
        else
            dialog.setTitle("You took a half day on");

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);


        LinearLayout dates = new LinearLayout(getContext());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dates.setOrientation(LinearLayout.VERTICAL);
        dates.setPadding(10,10,10,50);


        for(int i=0;i<employeewiseAttendance.getWeekData().size();i++)
        {
            TextView textView = new TextView(getContext());

            if(employeewiseAttendance.getWeekData().get(i).getStatus().equalsIgnoreCase(leave_type))
            {

                LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(tv_params);
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(50,10,10,10);

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date formated = null;
                try {
                    formated = fmt.parse(employeewiseAttendance.getWeekData().get(i).getDate());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                SimpleDateFormat fmtout = new SimpleDateFormat("dd MMMM , EEEE");

                AppUtil.logger("Final date : ", fmtout.format(formated));

                textView.setText(fmtout.format(formated));
                dates.addView(textView);
            }
        }

        dialog.addContentView(dates,lparams);
        dialog.show();

    }


    private void set_month_data() {


        float present_days = employeewiseAttendance.getMonthPresentCnt();
        final float absent_days = employeewiseAttendance.getMonthAbsentCnt();
        final float half_days = employeewiseAttendance.getMonthHdCnt();

        float[] count = {half_days,absent_days,present_days};

        monthPresent.setText("Present : "+(int)present_days);
        monthAbsent.setText("Absent : "+(int)absent_days);
        monthHalfDay.setText("Half-days : "+(int)half_days);

        month_entries = new ArrayList<>();
        int j=0;
        for(int i=0;i<3;i++)
        {
            if(count[i]>0)
            {
                month_entries.add(new PieEntry(count[i], j));
                j++;
            }
        }

//        week_entries.add(new PieEntry(half_days, 0));
//        week_entries.add(new PieEntry(absent_days, 1));
//        week_entries.add(new PieEntry(present_days, 2));


        month_legend = monthChart.getLegend();
        month_legend.setEnabled(false);


        month_pieDataSet = new PieDataSet(month_entries, "");

        month_pieData = new PieData(month_pieDataSet);


        month_pieDataSet.setColors(months_colors);
        monthChart.setData(month_pieData);

        monthChart.animateY(3000);

        monthChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (absent_days > 0) {
                    if (half_days > 0) {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show half days
                            show_month_dialog("Half day");

                        } else if (Integer.parseInt(e.getData().toString()) == 1) {
                            //show absent days
                            show_month_dialog("Absent");
                        }
                    } else {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show absent days
                            show_month_dialog("Absent");

                        }
                    }
                } else {
                    if (half_days > 0) {
                        if (Integer.parseInt(e.getData().toString()) == 0) {
                            //Show half days
                            show_month_dialog("Half day");
                        }
                    }
                }
            }


            @Override
            public void onNothingSelected() {

            }
        });

    }


    private void show_month_dialog(String leave_type)
    {

        final Dialog dialog = new Dialog(getContext());
        if (leave_type.equalsIgnoreCase("Absent"))
            dialog.setTitle("You were Absent on ");
        else
            dialog.setTitle("You took a half day on");

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);


        LinearLayout dates = new LinearLayout(getContext());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dates.setOrientation(LinearLayout.VERTICAL);
        dates.setPadding(10,10,10,50);


        for(int i=0;i<employeewiseAttendance.getMonthData().size();i++)
        {
            TextView textView = new TextView(getContext());

            if(employeewiseAttendance.getMonthData().get(i).getStatus().equalsIgnoreCase(leave_type))
            {

                LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(tv_params);
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(50,10,10,10);

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date formated = null;
                try {
                    formated = fmt.parse(employeewiseAttendance.getMonthData().get(i).getDate());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                SimpleDateFormat fmtout = new SimpleDateFormat("dd MMMM , EEEE");

                AppUtil.logger("Final date : ", fmtout.format(formated));

                textView.setText(fmtout.format(formated));
                dates.addView(textView);
            }
        }

        dialog.addContentView(dates,lparams);
        dialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
