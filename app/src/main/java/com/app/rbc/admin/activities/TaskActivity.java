package com.app.rbc.admin.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.Task_create;
import com.app.rbc.admin.fragments.Task_details;
import com.app.rbc.admin.fragments.Task_home;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.FileDownloader;
import com.app.rbc.admin.utils.RetrofitClient;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskActivity extends AppCompatActivity implements Task_home.OnTaskTypeSelectListener,OnMenuItemClickListener, OnMenuItemLongClickListener {

    @BindView(R.id.frame_main)
    FrameLayout frameMain;
    private String TAG = "TaskActivity";
    Task_create task_create;
    Task_details task_details;
    Task_home task_home;
    SweetAlertDialog pDialog;
    public Toolbar toolbar;
    Context context;
    final ApiServices apiServices = RetrofitClient.getApiService();
    public static String visible_fragment;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //initMenuFragment();
        setSupportActionBar(toolbar);
        context = this.getApplicationContext();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Intent intent = getIntent();
            if (intent.getStringExtra("type").equalsIgnoreCase("task_update")||intent.getStringExtra("type").equalsIgnoreCase("new_task"))
            {
                //show_task_details(intent.getStringExtra("task_id"),"Title");

                AppUtil.logger(TAG, "Task_id : "+intent.getStringExtra("task_id") );
                Task_home task_home = new Task_home();
                Bundle args = new Bundle();
                args.putString(Task_home.TASK_ID,intent.getStringExtra("task_id"));
                args.putString(Task_home.TASK_TITLE,intent.getStringExtra("title"));

                task_home.setArguments(args);
               // task_home.newInstance(intent.getStringExtra("task_id"));
                setFragment(task_home,Task_home.TAG);

            }
        }
        catch (Exception e)
        {
            AppUtil.logger(TAG,e.toString());
            task_home = new Task_home();
            setFragment(task_home,Task_home.TAG);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task, menu);

        return true;
    }



    @Override
    public void OnTaskSelected(String type) {
        task_create = new Task_create();
        Bundle args = new Bundle();
        task_create = task_create.newInstance(type);
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frame_main, task_create,task_create.TAG);
        fm.addToBackStack("task_create");
        fm.commit();
    }

    public void setFragment(Fragment frag,String TAG) {


        if (findViewById(R.id.frame_main) == null) {
            AppUtil.logger(TAG,"Frame main is null");
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_main, frag).commit();
        } else {
            AppUtil.logger(TAG,"Frame main is not null");
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_main, frag,TAG);
            fm.addToBackStack("");
            fm.commit();
        }

    }


    public void show_task_details(String task_id, String task_title) {
        task_details = new Task_details();
        Bundle args = new Bundle();
        task_details = task_details.newInstance(task_id);
        setToolbar(task_title);
        setFragment(task_details,Task_details.TAG);

    }


    public void setToolbar(String title)
    {
        toolbar.setTitle(title);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
                AppUtil.logger(TAG,"Home pressed");
                onBackPressed();
                return true;
            case R.id.add_attachment:

                return true;
            case R.id.status:
//                if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
//                    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
//                }

                final Task_details info = (Task_details) getSupportFragmentManager().findFragmentByTag(Task_details.TAG);
                info.set_status();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("In Review");
        send.setResource(R.drawable.circle_green);

        MenuObject like = new MenuObject("Check for Completion");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.circle_orange);
        like.setResource(R.drawable.circle_orange);

        MenuObject addFr = new MenuObject("Complete");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.circle_blue));
        addFr.setResource(R.drawable.circle_blue);

        MenuObject addFav = new MenuObject("Delayed");
        addFav.setResource(R.drawable.circle_red);

        MenuObject block = new MenuObject("Block user");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        //menuObjects.add(block);
        return menuObjects;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();

        if(position>0) {
            AppUtil.showToast(context, getMenuObjects().get(position).getTitle());
//            final Task_details info = (Task_details) getSupportFragmentManager().findFragmentByTag(Task_details.TAG);
//            info.set_status(getMenuObjects().get(position).getTitle());
        }

    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
       // Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {


        AppUtil.logger(TAG,"Visible :"+visible_fragment);
        if(visible_fragment.equalsIgnoreCase("Task_create"))
        {

            if(Task_create.details_page)
            {
                final Task_create info = (Task_create) getSupportFragmentManager().findFragmentByTag(Task_create.TAG);
//                if(fragment.isAdded()){
                info.onBackPressed();
//                }
            }
            else {
                setToolbar("Task");
                task_home = new Task_home();
                setFragment(task_home,Task_home.TAG);
            }

        }
        else if(visible_fragment.equalsIgnoreCase("Task_details"))
        {
            getSupportActionBar().setTitle("Task");
            visible_fragment="Task_home";
            super.onBackPressed();
//            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
//            fm.replace(R.id.frame_main, new Task_home());
//            fm.addToBackStack(null);
//            fm.commit();

        }
        else {
            if (Task_home.show_delete)
            {
                task_home.stop_animation();
            }
            else {
                Intent intent = new Intent(TaskActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }




    }

    public void show_pdf(File file)
    {
       // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(file);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            AppUtil.showToast(context, "No Application available to view PDF");
        }
    }

    public void download(String url , String file_name)
    {
        new DownloadFile().execute(url, file_name);
    }




























    private class DownloadFile extends AsyncTask<String, Void, Void> {

        String file_name;
        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            file_name = fileName;
            File folder = new File(Environment.getExternalStorageDirectory().getPath(), "Inizio/Downloaded_Attachments");

            if (!folder.exists()) {
                folder.mkdirs();
            }


            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AppUtil.showToast(context,"File Downloaded");
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "Inizio/Downloaded_Attachments/"+file_name);
            show_pdf(file);


        }
    }


}