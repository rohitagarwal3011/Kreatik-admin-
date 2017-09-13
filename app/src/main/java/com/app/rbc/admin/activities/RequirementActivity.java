package com.app.rbc.admin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Requirement_list_adapter;
import com.app.rbc.admin.fragments.Product_selection;
import com.app.rbc.admin.fragments.Requirement_create_new;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.RequirementList;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.rbc.admin.utils.ChangeFragment.changeFragment;

public class RequirementActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.frame_main)
    FrameLayout frameMain;

    String category_selected;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        // TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //      tabLayout.setupWithViewPager(mViewPager);

        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hide_tablayout();
                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(category_selected, AppUtil.getString(RequirementActivity.this,TagsPreferences.USER_ID)), Requirement_create_new.TAG);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        hide_tablayout();
        changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories().newInstance("RequirementActivity"), Stock_categories.TAG);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void show_tablayout() {

        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        frameMain.setVisibility(View.GONE);
        fab.show();

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


    }

    public void hide_tablayout() {
        tabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        frameMain.setVisibility(View.VISIBLE);
        fab.hide();
    }


    SweetAlertDialog pDialog;


    public void get_category_requirements(String category) {
        pDialog = new SweetAlertDialog(RequirementActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        category_selected = category;

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<RequirementList> call = apiServices.category_requirement_list(category);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Requirement Activity ", "Get Category Requirements : " + call.request().toString() + "Category :" + category);
        call.enqueue(new Callback<RequirementList>() {
            @Override
            public void onResponse(Call<RequirementList> call, Response<RequirementList> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getApplicationContext(), TagsPreferences.REQUIREMENT_LIST, new Gson().toJson(response.body()));
                    //productDetails = new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.CATEGORY_DETAILS), RequirementList.class);
                    AppUtil.logger("Product Details : ", AppUtil.getString(getApplicationContext(), TagsPreferences.REQUIREMENT_LIST));
                    show_tablayout();
                }

            }

            @Override
            public void onFailure(Call<RequirementList> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(RequirementActivity.this, "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_req_details(String req_id)
    {
        Intent intent = new Intent(RequirementActivity.this,RequirementDetailActivity.class);
        intent.putExtra("rq_id",req_id);
        intent.putExtra("category_selected",category_selected);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        Unbinder unbinder;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        int position;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_requirement_list, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            position = getArguments().getInt(ARG_SECTION_NUMBER);
            AppUtil.logger("Position for adapter : ",String.valueOf(position));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            List<RequirementList.ReqList> reqLists = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.REQUIREMENT_LIST), RequirementList.class).getReqList();

            List<RequirementList.ReqList> latest = new ArrayList<>(),ongoing = new ArrayList<>(),complete= new ArrayList<>();

            for(int i=0;i<reqLists.size();i++)
            {
                if(reqLists.get(i).getDetails().get(0).getStatus().equalsIgnoreCase("created"))
                {
                    latest.add(reqLists.get(i));

                }
                else if(reqLists.get(i).getDetails().get(0).getStatus().equalsIgnoreCase("complete"))
                {
                    complete.add(reqLists.get(i));
                }
                else {
                    ongoing.add(reqLists.get(i));
                }
            }


            switch (position) {
                case 1:
                    show_requirement_list(latest);
                    break;
                case 2:
                  show_requirement_list(ongoing);
                    break;
                case 3:
                    show_requirement_list(complete);
                    break;

            }
        }

        public void show_requirement_list(List<RequirementList.ReqList> reqLists) {

           // List<RequirementList.ReqList> reqLists = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.REQUIREMENT_LIST), RequirementList.class).getReqList();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


            Requirement_list_adapter adapter = new Requirement_list_adapter(reqLists, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String tag1, tag2, tag3;

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // get the tags set by FragmentPagerAdapter
            switch (position) {
                case 0:
                    String firstTag = createdFragment.getTag();
                    tag1 = firstTag;
                    break;
                case 1:
                    String secondTag = createdFragment.getTag();
                    tag2 = secondTag;
                    break;
                case 2:
                    String thirdTag = createdFragment.getTag();
                    tag3 = thirdTag;
                    break;
            }
            // ... save the tags somewhere so you can reference them later
            return createdFragment;
        }

        public void onclick_method(final String req_id) {
            // do work on the referenced Fragments, but first check if they
            // even exist yet, otherwise you'll get an NPE.

            show_req_details(req_id);
//            if (tag1 != null) {
//                // m1stFragment.doWork();
//            }
//
//            if (tag2 != null) {
//
//
////                show_po_details(po);
//
//
//            }
//
//            if (tag3 != null) {
//
//            }
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Latest";
                case 1:
                    return "On-Going";
                case 2:
                    return "Completed";
            }
            return null;
        }
    }
}