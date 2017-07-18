package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.adapters.Task_log_adapter;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Tasklogs;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.Compress;
import com.app.rbc.admin.utils.Constant;
import com.app.rbc.admin.utils.FileUtils;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
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


public class Task_details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.show_deadline)
    TextView showDeadline;

    String deadline;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static final String TAG = "Task_details";
    @BindView(R.id.task_chat)
    RecyclerView taskChat;
    Unbinder unbinder;
    @BindView(R.id.comment)
    EditText comment_text;
    @BindView(R.id.send_comment)
    ImageView sendComment;
    // TODO: Rename and change types of parameters
    private String task_id ="task_id";
    private String mParam2;

    SweetAlertDialog pDialog;
    Context context;
    final ApiServices apiServices = RetrofitClient.getApiService();
    File attactment;
    Image image;
    String current_status;
    Menu bar_menu =null;

    Task_log_adapter task_log_adapter;

    private String log_type,cmt,status,docs,changed_time,docs_url;
    private MultipartBody.Part body;


    public Task_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Task_details.
     */
    // TODO: Rename and change types and number of parameters
    public static Task_details newInstance(String param1) {
        Task_details fragment = new Task_details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            task_id = getArguments().getString(ARG_PARAM1);
        }
        context = getContext();
        setHasOptionsMenu(true);
        AppUtil.logger(TAG, "Created");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
//            case android.R.id.home :
//                AppUtil.logger(TAG,"Home pressed");
//                onBackPressed();
//                return true;
            case R.id.attachment:
                show_attachment_dialog();

                return true;
            case R.id.status:
