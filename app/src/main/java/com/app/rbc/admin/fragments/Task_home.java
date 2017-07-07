package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.adapters.Filter_task_adapter;
import com.app.rbc.admin.adapters.Tasks_assigned_adapter;
import com.app.rbc.admin.adapters.Todo_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Task_home extends Fragment implements Todo_list_adapter.OnItemLongClickListener {

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


    public static Employee employee_list;
    public static Todolist todolist;
    private String getTaskId, getTaskTitle;
    final ApiServices apiServices = RetrofitClient.getApiService();

    public static boolean show_delete = false;

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
        return rootview;
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
    }

    //    Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.attachment);
//        item.setVisible(false);
//        MenuItem item1 = menu.findItem(R.id.search);
//        item1.setVisible(false);
//    }

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
        if (getArguments() != null) {
            AppUtil.logger(TAG, "proceed to details");


            proceed_to_details(getTaskId, getTaskTitle);
        }


    }


    public void show_tasks_assigned() {
        assignedIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        todoIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    //    Todolist todolist = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.TASK_LIST), Todolist.class);

        tasks_assigned_adapter = new Tasks_assigned_adapter(todolist.getData1(), getContext(),Task_home.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(gridLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //tasksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        tasksRecyclerView.setAdapter(tasks_assigned_adapter);
        tasks_assigned_adapter.notifyDataSetChanged();
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

                                dialog.dismiss();
                                ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("daily");
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

                                dialog.dismiss();
                                ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("letter");
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

                                dialog.dismiss();
                                ((OnTaskTypeSelectListener) getActivity()).OnTaskSelected("meetings");
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


                show_todo_list();


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

    public void setStatus(String task_id ,String status)
    {
        AppUtil.logger(TAG,"set status change");
        Boolean flag=false;
        for(int i=0;i<todolist.getData().size();i++)
        {
            if(todolist.getData().get(i).getTask_id().equalsIgnoreCase(task_id))
            {
                flag=true;
                todolist.getData().get(i).setStatus(status);
                todo_list_adapter.notifyDataSetChanged();
            }
        }
        if(flag==false)
        {
            for(int i=0;i<todolist.getData1().size();i++)
            {
                if(todolist.getData1().get(i).getTask_id().equalsIgnoreCase(task_id))
                {
                    flag=true;
                    todolist.getData1().get(i).setStatus(status);
                    tasks_assigned_adapter.notifyDataSetChanged();


                }
            }

        }

    }

}

