package com.app.rbc.admin.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.rbc.admin.R;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;

import org.json.JSONArray;

public class Splash extends AppCompatActivity {

    String type,task_id,title;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         intent = getIntent();
        type = intent.getStringExtra("type");
        task_id=intent.getStringExtra("task_id");
        title=intent.getStringExtra("title");
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
//                        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                            @Override
//                            public void onReceive(Context context, Intent intent) {
//                                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
//                                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
//
//                                    if(intent.getStringExtra("type").equalsIgnoreCase("task_update")) {
//                                        Intent next_intent = new Intent(Splash.this,TaskActivity.class);
//                                        startActivity(next_intent);
////                                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////                                        notificationManager.cancelAll();
////
////                                        Tasklogs.Log newlog1 = new Tasklogs().new Log();
////                                        newlog1.setChangedBy(intent.getStringExtra("changed_by"));
////                                        newlog1.setChangeTime("rstrt");
////                                        newlog1.setDocs(intent.getStringExtra("docs"));
////                                        newlog1.setStatus(intent.getStringExtra("status"));
////                                        newlog1.setTaskId(intent.getStringExtra("task_id"));
////                                        newlog1.setComment(intent.getStringExtra("comment"));
////                                        newlog1.setmLogtype(intent.getStringExtra("log_type"));
////                                        add_new_message(newlog1);
//                                    }
//
//                                }
//                                // checking for type intent filter
//
//                            }
//                        };

                        if (AppUtil.getBoolean(Splash.this, TagsPreferences.IS_LOGIN)) {
                        try {

                                if (type.equalsIgnoreCase("task_update")) {
                                    AppUtil.logger("Intent : ", intent.getExtras().toString());
                                    JSONArray arr = new JSONArray(intent.getStringExtra("data"));

                                    Intent intent = new Intent(Splash.this, TaskActivity.class);
                                    intent.putExtra("type", "task_update");
                                    intent.putExtra("title", title);
                                    intent.putExtra("task_id", arr.getJSONObject(0).getString("task_id"));
                                    startActivity(intent);
                                    finish();
                                }

                                else if (type.equalsIgnoreCase("new_task"))
                                {
                                    Intent intent = new Intent(Splash.this, TaskActivity.class);
                                    intent.putExtra("type", "new_task");
                                    intent.putExtra("title", title);
                                    intent.putExtra("task_id",task_id);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(type.equalsIgnoreCase("mark_attendance"))
                                {
                                    Intent intent = new Intent(Splash.this,AttendanceActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else {
                                    Intent intent = new Intent(Splash.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        catch(Exception e)
                            {
                                AppUtil.logger("Splash", e.toString());
                                Intent intent = new Intent(Splash.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                       else {
                            Intent intent = new Intent(Splash.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                3000
        );
    }
}
