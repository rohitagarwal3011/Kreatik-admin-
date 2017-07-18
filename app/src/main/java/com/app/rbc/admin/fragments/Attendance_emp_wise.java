package com.app.rbc.admin.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import java.util.List;

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
public class Attendance_emp_wise extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static Boolean date_search = false;
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

    List<LegendEntry> legendEntries = new ArrayList<>();
    List<LegendEntry> month_legend_entries = new ArrayList<>();
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
    @BindView(R.id.legends_layout)
    LinearLayout legendsLayout;
    @BindView(R.id.months_legend_layout)
    LinearLayout monthsLegendLayout;
    @BindView(R.id.week_layout)
    LinearLayout weekLayout;

    RackMonthPicker rackMonthPicker;
    @BindView(R.id.month_layout)
    LinearLayout monthLayout;
    @BindView(R.id.profile_layout)
    LinearLayout profileLayout;
    @BindView(R.id.which_month)
    TextView whichMonth;


    public Attendance_emp_wise() {
        // Required empty public constructor
    }

    String user_id, user_name;

    public static Attendance_emp_wise newInstance(String user_id, String user_name) {
        Attendance_emp_wise fragment = new Attendance_emp_wise();
        Bundle args = new Bundle();
        args.putString(USER_ID, user_id);
        args.putString(USER_NAME, user_name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(USER_ID);
            user_name = getArguments().getString(USER_NAME);
        }

        date_search=false;

//        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_emp_wise, container, false);
        unbinder = ButterKnife.bind(this, view);
        rackMonthPicker = new RackMonthPicker(getContext())
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String m = month < 10 ? ("0" + month) : String.valueOf(month);
                        get_month_record(m, String.valueOf(year));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

        ((AttendanceActivity) getContext()).setToolbar(user_name);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AttendanceActivity) getActivity()).fab.hide();
        show_profile_pic();
        String date_send = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        get_attendance_record(date_send);
    }


    public void show_profile_pic() {

        String[] user = AppUtil.get_employee_from_user_id(getContext(),user_id);
        int color = getContext().getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        profilePic.getHierarchy().setRoundingParams(roundingParams);


        profilePic.setImageURI(Uri.parse(user[1]));

    }

    SweetAlertDialog pDialog;


    public void get_attendance_record(String date_send) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<EmployeewiseAttendance> call = apiServices.emp_wise_attendance(user_id, date_send);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance for Employee : " + call.request().toString() + "USER_ID :" + user_id);
        call.enqueue(new Callback<EmployeewiseAttendance>() {
            @Override
            public void onResponse(Call<EmployeewiseAttendance> call, Response<EmployeewiseAttendance> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {

                    employeewiseAttendance = new Gson().fromJson(new Gson().toJson(response.body()), EmployeewiseAttendance.class);
                    profileLayout.setVisibility(View.VISIBLE);
                    if (!date_search) {
                        SimpleDateFormat fmtout = new SimpleDateFormat("MMMM");

                        whichMonth.setText(fmtout.format(Calendar.getInstance().getTime())+"'s Status");
                        weekLayout.setVisibility(View.VISIBLE);
                        monthLayout.setVisibility(View.VISIBLE);
                    } else {
                        weekLayout.setVisibility(View.GONE);
                        monthLayout.setVisibility(View.GONE);
                    }
                    set_data();
                } else if (response.body().getMeta().getStatus() == 0) {
                    AppUtil.showToast(getContext(), response.body().getMeta().getMessage());
                    show_date_selector();
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
                attendanceRemarks.setVisibility(View.VISIBLE);
                attendanceRemarks.setText("Remarks : " + employeewiseAttendance.getTodayData().get(0).getRemarks());
            }
        } else {
            attendanceStatus.setText("Attendance is not yet uploaded");
            attendanceRemarks.setVisibility(View.GONE);
        }
    }

    private void set_weeks_data() {

        legendEntries.clear();
        float present_days = employeewiseAttendance.getWeekPresentCnt();
        final float absent_days = employeewiseAttendance.getWeekAbsentCnt();
        final float half_days = employeewiseAttendance.getWeekHdCnt();

        float[] count = {half_days, absent_days, present_days};

//        if(present_days>0)
//        {
//            //weekPresent.setText("Present : "+(int)present_days);
//
//
//        }
//        else {
//            weekPresent.setVisibility(View.GONE);
//        }
//        if(absent_days>0)
//        {
//
//            //weekAbsent.setText("Absent : "+(int)absent_days);
//
//
//        }
//
//        else
//        {
//            weekAbsent.setVisibility(View.GONE);
//
//        }
//
//        if(half_days>0)
//        {
//          //  weekHalfDay.setText("Half-day : "+(int)half_days);
//
//
//        }
//        else {
//            weekHalfDay.setVisibility(View.GONE);
//        }
        week_entries = new ArrayList<>();
        int j = 0;
        String[] legendLabels = {"Half-day", "Absent", "Present"};

        String s;
        for (int i = 0; i < 3; i++) {
            if (count[i] > 0) {
                LegendEntry entry = new LegendEntry();
                entry.label = "";
                entry.form = Legend.LegendForm.SQUARE;
                entry.formColor = weeks_colors[j];
                entry.formSize = 17f;

                legendEntries.add(j, entry);
                week_entries.add(new PieEntry(count[i], j));
                if (i == 0) {
                    s = "Half-day : " + (int) half_days;
                } else if (i == 1) {
                    s = "Absent : " + (int) absent_days;
                } else {
                    s = "Present : " + (int) present_days;
                }
                switch (j) {
                    case 0:
                        weekHalfDay.setText(s);
                        break;
                    case 1:
                        weekAbsent.setText(s);
                        break;
                    case 2:
                        weekPresent.setText(s);
                        break;
                }
                j++;
            }

        }

        if (j == 1) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._174sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._74sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            legendsLayout.setLayoutParams(lparams);

        }
        if (j == 2) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._177sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._68sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            legendsLayout.setLayoutParams(lparams);

        }

        if (j == 3) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._180sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._61sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            legendsLayout.setLayoutParams(lparams);

        }
