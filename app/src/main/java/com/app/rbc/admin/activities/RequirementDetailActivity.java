package com.app.rbc.admin.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Vehicle_detail_adapter;
import com.app.rbc.admin.fragments.Requirement_create_new;
import com.app.rbc.admin.fragments.Requirement_fulfill_task;
import com.app.rbc.admin.fragments.Stock_list_product_wise;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Product;
import com.app.rbc.admin.models.RequirementDetails;
import com.app.rbc.admin.models.VehicleDetail;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirementDetailActivity extends AppCompatActivity {



    public RequirementDetails requirementDetails;
    public Toolbar toolbar;
    public static String rq_id;
    public static String req_site_name;
    public static String req_site_id;
    public static List<String> product_list = new ArrayList<>();
    @BindView(R.id.frame_main)
    FrameLayout frameMain;

    private String category_selected;

    public List<Order> orders = new ArrayList<>();
    public Bundle finalform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_detail);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        rq_id = intent.getStringExtra("rq_id");
        category_selected = intent.getStringExtra("category_selected");
        ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, com.app.rbc.admin.fragments.RequirementDetails.newInstance(category_selected), com.app.rbc.admin.fragments.RequirementDetails.TAG);

    }


    public void  setToolbar(String title)
    {
        toolbar.setTitle(title);
    }


    @Override
    public void onBackPressed() {

        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (mFragment instanceof com.app.rbc.admin.fragments.RequirementDetails)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }

            super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
