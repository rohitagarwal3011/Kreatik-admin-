package com.app.rbc.admin.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.UpdatePlaceholderFragment;

public class ChatActivity extends AppCompatActivity {

    private UpdatePlaceholderFragment updatePlaceholderFragment;
    private Toolbar toolbar;


    public static final int ACTIVITY = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void setFragment(int code,Object... data) {
        FragmentManager fm = getSupportFragmentManager();
        switch (code) {
            case 0 :
                getSupportActionBar().setTitle("Chats");
                updatePlaceholderFragment = new UpdatePlaceholderFragment();
                fm.beginTransaction()
                        .replace(R.id.fragment_container, updatePlaceholderFragment)
                        .commit();
                break;

        }
    }
}