//        week_entries.add(new PieEntry(half_days, 0));
//        week_entries.add(new PieEntry(absent_days, 1));
//        week_entries.add(new PieEntry(present_days, 2));


        week_legend = weekChart.getLegend();
        week_legend.setEnabled(false);


        week_pieDataSet = new PieDataSet(week_entries, "");

        week_pieData = new PieData(week_pieDataSet);

        week_pieData.setValueFormatter(new MyValueFormatter());
        week_pieDataSet.setColors(weeks_colors);
        weekChart.setData(week_pieData);

        Description des = weekChart.getDescription();
        des.setEnabled(false);
        weekChart.animateY(3000);


        week_legend = weekChart.getLegend();
//        week_legend.setFormSize(10f); // set the size of the legend forms/shapes
//        week_legend.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
//        week_legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
//        week_legend.setTypeface(Typeface.DEFAULT);
//        week_legend.setTextSize(12f);
//        week_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        week_legend.setTextColor(Color.BLACK);
//        week_legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
//        week_legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
//
//        // set custom labels and colors
//        week_legend.setExtra(weeks_colors, new String[] { "Set1", "Set2", "Set3"});
        week_legend.setEnabled(true);
//        week_legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        week_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        week_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        week_legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        week_legend.setYEntrySpace(5f);
        week_legend.setWordWrapEnabled(true);
        week_legend.setCustom(legendEntries);


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


    private void show_week_dialog(String leave_type) {

        final Dialog dialog = new Dialog(new ContextThemeWrapper(getContext(), R.style.CustomDialog));

        if (leave_type.equalsIgnoreCase("Absent"))
            dialog.setTitle("Absent on ");
        else
            dialog.setTitle("Took a half day on");

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        LinearLayout dates = new LinearLayout(getContext());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dates.setOrientation(LinearLayout.VERTICAL);
        dates.setPadding(10, 10, 10, 50);


        for (int i = 0; i < employeewiseAttendance.getWeekData().size(); i++) {
            TextView textView = new TextView(getContext());
            TextView remarks = new TextView(getContext());

            if (employeewiseAttendance.getWeekData().get(i).getStatus().equalsIgnoreCase(leave_type)) {

                LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(tv_params);
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(50, 10, 10, 5);

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

                remarks.setLayoutParams(tv_params);
                remarks.setGravity(Gravity.LEFT);
                remarks.setPadding(50, 0, 10, 10);
                remarks.setText("Remarks : " + employeewiseAttendance.getWeekData().get(i).getRemarks());

                dates.addView(remarks);


            }
        }

        dialog.addContentView(dates, lparams);
        dialog.show();

    }


    private void set_month_data() {
        month_legend_entries.clear();

        float present_days = employeewiseAttendance.getMonthPresentCnt();
        final float absent_days = employeewiseAttendance.getMonthAbsentCnt();
        final float half_days = employeewiseAttendance.getMonthHdCnt();

        float[] count = {half_days, absent_days, present_days};

//        if(present_days>0)
//        {
//            //weekPresent.setText("Present : "+(int)present_days);
//
//
//        }
//        else {
//            weekPresent.setVisibility(View.GONE);
//        }
//        if(absent_days>0)
//        {
//
//            //weekAbsent.setText("Absent : "+(int)absent_days);
//
//
//        }
//
//        else
//        {
//            weekAbsent.setVisibility(View.GONE);
//
//        }
//
//        if(half_days>0)
//        {
//          //  weekHalfDay.setText("Half-day : "+(int)half_days);
//
//
//        }
//        else {
//            weekHalfDay.setVisibility(View.GONE);
//        }
        month_entries = new ArrayList<>();
        int j = 0;
        String[] legendLabels = {"Half-day", "Absent", "Present"};

        String s;
        for (int i = 0; i < 3; i++) {
            if (count[i] > 0) {
                LegendEntry entry = new LegendEntry();
                entry.label = "";
                entry.form = Legend.LegendForm.SQUARE;
                entry.formColor = months_colors[j];
                entry.formSize = 17f;

                month_legend_entries.add(j, entry);
                month_entries.add(new PieEntry(count[i], j));
                if (i == 0) {
                    s = "Half-day : " + (int) half_days;
                } else if (i == 1) {
                    s = "Absent : " + (int) absent_days;
                } else {
                    s = "Present : " + (int) present_days;
                }
                switch (j) {
                    case 0:
                        monthHalfDay.setText(s);
                        break;
                    case 1:
                        monthAbsent.setText(s);
                        break;
                    case 2:
                        monthPresent.setText(s);
                        break;
                }
                j++;
            }

        }

        if (j == 1) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._174sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._74sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            monthsLegendLayout.setLayoutParams(lparams);

        }
        if (j == 2) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._177sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._68sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            monthsLegendLayout.setLayoutParams(lparams);

        }

        if (j == 3) {
            int left_margin = getResources().getDimensionPixelSize(R.dimen._180sdp);
            int top_margin = getResources().getDimensionPixelSize(R.dimen._61sdp);
            int bottom_margin = getResources().getDimensionPixelSize(R.dimen._10sdp);
            FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(left_margin, top_margin, 0, 0);
            monthsLegendLayout.setLayoutParams(lparams);

        }
