package com.app.rbc.admin.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.adapters.Filter_task_adapter;
import com.app.rbc.admin.adapters.Tasks_assigned_adapter;
import com.app.rbc.admin.adapters.Todo_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Tasklogs;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.Constant;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Task_home extends Fragment implements Todo_list_adapter.OnItemLongClickListener{

    public static final String TASK_ID = "TASK_ID";
    public static final String TASK_TITLE = "TASK_TITLE";

    public static final String TAG = "Task_home";
    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    Unbinder unbinder;
    @BindView(R.id.filter_recycler_view)
    RecyclerView filterRecyclerView;
    @BindView(R.id.tasks_recycler_view)
    RecyclerView tasksRecyclerView;


    Filter_task_adapter filter_task_adapter;
    ArrayList<String> filter_list = new ArrayList<>();

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    Todo_list_adapter todo_list_adapter;
    Tasks_assigned_adapter tasks_assigned_adapter;
    @BindView(R.id.todo_list)
    Button todoList;
    @BindView(R.id.tasks_asssigned)
    Button tasksAsssigned;
    @BindView(R.id.todo_indicator)
    View todoIndicator;
    @BindView(R.id.assigned_indicator)
    View assignedIndicator;
    @BindView(R.id.empty_relative)
    RelativeLayout empty_relative;


    public static Employee employee_list;
    public static Todolist todolist;
    private String getTaskId, getTaskTitle;
    final ApiServices apiServices = RetrofitClient.getApiService();
    List<Todolist.Data1> completed_list = new ArrayList<>();
    List<Todolist.Data1> non_completed_list = new ArrayList<>();

    List<Todolist.Data1> todo_list = new ArrayList<>();
    public static boolean show_delete = false;
    public static boolean show_completed = false;

    // Filter Dialog
    private Button deadline_button;
    private Calendar myCalendar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static boolean proceed ;

    public Task_home() {
        // Required empty public constructor
    }

    public static Task_home newInstance(String task_id) {
        Task_home fragment = new Task_home();
        Bundle args = new Bundle();
        args.putString(TASK_ID, task_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            proceed = true;
            AppUtil.logger(TAG, getArguments().getString(TASK_ID));
            getTaskId = getArguments().getString(TASK_ID);
            getTaskTitle = getArguments().getString(TASK_TITLE);
        } else {
            AppUtil.logger(TAG, "Arguments is null");
        }
        AppUtil.logger(TAG, "Created");
        setHasOptionsMenu(true);
        TaskActivity.visible_fragment = TAG;
    }

    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = inflater.inflate(R.layout.fragment_task_home, container, false);
        show_delete = false;
        unbinder = ButterKnife.bind(this, rootview);
        setSwipeRefresh();
        return rootview;
    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                get_todo_list();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppUtil.logger(TAG, "Destroyed");
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtil.logger(TAG, "Resumed");
        TaskActivity.visible_fragment = TAG;
        show_delete=false;

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppUtil.logger(TAG, "Attached");
        TaskActivity.visible_fragment = TAG;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {



                    if (intent.getStringExtra("type").equalsIgnoreCase("task_update")) {





//                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.cancelAll();

                        update_order(intent.getStringExtra("task_id"),intent.getStringExtra("status"),Long.parseLong(intent.getStringExtra("unread_count")),true);
//                        Tasklogs.Log newlog1 = new Tasklogs().new Log();
//                        newlog1.setChangedBy(intent.getStringExtra("changed_by"));
//                        newlog1.setChangeTime(intent.getStringExtra("change_time"));
//                        newlog1.setDocs(intent.getStringExtra("docs"));
//                        newlog1.setStatus(intent.getStringExtra("status"));
//                        newlog1.setTaskId(intent.getStringExtra("task_id"));
//                        newlog1.setComment(intent.getStringExtra("comment"));
//                        newlog1.setmLogtype(intent.getStringExtra("log_type"));
//                        add_new_message(newlog1);


                    }
//                    else {
//                        update_order_and_count(intent.getStringExtra("task_id"),intent.getStringExtra("status"),Long.parseLong(intent.getStringExtra("unread_count")),true);
//                    }

                }
                // checking for type intent filter

            }
        };



        get_todo_list();
        show_filter();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.attachment);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.status);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.completed);
        item2.setVisible(true);
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(true);
        MenuItem filter = menu.findItem(R.id.filter);
        filter.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!((TaskActivity)getActivity()).searchView.isActivated()) {
            ((TaskActivity)getActivity()).searchView.onActionViewCollapsed();
        }
        switch (item.getItemId())
        {
            case R.id.completed:

                show_completed_list();
                return true;
            case R.id.filter :
                setFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @OnClick(R.id.todo_list)
    public void setTodo_list(View view) {
        AppUtil.logger("Task Activity : ", " Show Todo List");
        todoList.setTextColor(Color.parseColor("#FFFFFF"));
        tasksAsssigned.setTextColor(Color.parseColor("#CCCCCC"));
        show_todo_list();

    }

    @OnClick(R.id.tasks_asssigned)
    public void setTasksAsssigned(View view) {
        todoList.setTextColor(Color.parseColor("#CCCCCC"));
        tasksAsssigned.setTextColor(Color.parseColor("#FFFFFF"));
        show_tasks_assigned();
    }

    private void show_filter() {
        filter_list.add("On Going");
        filter_list.add("Today's Task");
        filter_list.add("Employee-wise");
        filter_list.add("Completed");

        filter_task_adapter = new Filter_task_adapter(filter_list);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filterRecyclerView.setLayoutManager(gridLayoutManager);

        filterRecyclerView.setAdapter(filter_task_adapter);
        filter_task_adapter.notifyDataSetChanged();

    }

    public void get_completed_list()
    {
//        if(completed_list!=null)
//       completed_list.clear();
        for(int i = 0 ;i<todolist.getData1().size();i++)
        {
            if(todolist.getData1().get(i).getStatus().equalsIgnoreCase("Complete"))
            {
                completed_list.add(todolist.getData1().get(i));
            }
        }

    }

    public void show_completed_list()
    {
        show_completed=true;
        tasks_assigned_adapter = new Tasks_assigned_adapter(completed_list, getContext(),Task_home.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(gridLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //tasksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        tasksRecyclerView.setAdapter(tasks_assigned_adapter);
        tasks_assigned_adapter.notifyDataSetChanged();
    }


    public void show_todo_list() {

        todoIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        assignedIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        todolist= new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);

        todo_list_adapter = new Todo_list_adapter(todolist.getData(), getContext(), Task_home.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(gridLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //tasksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        tasksRecyclerView.setAdapter(todo_list_adapter);
        todo_list_adapter.notifyDataSetChanged();



    }


    public void show_tasks_assigned() {
        show_completed=false;
        assignedIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        todoIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    //    Todolist todolist = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);
        todolist= new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);

        tasks_assigned_adapter = new Tasks_assigned_adapter(non_completed_list, getContext(),Task_home.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(gridLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //tasksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        tasksRecyclerView.setAdapter(tasks_assigned_adapter);
        tasks_assigned_adapter.notifyDataSetChanged();

        if (proceed) {
            AppUtil.logger(TAG, "proceed to details");
            proceed=false;

            proceed_to_details(getTaskId, getTaskTitle);
        }
    }


    @OnClick(R.id.fab)
    public void onViewClicked() {


        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_task_type);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        LinearLayout daily = (LinearLayout) dialog.findViewById(R.id.daily);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("daily");

                                    }

                                }
                                );
                            }
                        },
                        400
                );
            }
        });
        LinearLayout letter = (LinearLayout) dialog.findViewById(R.id.letter);
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {

                                getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                dialog.dismiss();
                                ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("letter");
                                                                }

                                                            }
                                );
                            }
                        },
                        400
                );

            }
        });
        LinearLayout meetings = (LinearLayout) dialog.findViewById(R.id.meetings);
        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        dialog.dismiss();
                                        ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("meetings");
                                    }
                                }
                                );
                            }
                        },
                        400
                );
            }
        });
        dialog.show();
    }


    SweetAlertDialog pDialog;


    public void get_todo_list() {

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Call<Todolist> call = apiServices.todo_list(AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        AppUtil.logger("Home Activity ", "Get Todo list : " + call.request().toString() + "User id : " + AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        call.enqueue(new Callback<Todolist>() {
            @Override
            public void onResponse(Call<Todolist> call, Response<Todolist> response2) {


                Todolist todo = new Todolist();
                todo = response2.body();
                AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST, new Gson().toJson(todo));
                // todolist= new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.TODO_LIST), Todolist.class);
                AppUtil.logger("Todo List : ", AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST));
                get_employee_list();

            }


            @Override
            public void onFailure(Call<Todolist> call, Throwable t) {
                empty_relative.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                pDialog.dismiss();
                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });


    }

//     public void get_tasks_assigned()
//     {
//         Call<Todolist> call1 = apiServices.tasks_assigned((AppUtil.getString(context,TagsPreferences.USER_ID)));
//         AppUtil.logger("Home Activity ", "Get TAsks assigned: " + call1.request().toString() + "User id : " + AppUtil.getString(context,TagsPreferences.USER_ID) );
//         call1.enqueue(new Callback<Todolist>() {
//             @Override
//             public void onResponse(Call<Todolist> call1, Response<Todolist> response1) {
//
//                 Todolist todo = new Todolist();
//                 todo = response1.body();
//                 AppUtil.putString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED, new Gson().toJson(todo));
//                 //todolist= new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED), Todolist.class);
//                 AppUtil.logger("Tasks Assigned : ", AppUtil.getString(getApplicationContext(), TagsPreferences.TASKS_ASSIGNED));
//                    c2 = true;
//                 get_employee_list();
//
//             }
//
//
//             @Override
//             public void onFailure(Call<Todolist> call1, Throwable t) {
//
//                 AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
//             }
//         });
//     }

    public void get_employee_list() {

        Call<Employee> call2 = apiServices.fetch_emp(AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        AppUtil.logger("Home Activity ", "Fetch Employee : " + call2.request().toString() + "User id : " + AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        call2.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call2, Response<Employee> response) {

                pDialog.dismiss();
                Employee employee = new Employee();
                employee = response.body();
                AppUtil.putString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST, new Gson().toJson(employee));
                employee_list = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
                AppUtil.logger("List : ", AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST));


               // show_todo_list();
              //  show_tasks_assigned();
                swipeRefreshLayout.setRefreshing(false);
                set_todo_list();
                get_non_completed_task();
                //  proceed();
            }


            @Override
            public void onFailure(Call<Employee> call2, Throwable t) {
                pDialog.dismiss();
                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });

    }

    public void proceed_to_details(String task_id, String title) {
        ((TaskActivity) getContext()).show_task_details(task_id, title);

    }

    @Override
    public boolean onItemLongClicked(int position) {

        show_delete = true;
        tasks_assigned_adapter.notifyDataSetChanged();
       // AppUtil.showToast(getContext(), "Long clicked");
        return false;
    }

    public void stop_animation()
    {
        show_delete=false;
        tasks_assigned_adapter.notifyDataSetChanged();

    }




    public interface OnTaskTypeSelectListener {
        public void OnTaskSelected(String type);
    }



    public void delete_task(final String task_id , final int position)
    {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<ResponseBody> call = apiServices.delete_task(AppUtil.getString(getContext(),TagsPreferences.USER_ID),task_id);
        AppUtil.logger("Login Activity ", "Update Password : " + call.request().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pDialog.dismiss();
                try {

                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        AppUtil.logger("Delete Task response : ", obj.toString());
                       tasks_assigned_adapter.removeAt(position);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call1, Throwable t) {

                pDialog.dismiss();
                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void get_non_completed_task()
    {

        todolist= new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);

        if(non_completed_list!=null)
            non_completed_list.clear();
        for(int i = 0 ;i<todolist.getData1().size();i++)
        {
            if(!todolist.getData1().get(i).getStatus().equalsIgnoreCase("Complete"))
            {
                non_completed_list.add(todolist.getData1().get(i));
            }
        }
        get_completed_list();
        show_tasks_assigned();


    }

    public void set_todo_list()
    {

        todolist= new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);

        if(todo_list!=null)
            todo_list.clear();
        for(int i = 0 ;i<todolist.getData1().size();i++)
        {
            todo_list.add(todolist.getData1().get(i));
        }

        if(todo_list.size() == 0) {
            empty_relative.setVisibility(View.VISIBLE);
        }
        else {
            empty_relative.setVisibility(View.GONE);
        }



    }

    public void setRecyclerSearch(String titleQuery) {
        AppUtil.logger(TAG,titleQuery);
        List<Todolist.Data1> searchList = new ArrayList<>();
        for(int i = 0 ; i < todo_list.size() ; i++) {
            if(todo_list.get(i).getTitle().toLowerCase().contains(titleQuery.toLowerCase())) {
                searchList.add(todo_list.get(i));
            }
        }
        AppUtil.logger(TAG,searchList.size()+"");
        setTodoListRecycler(searchList);

    }

    private void setTodoListRecycler(List<Todolist.Data1> list) {
        tasks_assigned_adapter = new Tasks_assigned_adapter(list, getContext(),Task_home.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(gridLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //tasksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        tasksRecyclerView.setAdapter(tasks_assigned_adapter);
        tasks_assigned_adapter.notifyDataSetChanged();

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            deadline_button.setText(sdf.format(myCalendar.getTime()));
        }

    };

    private void setFilterDialog() {

        final Dialog filterDialog = new Dialog(getActivity());
        View filterDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter_todolist,null);

        // initializing dialog view

        // Status
        final Spinner status_spinner = (Spinner) filterDialogView.findViewById(R.id.status_spinner);
        final String[] status_types = getActivity().getResources().getStringArray(R.array.task_status_filter);
        ArrayAdapter<String> status_adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_text,
                status_types);
        status_spinner.setAdapter(status_adapter);


        // Employee
        final List<String> employees = new ArrayList<>();
        final List<String> employeeIds = new ArrayList<>();
        employees.add("All");
        employeeIds.add("All");
        for(int i = 0 ; i < todo_list.size() ; i++) {
            int j;
            for(j = 0 ; j < employeeIds.size() ; j++) {
                if(todo_list.get(i).getToUser().equals(employeeIds.get(j))) {
                    break;
                }
            }
            if(j != employeeIds.size()) {
                continue;
            }
            for(j = 0 ; j < employee_list.getData().size() ; j++) {
                if(todo_list.get(i).getToUser().equals(employee_list.getData().get(j).getUserId())) {
                    employees.add(employee_list.getData().get(j).getUserName());
                    employeeIds.add(employee_list.getData().get(j).getUserId());
                    break;
                }
            }
        }
        final Spinner employee_spinner = (Spinner) filterDialogView.findViewById(R.id.employee_spinner);
        ArrayAdapter<String> employee_adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_text,
                employees);
        employee_spinner.setAdapter(employee_adapter);


        // Deadline Date
        deadline_button = (Button) filterDialogView.findViewById(R.id.deadline_button);

        deadline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Submit
        Button submit_filters = (Button) filterDialogView.findViewById(R.id.submit_filters);

        submit_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = null,employee = null,deadline = null;
                if(!(status_spinner.getSelectedItemPosition() == 0)) {
                    status = status_types[status_spinner.getSelectedItemPosition()];
                }
                if(!(employee_spinner.getSelectedItemPosition() == 0)) {
                    employee = employeeIds.get(employee_spinner.getSelectedItemPosition());
                }

                if(deadline_button.getText().toString().length() > 1 && (!deadline_button.getText().toString()
                        .equalsIgnoreCase("Select Date"))) {
                    deadline = deadline_button.getText().toString();
                }
                filterDialog.dismiss();
                applyFilters(status,employee,deadline);
            }
        });


        // setting dialog view
        filterDialog.setContentView(filterDialogView);
        filterDialog.show();
    }

    private void applyFilters(String status,String emplopyee, String deadline) {
        List<Todolist.Data1> filtered_list = new ArrayList<>();
        for(int i = 0 ; i < todo_list.size() ; i++) {

            if(status != null) {
                if(!(todo_list.get(i).getStatus().equalsIgnoreCase(status))) {
                    continue;
                }
            }
            if(emplopyee != null) {
                if(!(todo_list.get(i).getToUser().equalsIgnoreCase(emplopyee))) {
                    continue;
                }
            }
            if(deadline != null ) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));

                    Date date = format.parse(todo_list.get(i).getDeadline());
                    long millisTodo = date.getTime();

                    format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    date = format.parse(deadline);
                    long millisFilter = date.getTime();

                    if(millisTodo > millisFilter) {
                        continue;
                    }

                }catch (Exception e) {
                    AppUtil.logger(TAG,e.toString());
                }
                AppUtil.logger(TAG,todo_list.get(i).getDeadline());
            }
            AppUtil.logger(TAG,"HIT");
            filtered_list.add(todo_list.get(i));
        }
        setTodoListRecycler(filtered_list);
    }

    public void setStatus(String task_id ,String status)
    {
        AppUtil.logger(TAG,"set status change");
//        Boolean flag=false;
//        for(int i=0;i<todolist.getData().size();i++)
//        {
//            if(todolist.getData().get(i).getTask_id().equalsIgnoreCase(task_id))
//            {
//                flag=true;
//                todolist.getData().get(i).setStatus(status);
//                todo_list_adapter.notifyDataSetChanged();
//            }
//        }
//        if(flag==false)
//        {
            for(int i=0;i<non_completed_list.size();i++)
            {
                if(non_completed_list.get(i).getTask_id().equalsIgnoreCase(task_id))
                {
//                    flag=true;
                    non_completed_list.get(i).setStatus(status);
                    if(status.equalsIgnoreCase("Complete"))
                    {
                        completed_list.add(0,non_completed_list.get(i));
                        non_completed_list.remove(i);
                    }
                    tasks_assigned_adapter.notifyDataSetChanged();


                }
            }

        for(int i=0;i<todolist.getData1().size();i++)
        {
            if(todolist.getData1().get(i).getTask_id().equalsIgnoreCase(task_id))
            {
//                    flag=true;
                todolist.getData1().get(i).setStatus(status);


            }
        }

        }

        public void update_order(String task_id,String status,Long unread_count,Boolean update_order)
        {
            for(int i=0;i<non_completed_list.size();i++)
            {
                if(non_completed_list.get(i).getTask_id().equalsIgnoreCase(task_id))
                {
//                    flag=true;

                    non_completed_list.get(i).setStatus(status);
                    Todolist.Data1 task = non_completed_list.get(i);
                    if(status.equalsIgnoreCase("Complete"))
                    {
                        non_completed_list.get(i).setUnread_count((long) 0);
                        completed_list.add(non_completed_list.get(i));
                        tasks_assigned_adapter.removeAt(i);
                        break;
                    }
                    else {
                        if(update_order) {
                            tasks_assigned_adapter.removeAt(i);
                            non_completed_list.add(0, task);
                            non_completed_list.get(0).setUnread_count(unread_count);
                            break;
                        }
                        else {
                            non_completed_list.get(i).setUnread_count(unread_count);
                            break;
                        }
                    }


                }
            }

            tasks_assigned_adapter.notifyDataSetChanged();


        }



}

