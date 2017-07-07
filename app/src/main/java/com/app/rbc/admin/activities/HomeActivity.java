package com.app.rbc.admin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.module_task)
    LinearLayout moduleTask;
    //    @BindView(R.id.delete_task)
//    Button deleteTask;
    //  private Menu menu;
    Context context;
    public static Employee employee_list;
    public static Todolist todolist;
    final ApiServices apiServices = RetrofitClient.getApiService();
    @BindView(R.id.module_attendance)
    LinearLayout moduleAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("R. B. Corporation");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        context = this.getApplicationContext();
        AppUtil.putBoolean(context, TagsPreferences.IS_LOGIN, true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navigationHeaderView = navigationView.getHeaderView(0);
        CircularImageView imageView = (CircularImageView) navigationHeaderView.findViewById(R.id.profile_pic);
        TextView username = (TextView) navigationHeaderView.findViewById(R.id.user_name);
        TextView role = (TextView) navigationHeaderView.findViewById(R.id.user_type);
        TextView mobile = (TextView) navigationHeaderView.findViewById(R.id.user_mobile);
        TextView email = (TextView) navigationHeaderView.findViewById(R.id.user_email);
        username.setText(AppUtil.getString(context, TagsPreferences.NAME));
        role.setText(AppUtil.getString(context, TagsPreferences.ROLE));
        mobile.setText(AppUtil.getLong(context, TagsPreferences.MOBILE).toString());
        email.setText(AppUtil.getString(context, TagsPreferences.EMAIL));


        Picasso.with(context).load(AppUtil.getString(context, TagsPreferences.PROFILE_IMAGE)).into(imageView);


        navigationView.setNavigationItemSelectedListener(this);


        //   this.menu = navigationView.getMenu();

        //   updateMenuItems();

    }

    //Open task module
    @OnClick(R.id.module_task)
    public void open_task_module(View view) {

        Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
        startActivity(intent);

    }

    //Open attendance module
    @OnClick(R.id.module_attendance)
    public void open_attendance_module(View view) {

        Intent intent = new Intent(HomeActivity.this, AttendanceActivity.class);
        startActivity(intent);

    }

//
//    @OnClick(R.id.delete_task)
//    public void delete_task(View view)
//    {
//        final ApiServices apiServices = RetrofitClient.getApiService();
//       // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
//        Call<ResponseBody> call = apiServices.delete_task("RO2323","TD_99");
//        AppUtil.logger("Login Activity ", "Update Password : " + call.request().toString());
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                try {
//
//                    try {
//                        JSONObject obj = new JSONObject(response.body().string());
//                        AppUtil.logger("Delete Task response : ", obj.toString());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<ResponseBody> call1, Throwable t) {
//
//                AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
//            }
//        });
//    }


//    SweetAlertDialog pDialog;
//
//
//    public void get_todo_list() {
//
//        pDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        Call<Todolist> call = apiServices.todo_list(AppUtil.getString(context, TagsPreferences.USER_ID));
//        AppUtil.logger("Home Activity ", "Get Todo list : " + call.request().toString() + "User id : " + AppUtil.getString(context, TagsPreferences.USER_ID));
//        call.enqueue(new Callback<Todolist>() {
//            @Override
//            public void onResponse(Call<Todolist> call, Response<Todolist> response2) {
//
//
//                Todolist todo = new Todolist();
//                todo = response2.body();
//                AppUtil.putString(getApplicationContext(), TagsPreferences.TASK_LIST, new Gson().toJson(todo));
//                // todolist= new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.TODO_LIST), Todolist.class);
//                AppUtil.logger("Todo List : ", AppUtil.getString(getApplicationContext(), TagsPreferences.TASK_LIST));
//                get_employee_list();
//
//            }
//
//
//            @Override
//            public void onFailure(Call<Todolist> call, Throwable t) {
//                pDialog.dismiss();
//                AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
//            }
//        });
//
//
//    }
//
////     public void get_tasks_assigned()
////     {
////         Call<Todolist> call1 = apiServices.tasks_assigned((AppUtil.getString(context,TagsPreferences.USER_ID)));
////         AppUtil.logger("Home Activity ", "Get TAsks assigned: " + call1.request().toString() + "User id : " + AppUtil.getString(context,TagsPreferences.USER_ID) );
////         call1.enqueue(new Callback<Todolist>() {
////             @Override
////             public void onResponse(Call<Todolist> call1, Response<Todolist> response1) {
////
////                 Todolist todo = new Todolist();
////                 todo = response1.body();
////                 AppUtil.putString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED, new Gson().toJson(todo));
////                 //todolist= new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED), Todolist.class);
////                 AppUtil.logger("Tasks Assigned : ", AppUtil.getString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED));
////                    c2 = true;
////                 get_employee_list();
////
////             }
////
////
////             @Override
////             public void onFailure(Call<Todolist> call1, Throwable t) {
////
////                 AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
////             }
////         });
////     }
//
//    public void get_employee_list() {
//
//        Call<Employee> call2 = apiServices.fetch_emp(AppUtil.getString(context, TagsPreferences.USER_ID));
//        AppUtil.logger("Home Activity ", "Fetch Employee : " + call2.request().toString() + "User id : " + AppUtil.getString(context, TagsPreferences.USER_ID));
//        call2.enqueue(new Callback<Employee>() {
//            @Override
//            public void onResponse(Call<Employee> call2, Response<Employee> response) {
//
//                pDialog.dismiss();
//                Employee employee = new Employee();
//                employee = response.body();
//                AppUtil.putString(getApplicationContext(), TagsPreferences.EMPLOYEE_LIST, new Gson().toJson(employee));
//                employee_list = new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
//                AppUtil.logger("List : ", AppUtil.getString(getApplicationContext(), TagsPreferences.EMPLOYEE_LIST));
//                Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
//                startActivity(intent);
//                //  proceed();
//            }
//
//
//            @Override
//            public void onFailure(Call<Employee> call2, Throwable t) {
//                pDialog.dismiss();
//                AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
//            }
//        });
//
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        this.menu = menu;
//        return true;
//    }
//
//    //Update User Details for the navigation drawer
//    private void updateMenuItems() {
//
//        menu.findItem(R.id.user_name).setTitle(AppUtil.getString(context, TagsPreferences.NAME));
//        menu.findItem(R.id.user_type).setTitle(AppUtil.getString(context, TagsPreferences.ROLE));
//        menu.findItem(R.id.user_mobile).setTitle(AppUtil.getLong(context, TagsPreferences.MOBILE).toString());
//        menu.findItem(R.id.user_email).setTitle(AppUtil.getString(context, TagsPreferences.EMAIL));
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Logout(View view) {
        AppUtil.putBoolean(context, TagsPreferences.IS_LOGIN, false);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}