//        week_entries.add(new PieEntry(half_days, 0));
//        week_entries.add(new PieEntry(absent_days, 1));
//        week_entries.add(new PieEntry(present_days, 2));


        month_pieDataSet = new PieDataSet(month_entries, "");

        month_pieData = new PieData(month_pieDataSet);

        month_pieData.setValueFormatter(new MyValueFormatter());
        month_pieDataSet.setColors(months_colors);
        monthChart.setData(month_pieData);

        Description des = monthChart.getDescription();
        des.setEnabled(false);
        monthChart.animateY(3000);


        month_legend = monthChart.getLegend();
//        week_legend.setFormSize(10f); // set the size of the legend forms/shapes
//        week_legend.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
//        week_legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
//        week_legend.setTypeface(Typeface.DEFAULT);
//        week_legend.setTextSize(12f);
//        week_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        week_legend.setTextColor(Color.BLACK);
//        week_legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
//        week_legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
//
//        // set custom labels and colors
//        week_legend.setExtra(weeks_colors, new String[] { "Set1", "Set2", "Set3"});
        month_legend.setEnabled(true);
//        week_legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        week_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        week_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        month_legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        month_legend.setYEntrySpace(5f);
        month_legend.setWordWrapEnabled(true);
        month_legend.setCustom(month_legend_entries);
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


    private void show_month_dialog(String leave_type) {

        final Dialog dialog = new Dialog(new ContextThemeWrapper(getContext(), R.style.CustomDialog));

        if (leave_type.equalsIgnoreCase("Absent"))
            dialog.setTitle("Absent on ");
        else
            dialog.setTitle("Took a half day on");


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


//
//        TextView title = (TextView)dialog.findViewById(R.id.dialog_title);
//
//        if (leave_type.equalsIgnoreCase("Absent"))
//            title.setText("You were Absent on ");
//        else
//            title.setText("You took a half day on");
//
//
//        LinearLayout dates = (LinearLayout)dialog.findViewById(R.id.dates);

        LinearLayout dates = new LinearLayout(getContext());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dates.setOrientation(LinearLayout.VERTICAL);
        dates.setPadding(10, 10, 10, 50);


        for (int i = 0; i < employeewiseAttendance.getMonthData().size(); i++) {
            TextView textView = new TextView(getContext());
            TextView remarks = new TextView(getContext());

            if (employeewiseAttendance.getMonthData().get(i).getStatus().equalsIgnoreCase(leave_type)) {

                LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(tv_params);
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(50, 10, 10, 5);

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

                remarks.setLayoutParams(tv_params);
                remarks.setGravity(Gravity.LEFT);
                remarks.setPadding(50, 0, 10, 10);
                remarks.setText("Remarks : " + employeewiseAttendance.getMonthData().get(i).getRemarks());
                dates.addView(remarks);


            }
        }

        dialog.addContentView(dates, lparams);
        dialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class MyValueFormatter implements IValueFormatter {


        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + "";
        }
    }


    public void show_dialog() {
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
                Attendance_emp_wise.this,
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
        date_search = true;
        get_attendance_record(date_send);


    }

    public void get_month_record(final String month, final String year) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<EmployeewiseAttendance> call = apiServices.monthwise_search(user_id, year, month);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance for Employee : " + call.request().toString() + "USER_ID :" + user_id + "month " + month + " year " + year);
        call.enqueue(new Callback<EmployeewiseAttendance>() {
            @Override
            public void onResponse(Call<EmployeewiseAttendance> call, Response<EmployeewiseAttendance> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {
                    weekLayout.setVisibility(View.GONE);
                    profileLayout.setVisibility(View.GONE);
                    monthLayout.setVisibility(View.VISIBLE);
                    employeewiseAttendance = new Gson().fromJson(new Gson().toJson(response.body()), EmployeewiseAttendance.class);
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date formated = null;
                    try {
                        formated = fmt.parse(year+"-"+month+"-01");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    SimpleDateFormat fmtout = new SimpleDateFormat("MMMM");

                    whichMonth.setText(fmtout.format(formated)+"'s Status");
                    set_month_data();


                }
                if (response.body().getMeta().getStatus() == 0) {

                    AppUtil.showToast(getContext(), response.body().getMeta().getMessage());
                    rackMonthPicker.show();

                }

            }

            @Override
            public void onFailure(Call<EmployeewiseAttendance> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }


}

