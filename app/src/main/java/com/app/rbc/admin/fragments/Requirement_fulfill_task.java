package com.app.rbc.admin.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.RequirementDetailActivity;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockListProductWise;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.utils.AdapterWithCustomItem;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.MySpinner;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.dd.processbutton.iml.ActionProcessButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Requirement_fulfill_task#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Requirement_fulfill_task extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "Requirement_fulfill_task";
    @BindView(R.id.employee_for_task)
    TextView employeeForTask;
    @BindView(R.id.requirement_category)
    TextView requirementCategory;
    @BindView(R.id.from_site)
    TextView fromSite;
    @BindView(R.id.to_site)
    TextView toSite;
    @BindView(R.id.product_table)
    TableLayout productTable;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.date_select)
    MySpinner dateSelect;
    @BindView(R.id.time_select)
    MySpinner timeSelect;
    @BindView(R.id.submit_task)
    ActionProcessButton submitTask;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String category_selected;
    private String user_selected;
    int count;

    private String to_user, from_user, product, deadline;
    private int qty;
    String deadline_date = "", deadline_time = "";
    JSONArray prod_list = new JSONArray();

    private String task_type;

    AdapterWithCustomItem dateAdapter;
    AdapterWithCustomItem timeAdapter;
    String date_shown, time_shown;

    private long state_store_id;
    public Requirement_fulfill_task() {
    }

    public static Requirement_fulfill_task newInstance(String param1, String param2,Requirement state_store_req) {
        Requirement_fulfill_task fragment = new Requirement_fulfill_task();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        state_store_req = state_store_req;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_selected = getArguments().getString(ARG_PARAM1);
            user_selected = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requirement_fulfill_task, container, false);
        unbinder = ButterKnife.bind(this, view);
        count =1;

        Bundle bundle = ((RequirementDetailActivity)getActivity()).finalform;
        if(bundle != null) {
            desc.setText(bundle.getString("description"));
            dateSelect.setSelection(bundle.getInt("dateselect"));
            timeSelect.setSelection(bundle.getInt("timeselect"));
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Assign a Task");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_data();
        spinner_values();

    }

    @Override
    public void onPause() {
        if(((RequirementDetailActivity)getActivity()).finalform == null) {
            ((RequirementDetailActivity)getActivity()).finalform = new Bundle();
        }
        Bundle bundle = ((RequirementDetailActivity) getActivity()).finalform;
        bundle.putString("description", desc.getText().toString());
        bundle.putInt("dateselect", dateSelect.getSelectedItemPosition());
        bundle.putInt("timeselect", timeSelect.getSelectedItemPosition());

        super.onPause();
    }


    private void set_data() {
        String[] user = AppUtil.get_employee_from_user_id(getContext(), user_selected);
        employeeForTask.setText(user[0]);
        requirementCategory.setText(category_selected);
        fromSite.setText(Stock_list_product_wise.site_name);
        toSite.setText(RequirementDetailActivity.req_site_name);

        try {

            for (String key : Product_selection.product_grid.keySet()) {
                JSONObject product1 = new JSONObject();
                product1.put("product", key);
                product1.put("quantity", Integer.parseInt(Product_selection.product_grid.get(key)));
                prod_list.put(product1);
                addrow(key,Product_selection.product_grid.get(key));
            }
            AppUtil.logger("Product list : ", prod_list.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addrow(String product,String quantity)
    {

        String unit = "";
        List<Categoryproduct> categoryproductList = Categoryproduct.find(Categoryproduct.class,
                "product = ?",product);
        if(categoryproductList.size() != 0) {

            unit = categoryproductList.get(0).getUnit();
        }

        View tr = getActivity().getLayoutInflater().inflate(R.layout.custom_requirement_table_row,null);

        TextView productText = (TextView) tr.findViewById(R.id.product);
        TextView quantityText = (TextView) tr.findViewById(R.id.quantity);
        Button product_icon = (Button) tr.findViewById(R.id.product_icon);

        productText.setText(product);
        quantityText.setText(quantity+" "+unit);
        product_icon.setText(product.substring(0,1));

        productTable.addView(tr);
        count++;
    }
    private Boolean check_length(EditText field) {
        if (field.getText().toString().length() == 0) {
            desc.setError("Enter Task Description");
            return false;
        } else
            return true;
    }


    private Boolean verify() {

        deadline = deadline_date + " " + deadline_time;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;

        Boolean flag = false;

        try {
            date1 = fmt.parse(deadline);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Timestamp(date1.getTime()).before(new Timestamp(new Date().getTime()))) {
            AppUtil.showToast(getContext(), "Choose correct time");
            flag= false;

        } else {

            if(check_length(desc))
            {
                flag = true;
            }
            else
                flag=false;


        }



        return flag;


    }

    public void spinner_values() {


        String[] dates = {"Today ", "Tomorrow ", "Select Date"};


        dateAdapter = new AdapterWithCustomItem(getContext(), dates);
        dateAdapter.setDropDownViewResource(R.layout.custom_spinner_text);
        dateSelect.setAdapter(dateAdapter);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        deadline_date = strDate;
        AppUtil.logger(TAG, "Current Date : " + strDate);


        dateSelect.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2) {
                    show_date_selector();
                }

                if (position == 0) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = mdformat.format(calendar.getTime());
                    deadline_date = strDate;
                    AppUtil.logger(TAG, "Current Date : " + strDate);
                    date_shown = "Today";
                }
                if (position == 1) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    String strDate = mdformat.format(calendar.getTime());
                    deadline_date = strDate;
                    date_shown = "Tomorrow";
                    AppUtil.logger(TAG, "Current Date : " + strDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] times = {"Noon 1:00 pm", "Evening 7:00 pm", "Select Time"};

        deadline_time = "13:00:00";

        timeAdapter = new AdapterWithCustomItem(getContext(), times);
        timeAdapter.setDropDownViewResource(R.layout.custom_spinner_text);
        timeSelect.setAdapter(timeAdapter);
        timeSelect.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // AppUtil.showToast(getContext(),parent.getItemAtPosition(position).toString());

                if (position == 2) {
                    show_time_selector();
                }
                if (position == 0) {
                    deadline_time = "13:00:00";
                    time_shown = "Noon 1:00 pm";
                }
                if (position == 1) {
                    deadline_time = "19:00:00";
                    time_shown = "Evening 7:00 pm";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void show_time_selector() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                Requirement_fulfill_task.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);

        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                timeAdapter.setCustomText(time_shown);
            }
        });
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");

    }

    public void show_date_selector() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Requirement_fulfill_task.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);

        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
                dateAdapter.setCustomText(date_shown);

            }
        });

        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateAdapter.setCustomText(dayOfMonth + "/" + (++monthOfYear) + "/" + year);
        date_shown = "" + dayOfMonth + "/" + (monthOfYear) + "/" + year;
        deadline_date = "" + year + "-" + monthOfYear + "-" + dayOfMonth;
        AppUtil.logger(TAG, "Date : " + deadline_date);


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay <= 12 ? "" + hourOfDay : "" + (hourOfDay - 12);
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String amorpm = hourOfDay < 12 ? "am" : "pm";
        Log.d("Task_create ", "Hour of the day : " + hourOfDay);
        time_shown = "" + hourString + ":" + minuteString + "  " + amorpm;

        timeAdapter.setCustomText(hourString + ":" + minuteString + "  " + amorpm);
        deadline_time = hourOfDay + ":" + minute + ":" + second;
        AppUtil.logger(TAG, hourOfDay + ":" + minute + ":" + second);
    }


    @OnClick(R.id.submit_task)
    public void onViewClicked() {

        if (verify()) {
            submitTask.setProgress(1);
            submitTask.setEnabled(false);
            from_user = AppUtil.getString(getContext(), TagsPreferences.USER_ID);
            deadline = deadline_date + " " + deadline_time;
            to_user = user_selected;
            product = category_selected;

//            qty = Integer.parseInt(quantityForPo.getText().toString());



            final ApiServices apiServices = RetrofitClient.getApiService();
            Call<ResponseBody> call = apiServices.fulfill_req(RequirementDetailActivity.rq_id,RequirementDetailActivity.req_site_id, Stock_list_product_wise.site_selected,to_user, from_user, product, prod_list,desc.getText().toString(), deadline);

            AppUtil.logger(TAG, "Creation Request : " + call.request().toString() + " Product :" + prod_list + " \n " + " Employee ID  :" + to_user + " \n " + "Assigned by :" + from_user + " \n " + "Deadline :" + deadline + " \n ");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    // progress.dismiss();


                    submitTask.setEnabled(true);
                    submitTask.setProgress(0);

                    try {

                        try {
                            JSONObject obj = new JSONObject(response.body().string());
                            AppUtil.logger(TAG, obj.toString());
                            if(obj.getJSONObject("meta").getInt("status")==2)
                            {
                                final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Task Created");
                                pDialog.setContentText("Your task has been successfully created");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
//                                        while (getFragmentManager().getBackStackEntryCount() != 0) {
//                                            getFragmentManager().popBackStackImmediate();
//                                        }

                                        getActivity().getSupportFragmentManager().popBackStack(null,
                                                getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

                                        ChangeFragment.changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, com.app.rbc.admin.fragments.RequirementDetails.newInstance(category_selected), com.app.rbc.admin.fragments.RequirementDetails.TAG);


                                    }
                                });
                            }
                            else
                                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(Call<ResponseBody> call1, Throwable t) {
                    //progress.dismiss();
                    submitTask.setEnabled(true);
                    submitTask.setProgress(0);
                    AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
                }
            });


        }
    }
}