//                if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
//                    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
//                }

               set_status();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        bar_menu=menu;
        MenuItem item = menu.findItem(R.id.attachment);
        item.setVisible(true);
        MenuItem item1 = menu.findItem(R.id.status);
        item1.setVisible(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {

                    if (intent.getStringExtra("task_id").equalsIgnoreCase(task_id)) {




                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        Tasklogs.Log newlog1 = new Tasklogs().new Log();
                        newlog1.setChangedBy(intent.getStringExtra("changed_by"));
                        newlog1.setChangeTime(intent.getStringExtra("change_time"));
                        newlog1.setDocs(intent.getStringExtra("docs"));
                        newlog1.setStatus(intent.getStringExtra("status"));
                        newlog1.setTaskId(intent.getStringExtra("task_id"));
                        newlog1.setComment(intent.getStringExtra("comment"));
                        newlog1.setmLogtype(intent.getStringExtra("log_type"));
                        add_new_message(newlog1);


                    }

                }
                // checking for type intent filter

            }
        };

        show_task_details();

        comment_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtil.showToast(context, "Text Selected");
                taskChat.smoothScrollToPosition(taskChat.getAdapter().getItemCount());
                //taskChat.scrollToPosition(logs.size() - 1);
                return false;
            }
        });
    }

    List<Tasklogs.Log> logs;

    private void hide_status_icon()
    {
        MenuItem item = bar_menu.findItem(R.id.status);
        item.setVisible(false);
        MenuItem item1 = bar_menu.findItem(R.id.attachment);
        item1.setVisible(false);
    }

    public void show_task_details() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Call<Tasklogs> call = apiServices.task_details(task_id,AppUtil.getString(context,TagsPreferences.USER_ID));
        AppUtil.logger(TAG, "Get Task details : " + call.request().toString() + "Task ID : " + task_id);
        call.enqueue(new Callback<Tasklogs>() {
            @Override
            public void onResponse(Call<Tasklogs> call, Response<Tasklogs> response) {

                pDialog.dismiss();
                logs = response.body().getLogs();
                current_status = response.body().getData().get(0).getStatus();
                update_status();
                deadline=response.body().getData().get(0).getDeadline().replace('T',' ');

//                String date = deadline.substring(0,deadline.indexOf('T'));
//                String time = deadline.substring(deadline.indexOf('T')+1);

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date formated = fmt.parse(deadline);
                    SimpleDateFormat fmtout = new SimpleDateFormat("EEE, dd MMM h:mm a");
                    SimpleDateFormat timeout = new SimpleDateFormat("h:mm a");

                    AppUtil.logger("Final date : ", fmtout.format(formated));




//                    String dayOfTheWeek = (String) DateFormat.format("EE", formated); // Thursday
//                    String day          = (String) DateFormat.format("dd",   formated); // 20
//                    String monthString  = (String) DateFormat.format("MMM",  formated); // Jun
//                    String monthNumber  = (String) DateFormat.format("MM",   formated); // 06
//                    String year         = (String) DateFormat.format("yyyy", formated); // 2013

                    deadline= "Deadline : "+fmtout.format(formated) ;
                    showDeadline.setText(deadline);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Tasklogs.Log newlog = new Tasklogs().new Log();
                newlog.setChangedBy(response.body().getData().get(0).getFromUser());
                newlog.setChangeTime(response.body().getData().get(0).getCreateTime());
                newlog.setComment(response.body().getData().get(0).getTaskDesc());
                newlog.setDocs(response.body().getData().get(0).getDocs());
                newlog.setStatus(response.body().getData().get(0).getStatus());
                newlog.setTaskId(response.body().getData().get(0).getTaskId());
                newlog.setmLogtype("Comment");

                logs.add(1, newlog);


                Tasklogs.Log newlog1 = new Tasklogs().new Log();
                newlog1.setChangedBy(response.body().getData().get(0).getFromUser());
                newlog1.setChangeTime(response.body().getData().get(0).getCreateTime());
                newlog1.setStatus(response.body().getData().get(0).getStatus());
                newlog1.setTaskId(response.body().getData().get(0).getTaskId());
                newlog1.setComment("");
                newlog1.setDocs(response.body().getData().get(0).getDocs());


                if(response.body().getData().get(0).getType().equalsIgnoreCase("L"))
                {
                    newlog1.setmLogtype("Attachment");
                    logs.add(2, newlog1);
                }


                Employee employee_list = new Gson().fromJson(AppUtil.getString(context.getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
                String pic_url = "";
                if(response.body().getData().get(0).getFromUser().equalsIgnoreCase(AppUtil.getString(context,TagsPreferences.USER_ID)))
                {
                    for(int i=0;i<employee_list.getData().size();i++)
                    {
                        if(employee_list.getData().get(i).getUserId().equalsIgnoreCase(response.body().getData().get(0).getToUser()))
                        {
                            pic_url= employee_list.getData().get(i).getMpic_url();
                        }

                    }

                }
                else {
                    for(int i=0;i<employee_list.getData().size();i++)
                    {
                        if(employee_list.getData().get(i).getUserId().equalsIgnoreCase(response.body().getData().get(0).getFromUser()))
                        {
                            pic_url= employee_list.getData().get(i).getMpic_url();
                        }

                    }
                }

                task_log_adapter = new Task_log_adapter(logs, context ,pic_url,Task_details.this );
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
                //  gridLayoutManager.setSmoothScrollbarEnabled(true);
//                gridLayoutManager.setAutoMeasureEnabled(false);
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                taskChat.setLayoutManager(gridLayoutManager);
//                taskChat.setItemAnimator(new DefaultItemAnimator());
//                taskChat.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                taskChat.setAdapter(task_log_adapter);
                task_log_adapter.notifyDataSetChanged();
                taskChat.scrollToPosition(logs.size() - 1);

            }


            @Override
            public void onFailure(Call<Tasklogs> call, Throwable t) {
                pDialog.dismiss();
                AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
            }
        });


    }

    public void update_status()
    {
        final Task_home info = (Task_home) getActivity().getSupportFragmentManager().findFragmentByTag(Task_home.TAG);
        info.setStatus(task_id,current_status);
    }

    public void add_new_message(Tasklogs.Log log) {
        logs.add(log);
        AppUtil.logger("Logs : ", logs.toString());
        task_log_adapter.notifyItemInserted(logs.size() - 1);
        taskChat.scrollToPosition(logs.size() - 1);
        sendComment.setEnabled(true);
        current_status=log.getStatus();
        update_status();
//        if(log.getmLogtype().equalsIgnoreCase("Status_change"))
//        {
//            AppUtil.logger(TAG,"set status change");
//            update_status();
////            final Task_home info = (Task_home) getActivity().getSupportFragmentManager().findFragmentByTag(Task_home.TAG);
////            info.setStatus(task_id,log.getStatus());
//        }
    }

    public void add_new_log() {
        Tasklogs.Log newlog1 = new Tasklogs().new Log();
        newlog1.setChangedBy("RO2323");
        newlog1.setChangeTime("rstrt");
        newlog1.setDocs("null");
        newlog1.setStatus("Created");
        newlog1.setTaskId("TD_3");
        newlog1.setComment("New message");
        newlog1.setmLogtype("Comment");
        add_new_message(newlog1);
    }

    public void add_new_comment() {
        Tasklogs.Log newlog1 = new Tasklogs().new Log();
        newlog1.setChangedBy(AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        newlog1.setChangeTime(changed_time);
        newlog1.setDocs("null");
        newlog1.setStatus("null");
        newlog1.setTaskId(task_id);
        newlog1.setComment(comment_text.getText().toString());
        newlog1.setmLogtype("Comment");
        comment_text.setText("");
        add_new_message(newlog1);
    }

    public void add_new_attachment() {
        Tasklogs.Log newlog1 = new Tasklogs().new Log();
        newlog1.setChangedBy(AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        newlog1.setChangeTime(changed_time);
        newlog1.setDocs(docs_url);
        newlog1.setStatus("null");
        newlog1.setTaskId(task_id);
        newlog1.setComment("");
        newlog1.setmLogtype("Attachment");
        add_new_message(newlog1);
    }

    public void add_new_status(){

        pDialog.dismiss();

        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Completed!")
                .setContentText("This task is complete!")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                       sweetAlertDialog.dismiss();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

        Tasklogs.Log newlog1 = new Tasklogs().new Log();
        newlog1.setChangedBy(AppUtil.getString(getContext(), TagsPreferences.USER_ID));
        newlog1.setChangeTime(changed_time);
        newlog1.setDocs("null");
        newlog1.setStatus(current_status);
        newlog1.setTaskId(task_id);
        newlog1.setComment("");
        newlog1.setmLogtype("Status_change");
        add_new_message(newlog1);


    }

    @OnClick(R.id.send_comment)
    public void setSendComment(View view) {
        if (comment_text.getText().toString().length() > 0) {

            log_type= "Comment";
            cmt = comment_text.getText().toString();
            status = "null";
            docs="";
            body=null;
            send_comment();
        }
    }


    private void send_comment() throws NullPointerException {
        sendComment.setEnabled(false);
        final ApiServices apiServices = RetrofitClient.getApiService();

        Call<ResponseBody> call = apiServices.update_task(StringtoRequestBody(log_type), StringtoRequestBody(cmt), StringtoRequestBody(status), StringtoRequestBody(AppUtil.getString(getContext(), TagsPreferences.USER_ID)), StringtoRequestBody(task_id),StringtoRequestBody("Task"), StringtoRequestBody(docs), body);

        //AppUtil.logger(TAG, "Creation Request : " + call.request().toString() + " Task type :" + type + " \n " + " Employee ID  :" + to_user + " \n " + "Assigned by :" + from_user + " \n " + "title :" + title + " \n " + "Description :" + desc + " \n " + "Deadline :" + deadline + " \n ");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {


                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        //AppUtil.logger("Comment updated" ,new JSONObject(response.body().string()).toString() );
                        AppUtil.logger(TAG, obj.toString());
                        changed_time=obj.getString("time");
                        docs_url=obj.getString("doc_url");
                        current_status= obj.getString("status");

                        if(log_type.equalsIgnoreCase("Comment"))
                        add_new_comment();
                        else if(log_type.equalsIgnoreCase("Attachment"))
                         add_new_attachment();
                        else
                            add_new_status();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppUtil.showToast(getContext(), "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    private RequestBody StringtoRequestBody(String param) {
        RequestBody description =
                RequestBody.create(
                        MultipartBody.FORM, param);
        return description;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppUtil.logger(TAG, "Destroyed");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        AppUtil.logger(TAG, "Detached");
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtil.logger(TAG, "Resumed");
        TaskActivity.visible_fragment = TAG;
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
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


    public void show_attachment_dialog()
    {
        final Dialog dialog = new Dialog(getContext());

        // dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        dialog.setTitle("Choose option");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_attachment);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView gallery,camera,pdf;

        gallery = (ImageView) dialog.findViewById(R.id.gallery);
        camera = (ImageView) dialog.findViewById(R.id.camera);
        pdf = (ImageView) dialog.findViewById(R.id.pdf);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadImagefromGallery(v);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                dialog.dismiss();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBrowse(v);
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    //this when button click
    public void onBrowse(View view) {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        getActivity().startActivityForResult(intent,RESULT_LOAD_PDF);
    }

    private static int RESULT_LOAD_PDF =2 ;



    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;


    Bitmap bitmap;
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }



    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = FileUtils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
        outState.putParcelable("file_uri", fileUri);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Intent intent_data = data;
            if(requestCode == RESULT_LOAD_PDF )
            {
                if(resultCode == RESULT_OK)
                {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Send this Attachment")
                            .setCancelText("No")
                            .setConfirmText("Yes,send it!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    AppUtil.logger(TAG,"Attachment selected");
                                    AppUtil.logger(TAG , " PDF SELECTED");
                                    try {
                                    Uri uri = intent_data.getData();

                                    InputStream newfile =  getContext().getContentResolver().openInputStream(uri);
                                    byte[] buffer = new byte[newfile.available()];
                                    newfile.read(buffer);

                                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "Inizio/Sent_Attachments");

                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    File pdf_created= new File(file.getAbsolutePath(), "/" + System.currentTimeMillis() + ".pdf");
                                    OutputStream outStream = new FileOutputStream(pdf_created);
                                    outStream.write(buffer);
//                    File file = FileUtils.getFile(this, fileUri);



//                    String FilePath = getRealPathFromURI(uri); // should the path be here in this string
                                    AppUtil.logger(TAG,"Path  = " + pdf_created.getAbsolutePath());
                                    attactment=pdf_created;
                                        log_type= "Attachment";
                                        cmt = "";
                                        status = "null";
                                        docs=attactment.getName();
                                        RequestBody requestFile =
                                                RequestBody.create(
                                                        MediaType.parse("application/pdf"),
                                                        attactment
                                                );

                                        // MultipartBody.Part is used to send also the actual file name
                                        body =
                                                MultipartBody.Part.createFormData("myfile", attactment.getName(), requestFile);
                                        sDialog.cancel();
                                        send_comment();

//                      show_attachment_card(pdf_created.getAbsolutePath().substring(pdf_created.getAbsolutePath().lastIndexOf("/") + 1));

                                    } catch (Exception e) {

                                        AppUtil.showToast(getContext(), "Something went wrong");
                                        AppUtil.logger("Task_create",e.toString());
                                    }

                                }
                            })
                            .show();




                }

            }


            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                    AppUtil.logger(TAG,"imgDecodableString : "+imgDecodableString);
                cursor.close();
                    compress_and_send_image(imgDecodableString);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadElementException e) {
                    e.printStackTrace();
                }


            }

        // when image is clicked
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                AppUtil.logger("Task_create", "Image Path : " + fileUri);
                try {
                    compress_and_send_image(fileUri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                //  compress_create_pdf_and_show_card(fileUri.getPath());
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



        else {
                AppUtil.showToast(getContext(),"You haven't selected any image");

            }







    }

    private void compress_and_send_image(String image_path) throws IOException, BadElementException {
        Compress compress = new Compress();
        String image_returned = compress.compressImage(image_path);
        AppUtil.logger("Returned path : ", image_returned);
        Image img = Image.getInstance(image_returned);
        Uri fileUri = Uri.parse(image_returned);
        AppUtil.logger("Uri of image ",fileUri.toString());
        //fileUri=selectedImage;
        File file = FileUtils.getFile(getContext(), fileUri);
        file= new File(image_returned);
//                    attactment = new File(imgDecodableString);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );
        body =
                MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        log_type= "Attachment";
        cmt = "";
        status = "null";
        docs=file.getName();
        send_comment();
    }



    public void set_status()
    {
        if(current_status.equalsIgnoreCase("Check for completion")) {

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Is this task complete?")
                    .setContentText("You won't be able to undo the process")
                    .setConfirmText("Yes,its Complete!")
                    .setCancelText("No, not yet")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismiss();
                            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating Status");
                            pDialog.setCancelable(false);
                            pDialog.show();


                            log_type = "Status change";
                            cmt = "";
                            status = "Complete";
                            docs = "";
                            body = null;
                            send_comment();


                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
                            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating Status");
                            pDialog.setCancelable(false);
                            pDialog.show();


                            log_type = "Status change";
                            cmt = "";
                            status = "Incomplete";
                            docs = "";
                            body = null;
                            send_comment();
                        }
                    })
                    .show();
        }
        else {

            if(!current_status.equalsIgnoreCase("Complete")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Mark this task as Complete?")
                        .setContentText("You won't be able to undo the process")
                        .setConfirmText("Yes,its Complete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismiss();
                                pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Updating Status");
                                pDialog.setCancelable(false);
                                pDialog.show();


                                log_type = "Status change";
                                cmt = "";
                                status = "Complete";
                                docs = "";
                                body = null;
                                send_comment();


                            }
                        })
                        .show();


            }
            else {


            }
        }
    }




    public void set_task_deleted()
    {
        comment_text.setVisibility(View.GONE);
        sendComment.setVisibility(View.GONE);
        hide_status_icon();
    }






















}
