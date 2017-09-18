package com.app.rbc.admin.activities;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.rbc.admin.R;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Splash extends AppCompatActivity {


    String type, task_id, title;
    Intent intent;
    ImageView kreatik_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        intent = getIntent();
        type = intent.getStringExtra("type");
        task_id = intent.getStringExtra("task_id");
        title = intent.getStringExtra("title");

        kreatik_icon = (ImageView) findViewById(R.id.kreatik_icon);
    }

//    public void presentActivity(View view) {
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(this, view, "transition");
//        int revealX = (int) (view.getX() + view.getWidth() / 2);
//        int revealY = (int) (view.getY() + view.getHeight() / 2);
//
//        Intent intent = new Intent(this, SecondActivity.class);
//        intent.putExtra(SecondActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
//        intent.putExtra(SecondActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
//
//        ActivityCompat.startActivity(this, intent, options.toBundle());
//    }

    //
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onResume() {
        super.onResume();
        new Timer().schedule(
                new TimerTask() {
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
                                } else if (type.equalsIgnoreCase("new_task")) {
                                    Intent intent = new Intent(Splash.this, TaskActivity.class);
                                    intent.putExtra("type", "new_task");
                                    intent.putExtra("title", title);
                                    intent.putExtra("task_id", task_id);
                                    startActivity(intent);
                                    finish();
                                } else if (type.equalsIgnoreCase("mark_attendance")) {
                                    Intent intent = new Intent(Splash.this, AttendanceActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {



                                    Intent intent = new Intent(Splash.this, HomeActivity.class);


                                   startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {




                                Intent intent = new Intent(Splash.this, HomeActivity.class);


                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(Splash.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                3000
        );
    }

//    @OnClick(R.id.fab)
//    public void onViewClicked() {
//        showbackground();
//    }
}
