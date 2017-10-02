package com.app.rbc.admin.activities;

import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.SettingsFragment;
import com.app.rbc.admin.fragments.SiteOverviewFragment;
import com.app.rbc.admin.fragments.SiteOverviewListFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orm.SugarContext;

public class SiteOverviewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final int ACTIVITY = 2;
    private SiteOverviewListFragment siteOverviewListFragment;
    private SiteOverviewFragment siteOverviewFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_overview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SugarContext.init(this);
        MultiDex.install(this);
        setFragment(1);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void setFragment(int code,Object... data) {
        FragmentManager fm = getSupportFragmentManager();

        switch (code) {
            case 1 :
                siteOverviewListFragment = new SiteOverviewListFragment();
                fm.beginTransaction()
                        .replace(R.id.fragment_container,siteOverviewListFragment)
                        .commit();
                break;

            case 2 :
                siteOverviewFragment = new SiteOverviewFragment();
                siteOverviewFragment.site = (long)data[0];
                fm.beginTransaction()
                        .replace(R.id.fragment_container,siteOverviewFragment).addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void publishAPIResponse(int status,int code,String... message) {
        switch (code) {

            case 11 :
                siteOverviewListFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 21 :
                siteOverviewFragment.publishAPIResponse(status,code,message);
                break;

        }
    }
}
