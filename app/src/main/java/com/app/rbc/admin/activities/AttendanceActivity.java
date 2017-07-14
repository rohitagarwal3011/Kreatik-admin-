package com.app.rbc.admin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.Attendance_all;
import com.app.rbc.admin.fragments.Attendance_emp_wise;
import com.app.rbc.admin.fragments.Attendance_mark;
import com.app.rbc.admin.fragments.Task_details;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

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
    private String TAG = "AttendanceActivity";
   RackMonthPicker rackMonthPicker;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Attendance_all info = (Attendance_all) getSupportFragmentManager().findFragmentByTag(Attendance_all.TAG);
                info.show_mark_dialog();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, new Attendance_all(),Attendance_all.TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_attendance, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().findFragmentByTag(Attendance_all.TAG).isVisible()) {
            Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
                AppUtil.logger(TAG,"Home pressed");
                onBackPressed();
                return true;
            case R.id.search_attendance:
                if(getSupportFragmentManager().findFragmentByTag(Attendance_all.TAG).isVisible()){
                    final Attendance_all info = (Attendance_all) getSupportFragmentManager().findFragmentByTag(Attendance_all.TAG);
                    info.show_dialog();

                }
                else if (getSupportFragmentManager().findFragmentByTag(Attendance_emp_wise.TAG).isVisible()){
                    final Attendance_emp_wise info = (Attendance_emp_wise) getSupportFragmentManager().findFragmentByTag(Attendance_emp_wise.TAG);
                    info.show_dialog();

                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbar(String title)
    {
        toolbar.setTitle(title);
    }





}
