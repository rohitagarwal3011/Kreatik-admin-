package com.app.rbc.admin.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {


    String type, task_id, title;
    Intent intent;
    ImageView reveal;
    @BindView(R.id.kreatik_title)
    TextView kreatikTitle;
    @BindView(R.id.kreatik_icon)
    ImageView kreatikIcon;
    @BindView(R.id.layoutMain)
    FrameLayout layoutMain;

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
//        final Animation myAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.bounce);
//        myAnim.setDuration(2000);
//
//        final Animation fadeIn = AnimationUtils.loadAnimation(Splash.this, R.anim.fade_in);
//        fadeIn.setDuration(2000);
//
//
//        reveal.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                reveal.startAnimation(myAnim);
//            }
//        }, 100);
//
//        kreatikIcon.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                kreatikIcon.startAnimation(fadeIn);
//            }
//        },2200);
//        reveal.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//                if (AppUtil.getBoolean(Splash.this, TagsPreferences.IS_LOGIN)) {
//                    try {
//
//                        if (type.equalsIgnoreCase("task_update")) {
//                            AppUtil.logger("Intent : ", intent.getExtras().toString());
//                            JSONArray arr = new JSONArray(intent.getStringExtra("data"));
//
//                            Intent intent = new Intent(Splash.this, TaskActivity.class);
//                            intent.putExtra("type", "task_update");
//                            intent.putExtra("title", title);
//                            intent.putExtra("task_id", arr.getJSONObject(0).getString("task_id"));
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                            finish();
//                        } else if (type.equalsIgnoreCase("new_task")) {
//                            Intent intent = new Intent(Splash.this, TaskActivity.class);
//                            intent.putExtra("type", "new_task");
//                            intent.putExtra("title", title);
//                            intent.putExtra("task_id", task_id);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                            finish();
//                        } else if (type.equalsIgnoreCase("mark_attendance")) {
//                            Intent intent = new Intent(Splash.this, AttendanceActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                            finish();
//                        } else {
//
//
//                            Intent intent = new Intent(Splash.this, HomeActivity.class);
//
//
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                            finish();
//
//                        }
//                    } catch (Exception e) {
//
//                        Intent intent = new Intent(Splash.this, HomeActivity.class);
//
//
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                        finish();
//                    }
//                } else {
//
//                    Intent intent = new Intent(Splash.this, LoginActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    finish();
//                }
//            }
//        }, 500);
    }

    //
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animation myAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.bounce);
                myAnim.setDuration(2000);


                reveal.startAnimation(myAnim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
//                                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//                        reveal.setLayoutParams(lparams);

                        layoutMain.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        kreatikIcon.setVisibility(View.VISIBLE);
                        kreatikTitle.setVisibility(View.VISIBLE);


                        final Animation fadeIn = AnimationUtils.loadAnimation(Splash.this, R.anim.fade_in);
                        fadeIn.setDuration(1000);

                        kreatikIcon.startAnimation(fadeIn);
                        kreatikTitle.startAnimation(fadeIn);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//
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
                                        }

                                        else if (type.equalsIgnoreCase("new_po"))
                                        {
                                            Intent intent = new Intent(Splash.this, StockActivity.class);
                                            intent.putExtra("category",intent.getStringExtra("category"));
                                            intent.putExtra("po_id",intent.getStringExtra("parent_id"));
                                            intent.putExtra("type",intent.getStringExtra("type"));
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                            finish();
                                        }

                                        else {


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
                        }, 3000);

                    }
                }, 1000);

            }
        }, 100);


    }

}
