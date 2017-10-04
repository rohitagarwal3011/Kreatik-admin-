package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.Manifest;
import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.adapters.Employee_list_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.utils.AdapterWithCustomItem;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.Compress;
import com.app.rbc.admin.utils.FileUtils;
import com.app.rbc.admin.utils.MySpinner;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.dd.processbutton.iml.ActionProcessButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Task_create extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TASK_TYPE = "task_type";
    private static final String TO_USER = "TO_USER";
    public static final String TAG = "Task_create";
    public static final int fragment = 1;
    @BindView(R.id.form_card)
    CardView formCard;
    @BindView(R.id.submit_button_layout)
    LinearLayout submitButtonLayout;
    private int function;
    @BindView(R.id.emp_select)
    Spinner empSelect;
    @BindView(R.id.task_title)
    EditText taskTitle;
    @BindView(R.id.task_desc)
    EditText taskDesc;
    @BindView(R.id.text_attachment)
    TextView textAttachment;
    @BindView(R.id.button_attachment)
    Button buttonAttachment;
    @BindView(R.id.name_attachment)
    TextView nameAttachment;
    @BindView(R.id.remove_attachment)
    ImageView removeAttachment;
    @BindView(R.id.card_attachment)
    CardView cardAttachment;
    @BindView(R.id.date_select)
    MySpinner dateSelect;
    @BindView(R.id.time_select)
    MySpinner timeSelect;
    @BindView(R.id.submit_task)
    ActionProcessButton submitTask;
    Unbinder unbinder;
    @BindView(R.id.deadline_title)
    TextView deadLine_text;
    @BindView(R.id.select_employee)
    RecyclerView selectEmployee;
    @BindView(R.id.task_details_page)
    RelativeLayout taskDetailsPage;

    Employee_list_adapter employee_list_adapter;

    private String type, to_user, from_user, title, desc, deadline, docs;
    String deadline_date = "", deadline_time = "";

    private String task_type;

    AdapterWithCustomItem dateAdapter;
    AdapterWithCustomItem timeAdapter;
    private View rootview;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private File fileUri;

    String date_shown, time_shown;
    File attactment;
    public static Boolean details_page = false;

    String toolbar_string = "";

    Toolbar toolbar;

    public Task_create() {
        // Required empty public constructor
    }


    public static Task_create newInstance(String task_type, String to_user) {
        Task_create fragment = new Task_create();
        Bundle args = new Bundle();
        args.putString(TASK_TYPE, task_type);
        args.putString(TO_USER, to_user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task_type = getArguments().getString(TASK_TYPE);
            to_user = getArguments().getString(TO_USER);
        }

        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
        TaskActivity.visible_fragment = TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //AppUtil.showToast(getContext(),"Task type : "+task_type);
        rootview = inflater.inflate(R.layout.fragment_task_create, container, false);
//        toolbar = (Toolbar)rootview.findViewById(R.id.toolbar);
//        toolbar.setTitle("Select Employee ");


        unbinder = ButterKnife.bind(this, rootview);

        submitTask.setMode(ActionProcessButton.Mode.ENDLESS);

        details_page = true;
        selectEmployee.setVisibility(View.GONE);
        taskDetailsPage.setVisibility(View.VISIBLE);
        updateview(task_type);

        return rootview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_values();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.attachment);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.status);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.completed);
        item2.setVisible(false);
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);
        MenuItem filter = menu.findItem(R.id.filter);
        filter.setVisible(false);
    }


    private RequestBody StringtoRequestBody(String param) {
        RequestBody description =
                RequestBody.create(
                        MultipartBody.FORM, param);
        return description;
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


        if (check_length(taskTitle) && check_length(taskDesc)) {
            if (task_type.equalsIgnoreCase("Letter")) {
                if (cardAttachment.getVisibility() == View.VISIBLE) {
                    flag = true;
                } else {
                    AppUtil.showToast(getContext(), "Please add and Attachment");
                    flag = false;
                }
            } else
                flag = true;


        } else {
            if (!check_length(taskTitle)) {
                taskTitle.setError("Please enter a title");
                taskTitle.requestFocus();
            } else {
                taskDesc.setError("Please enter description");
                taskDesc.requestFocus();
            }
            flag = false;
        }


        if (new Timestamp(date1.getTime()).before(new Timestamp(new Date().getTime()))) {
            AppUtil.showToast(getContext(), "Choose correct time");
            flag = false;
        }


        return flag;


    }

    private Boolean check_length(EditText field) {
        if (field.getText().toString().length() == 0) {
            return false;
        } else
            return true;
    }


    public void askPermission(int code, int function) {
        this.function = function;
        switch (code) {
            case 120:
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            code);

                } else {

                    switch (function) {
                        case 1:
                            loadImagefromGallery();
                            break;
                        case 2:
                            captureImage();
                            break;
                        case 3:
                            onBrowse();
                            break;
                    }
                    break;


                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("Permisson", "callback");

        switch (requestCode) {
            case 120: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    switch (function) {
                        case 1:
                            loadImagefromGallery();
                            break;
                        case 2:
                            captureImage();
                            break;
                        case 3:
                            onBrowse();
                            break;
                    }
                    break;


                } else {

                    Toast.makeText(getContext(),
                            "Permission Denied!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    @OnClick({R.id.button_attachment, R.id.remove_attachment, R.id.submit_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_attachment:

                final Dialog dialog = new Dialog(getContext());

                // dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                dialog.setTitle("Choose option");
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_add_attachment);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                ImageView gallery, camera, pdf;

                gallery = (ImageView) dialog.findViewById(R.id.gallery);
                camera = (ImageView) dialog.findViewById(R.id.camera);
                pdf = (ImageView) dialog.findViewById(R.id.pdf);


                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askPermission(120, 1);

                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askPermission(120, 2);

                        dialog.dismiss();
                    }
                });

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askPermission(120, 3);
                        dialog.dismiss();
                    }
                });

                dialog.show();


                break;
            case R.id.remove_attachment:

                cardAttachment.setVisibility(View.GONE);
                buttonAttachment.setVisibility(View.VISIBLE);
                break;
            case R.id.submit_task:

                if (verify()) {

                    submitTask.setProgress(1);
                    submitTask.setEnabled(false);

//                    final ProgressDialog progress = new ProgressDialog(getContext().getApplicationContext());
//                    progress.setMessage("Your task is being created");
//                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progress.show();

                    type = ("" + task_type.charAt(0)).toUpperCase();
                    from_user = AppUtil.getString(getContext(), TagsPreferences.USER_ID);
                    title = taskTitle.getText().toString();
                    desc = taskDesc.getText().toString();
                    deadline = deadline_date + " " + deadline_time;

                    MultipartBody.Part body;
                    if (type.equalsIgnoreCase("L")) {
                        docs = attactment.getName();

                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse("application/pdf"),
                                        attactment
                                );

                        // MultipartBody.Part is used to send also the actual file name
                        body =
                                MultipartBody.Part.createFormData("myfile", attactment.getName(), requestFile);


                    } else {
                        docs = "";
                        body = null;

                    }
                    final ApiServices apiServices = RetrofitClient.getApiService();
                    Call<ResponseBody> call = apiServices.create_task(StringtoRequestBody(type), StringtoRequestBody(to_user), StringtoRequestBody(from_user), StringtoRequestBody(title), StringtoRequestBody(desc), StringtoRequestBody(deadline), StringtoRequestBody(docs), body);

                    AppUtil.logger(TAG, "Creation Request : " + call.request().toString() + " Task type :" + type + " \n " + " Employee ID  :" + to_user + " \n " + "Assigned by :" + from_user + " \n " + "title :" + title + " \n " + "Description :" + desc + " \n " + "Deadline :" + deadline + " \n ");
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
                                    if (obj.getJSONObject("meta").getInt("status") == 2) {
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
                                                Task_home task_home = new Task_home();
                                                getActivity().getSupportFragmentManager().popBackStack(null,
                                                        getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                                                ((TaskActivity) getContext()).setToolbar("Tasks");
                                                ((TaskActivity) getContext()).setFragment(task_home, Task_home.TAG);

                                            }
                                        });
                                    } else {
                                        AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
                                    }
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


                break;
        }
    }

    public void show_attachment_card(String document_name) {
        nameAttachment.setText(document_name);
        cardAttachment.setVisibility(View.VISIBLE);
        buttonAttachment.setVisibility(View.GONE);
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
                Task_create.this,
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
                Task_create.this,
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


    public void updateview(String type) {
        switch (type) {
            case "daily":
                toolbar_string = "Task Details";
                textAttachment.setVisibility(View.GONE);
                buttonAttachment.setVisibility(View.GONE);
                cardAttachment.setVisibility(View.GONE);
                formCard.setVisibility(View.GONE);

                break;
            case "letter":
                toolbar_string = "Letter Details";
                textAttachment.setVisibility(View.VISIBLE);
                buttonAttachment.setVisibility(View.VISIBLE);
                formCard.setVisibility(View.VISIBLE);
                break;
            case "meetings":
                toolbar_string = "Meeting Details";
                deadLine_text.setText("Time to go");
                textAttachment.setVisibility(View.GONE);
                buttonAttachment.setVisibility(View.GONE);
                cardAttachment.setVisibility(View.GONE);
                formCard.setVisibility(View.GONE);
                break;
        }
        ((TaskActivity) getContext()).setToolbar(toolbar_string);
        // set_employee_list();

    }


//    public void set_employee_list()
//    {
//        Employee employee = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
//
//        employee_list_adapter = new Employee_list_adapter(employee.getData(), getContext(),Task_create.TAG);
//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        selectEmployee.setLayoutManager(gridLayoutManager);
//        selectEmployee.setItemAnimator(new DefaultItemAnimator());
//        selectEmployee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        selectEmployee.setAdapter(employee_list_adapter);
//
//        employee_list_adapter.notifyDataSetChanged();
//    }


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


    //this when button click
    public void onBrowse() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        getActivity().startActivityForResult(intent, RESULT_LOAD_PDF);
    }

    private static int RESULT_LOAD_PDF = 2;


    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    Bitmap bitmap;

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            // when image is clicked
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // display it in image view
                    AppUtil.logger("Task_create", "Image Path : " + fileUri.getPath());
                    compress_create_pdf_and_show_card(fileUri.getPath());
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity().getApplicationContext(),
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            }


            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                compress_create_pdf_and_show_card(imgDecodableString);


            } else if (requestCode == RESULT_LOAD_PDF) {
                if (resultCode == RESULT_OK) {

                    AppUtil.logger(TAG, " PDF SELECTED");
                    Uri uri = data.getData();

                    InputStream newfile = getContext().getContentResolver().openInputStream(uri);
                    byte[] buffer = new byte[newfile.available()];
                    newfile.read(buffer);

                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "Kreatik/Sent_Attachments");

                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File pdf_created = new File(file.getAbsolutePath(), "/" + System.currentTimeMillis() + ".pdf");
                    OutputStream outStream = new FileOutputStream(pdf_created);
                    outStream.write(buffer);
//                    File file = FileUtils.getFile(this, fileUri);


//                    String FilePath = getRealPathFromURI(uri); // should the path be here in this string
                    AppUtil.logger(TAG, "Path  = " + pdf_created.getAbsolutePath());
                    attactment = pdf_created;
                    show_attachment_card(pdf_created.getAbsolutePath().substring(pdf_created.getAbsolutePath().lastIndexOf("/") + 1));


                }

            } else {
                AppUtil.showToast(getContext(), "You haven't selected any image");

            }


        } catch (Exception e) {

            AppUtil.showToast(getContext(), "Something went wrong");
            AppUtil.logger("Task_create", e.toString());
        }

    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void compress_create_pdf_and_show_card(String image) {
        try {
//            Compress compress = new Compress();
//            String image_returned = compress.compressImage(image);
//
//            Document document = new Document();
//            String dirpath = Environment.getExternalStorageDirectory().toString();
//            File file = new File(Environment.getExternalStorageDirectory().getPath(), "Kreatik/Sent_Attachments");
//
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//
//            File pdf_created = new File(file.getAbsolutePath(), "/" + System.currentTimeMillis() + ".pdf");
//
//            PdfWriter.getInstance(document, new FileOutputStream(pdf_created)); //  Change pdf's name.
//            document.open();
////                AppUtil.logger("Task_create","DirPath :"+dirpath);
////                AppUtil.logger("Task_create","URIPath :"+selectedImage.getPath());
//            //AppUtil.logger("Task_create ","Image path"+imgDecodableString);
//            Image img = Image.getInstance(image_returned);  // Change image's name and extension.
//
//            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                    - document.rightMargin() - 0) / img.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
//            img.scalePercent(scaler);
//            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
//            document.add(img);
//            document.close();
//            AppUtil.logger("Task_create", "Compressed Image Path : " + image_returned);
//            AppUtil.logger("Task_create", "PDF path " + pdf_created.getAbsolutePath());
//
//            attactment = pdf_created;

            Compress compress = new Compress();
            String image_returned = compress.compressImage(image);
            AppUtil.logger("Returned path : ", image_returned);
            Uri fileUri = Uri.parse(image_returned);
            AppUtil.logger("Uri of image ", fileUri.toString());
            File pdf_created = new File(image_returned);

            show_attachment_card(pdf_created.getAbsolutePath().substring(pdf_created.getAbsolutePath().lastIndexOf("/") + 1));
        } catch (Exception e) {

        }
    }


    public void captureImage() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            fileUri = null;
            try {
                fileUri = FileUtils.createImageFile();
            } catch (IOException ex) {
                Log.e("Vehicle Recieved", ex.toString());

            }

            if (fileUri != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        fileUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        }

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        fileUri = FileUtils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//        // start the image capture Intent
//        getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        if (fileUri != null) {
            outState.putString("file_uri", fileUri.getAbsolutePath());
        }
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        //super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }


//    public void onBackPressed()
//    {
//
//      taskDetailsPage.setVisibility(View.GONE);
//        selectEmployee.setVisibility(View.VISIBLE);
//        ((TaskActivity)getContext()).setToolbar("Select Employee");
//        details_page=false;
//        set_employee_list();
//
//    }

}




