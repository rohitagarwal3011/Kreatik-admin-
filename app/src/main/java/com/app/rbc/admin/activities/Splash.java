package com.app.rbc.admin.activities;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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


    String type, task_id, title;
    Intent intent;
    ImageView reveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        intent = getIntent();
        type = intent.getStringExtra("type");
        task_id = intent.getStringExtra("task_id");
        title = intent.getStringExtra("title");

        reveal = (ImageView) findViewById(R.id.reveal);
        reveal.setVisibility(View.VISIBLE);
        final Animation myAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.bounce);

        reveal.postDelayed(new Runnable() {
            @Override
            public void run() {
                reveal.startAnimation(myAnim);
            }
        },100);

        reveal.postDelayed(new Runnable() {
            @Override
            public void run() {


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
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            finish();
                        } else if (type.equalsIgnoreCase("new_task")) {
                            Intent intent = new Intent(Splash.this, TaskActivity.class);
                            intent.putExtra("type", "new_task");
                            intent.putExtra("title", title);
                            intent.putExtra("task_id", task_id);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            finish();
                        } else if (type.equalsIgnoreCase("mark_attendance")) {
                            Intent intent = new Intent(Splash.this, AttendanceActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            finish();
                        } else {


                            Intent intent = new Intent(Splash.this, HomeActivity.class);


                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            finish();

                        }
                    } catch (Exception e) {

                        Intent intent = new Intent(Splash.this, HomeActivity.class);


                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        finish();
                    }
                } else {

                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    finish();
                }
            }
        },500);
    }

    //
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



            }
        }, 300);




    }

//    @OnClick(R.id.fab)
//    public void onViewClicked() {
//        showbackground();
//    }
}
