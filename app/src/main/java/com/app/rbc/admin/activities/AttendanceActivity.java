package com.app.rbc.admin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.Attendance_all;
import com.app.rbc.admin.fragments.Attendance_mark;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {

  public  FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//                fab.hide();
//                ChangeFragment.addFragment(getSupportFragmentManager(),R.id.frame_main,new Attendance_mark(),Attendance_mark.TAG);
                show_dialog();

                //send_attendance();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, new Attendance_all(),Attendance_all.TAG);
    }


    public void send_attendance()
    {
        JSONObject obj = new JSONObject();
        JSONArray data = new JSONArray();
        try {


            JSONObject object1 = new JSONObject();
            object1.put("user_id", "SA2323");
            object1.put("status", "Present");
            object1.put("remarks", "");

            data.put(object1);
            JSONObject object2 = new JSONObject();
            object2.put("user_id", "MJ2323");
            object2.put("status", "Absent");
            object2.put("remarks", "Fever");
            data.put(object2);

            JSONObject object3 = new JSONObject();
            object3.put("user_id", "NJ2323");
            object3.put("status", "Half Day");
            object3.put("remarks", "Sleep");
            data.put(object3);



        }
        catch (Exception e)
        {
            AppUtil.logger("AttendanceActivity",e.toString());
        }
            AppUtil.logger("Attendance",data.toString());
        final ApiServices apiServices = RetrofitClient.getApiService();
       // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());

        Call<ResponseBody> call = apiServices.mark_attendance(data, String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Mark Attendance request: " + call.request().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        AppUtil.logger("Mark Attendance response: ", response.body().toString());
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call1, Throwable t) {

                AppUtil.showToast(AttendanceActivity.this, "Network Issue. Please check your connectivity and try again");
            }
        });


    }

    public void show_dialog()
    {
        final Dialog dialog = new Dialog(AttendanceActivity.this);

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
                ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("mark"),Attendance_mark.TAG);
            }
        });

        modify_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main,new Attendance_mark().newInstance("modify"),Attendance_mark.TAG);
            }
        });



        dialog.show();
    }
}
