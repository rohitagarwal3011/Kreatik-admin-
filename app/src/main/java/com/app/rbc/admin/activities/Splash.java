package com.app.rbc.admin.activities;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.app.rbc.admin.R;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Splash extends AppCompatActivity {


//    private FloatingActionButton fab;
//    private RelativeLayout layoutMain;
//    private RelativeLayout layoutButtons;
//    private RelativeLayout layoutContent;
//    private boolean isOpen = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
//        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
//        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
//
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewMenu();
//            }
//        });
//    }
//
//    private void viewMenu() {
//
//        if (!isOpen) {
//            int cx = layoutButtons.getWidth() ;
//            int cy = layoutButtons.getHeight();
//
//            // get the final radius for the clipping circle
//            float finalRadius = (float) Math.hypot( layoutButtons.getWidth(),layoutButtons.getHeight());
//
//            // create the animator for this view (the start radius is zero)
//            Animator anim =
//                    ViewAnimationUtils.createCircularReveal(layoutButtons, cx, cy, 0, finalRadius);
//            anim.setDuration(120000);
//
//            // make the view visible and start the animation
//            layoutButtons.setVisibility(View.VISIBLE);
//            anim.start();
//            isOpen=true ;
//
//        } else {
//
//            int x = layoutButtons.getRight();
//            int y = layoutButtons.getBottom();
//
//            int startRadius = Math.max(layoutContent.getWidth(), layoutContent.getHeight());
//            int endRadius = 0;
//
//            fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null)));
//           // fab.setImageResource(R.drawable.ic_plus_white);
//
//            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
//            anim.setDuration(120000);
//            anim.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    layoutButtons.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//            anim.start();
//
//            isOpen = false;
//        }
//    }


    String type, task_id, title;
    Intent intent;
    @BindView(R.id.layoutContent)
    RelativeLayout layoutContent;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.layoutMain)
    RelativeLayout layoutMain;
    @BindView(R.id.layoutButtons)
    RelativeLayout layoutButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        intent = getIntent();
        type = intent.getStringExtra("type");
        task_id = intent.getStringExtra("task_id");
        title = intent.getStringExtra("title");

    }

    //
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    private void showbackground() {
        int x = layoutContent.getRight();
        int y = layoutContent.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);

        layoutButtons.setVisibility(View.VISIBLE);
        anim.start();
    }

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
                                AppUtil.logger("Splash", e.toString());
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
