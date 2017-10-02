package com.app.rbc.admin.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.Requirement_list_adapter;
import com.app.rbc.admin.fragments.Product_selection;
import com.app.rbc.admin.fragments.Requirement_create_new;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.RequirementList;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.rbc.admin.utils.ChangeFragment.changeFragment;

public class RequirementActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private String TAG = RequirementActivity.class.getCanonicalName();
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

    public List<Order> orders = new ArrayList<>();
    public Bundle finalForm;
    public String category_selected;

    public SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Menu menu;
    public SearchView searchView;

    public static boolean show_tabs = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
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

                getSupportFragmentManager().popBackStackImmediate();
                hide_tablayout();
                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, Product_selection.newInstance(category_selected, AppUtil.getString(RequirementActivity.this,TagsPreferences.USER_ID)), Requirement_create_new.TAG);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });




    }

    Intent intent;

    @Override
    protected void onResume() {
        super.onResume();

        try{
            intent = getIntent();
            if (intent.getStringExtra("type").equalsIgnoreCase("new_req")||intent.getStringExtra("type").equalsIgnoreCase("vehicle")) {
                get_category_requirements(intent.getStringExtra("category"));
            }
        }
        catch (Exception e) {
            if (show_tabs) {
                show_tablayout();
            } else {
                hide_tablayout();
                changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories().newInstance("RequirementActivity"), Stock_categories.TAG);

            }
        }
    }

    @Override
    public void onBackPressed() {

        AppUtil.logger(TAG,"Back Pressed");
        if(tabLayout.getVisibility() == View.VISIBLE)
        {
            AppUtil.logger(TAG,"Tabs Visible");
            hide_tablayout();
            show_tabs=false;
            changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories().newInstance("RequirementActivity"), Stock_categories.TAG);
        }
        else {
            AppUtil.logger(TAG,"Tabs Invisible");

            if (show_tabs)
            {
                AppUtil.logger(TAG,"Show Tabs True");
                getSupportFragmentManager().popBackStackImmediate();
                show_tablayout();

            }

           // else if(getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG).isVisible())
            else
            {
                AppUtil.logger(TAG,"Show Tabs false");
                try {

                    Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                    if (mFragment instanceof Requirement_create_new)
                    {
                        AppUtil.logger(TAG," Requirement create new ");
                        super.onBackPressed();
                    }
                    else if(mFragment instanceof Stock_categories)
                    {
                        AppUtil.logger(TAG,"Stock Categories");
                        show_tabs = false;
                        getSupportFragmentManager().popBackStackImmediate();
                        Intent intent = new Intent(RequirementActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }

                }
                catch (Exception e)
                {
                    AppUtil.logger("Exception ",e.toString());
                }

            }

//            else if(getSupportFragmentManager()..isVisible())
//            {
//                show_tablayout();
//            }
//            else if(getSupportFragmentManager().findFragmentByTag(Stock_add_po_details.TAG).isVisible())
//            {
//                show_tablayout();
//            }
//            else {
//                getSupportFragmentManager().popBackStackImmediate();
//                // super.onBackPressed();
//            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement, menu);
        this.menu = menu;
        if(!show_tabs)
        {
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);

        }
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.filter) {
            RequirementActivity.PlaceholderFragment placeholderFragment = (RequirementActivity.PlaceholderFragment) mSectionsPagerAdapter
                    .getFragment(mViewPager.getCurrentItem());
            switch (mViewPager.getCurrentItem()) {
                case 0:placeholderFragment.setRequirementFilter(placeholderFragment.latest);
                    break;
                case 1:placeholderFragment.setRequirementFilter(placeholderFragment.ongoing);
                    break;
                case 2:placeholderFragment.setRequirementFilter(placeholderFragment.complete);
                    break;
            }
        }
       else if(id == android.R.id.home)
        {
            AppUtil.logger(TAG,"Home Pressed");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    public void show_tablayout() {

        toolbar.setTitle(category_selected+" Requirement");
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        frameMain.setVisibility(View.GONE);
        fab.show();

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.filter).setVisible(true);


    }

    public void hide_tablayout() {
        tabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        frameMain.setVisibility(View.VISIBLE);

        fab.hide();
        if(menu != null)
        {
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);

        }

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

                    try {
                        if (intent.getStringExtra("type").equalsIgnoreCase("new_req")||intent.getStringExtra("type").equalsIgnoreCase("vehicle")) {
                            show_req_details(intent.getStringExtra("rq_id"));
                        }
                    }
                    catch (Exception e){
                        show_tablayout();
                    }



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
        getSupportFragmentManager().popBackStackImmediate();
        Intent intent = new Intent(RequirementActivity.this,RequirementDetailActivity.class);
        intent.putExtra("rq_id",req_id);
        intent.putExtra("category_selected",category_selected);

        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        RequirementActivity.PlaceholderFragment placeholderFragment = (RequirementActivity.PlaceholderFragment) mSectionsPagerAdapter
                .getFragment(mViewPager.getCurrentItem());
        switch (mViewPager.getCurrentItem()) {
            case 0 :
               List<RequirementList.ReqList> search = new ArrayList<>();
                for(int i = 0 ; i < placeholderFragment.latest.size()  ; i++) {
                    RequirementList.ReqList.Detail detail = placeholderFragment.latest.get(i).getDetails().get(0);
                    if(detail.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getSite().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getPurpose().toLowerCase().contains(newText.toLowerCase())) {
                        search.add(placeholderFragment.latest.get(i));
                    }
                }
                placeholderFragment.set_requirement_list(search);
                break;

            case 1:
                List<RequirementList.ReqList> search_ongoing = new ArrayList<>();
                for(int i = 0 ; i < placeholderFragment.ongoing.size()  ; i++) {
                    RequirementList.ReqList.Detail detail = placeholderFragment.ongoing.get(i).getDetails().get(0);
                    if(detail.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getSite().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getPurpose().toLowerCase().contains(newText.toLowerCase())) {
                        search_ongoing.add(placeholderFragment.ongoing.get(i));
                    }
                }
                placeholderFragment.set_requirement_list(search_ongoing);
                break;

            case 2 :
                List<RequirementList.ReqList> search_complete = new ArrayList<>();
                for(int i = 0 ; i < placeholderFragment.complete.size()  ; i++) {
                    RequirementList.ReqList.Detail detail = placeholderFragment.complete.get(i).getDetails().get(0);
                    if(detail.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getSite().toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getPurpose().toLowerCase().contains(newText.toLowerCase())) {
                        search_complete.add(placeholderFragment.complete.get(i));
                    }
                }
                placeholderFragment.set_requirement_list(search_complete);
                break;
        }

        return true;

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

        @BindView(R.id.empty_relative)
        RelativeLayout empty_relative;


        Unbinder unbinder;
        private Button created_on;
        private Calendar myCalendar;
        SwipeRefreshLayout swipeRefreshLayout;

        public List<RequirementList.ReqList> latest =  new ArrayList<>(),
                ongoing = new ArrayList<>(),
                complete = new ArrayList<>();

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
            swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                    ((RequirementActivity)getActivity()).get_category_requirements(
                            ((RequirementActivity)getActivity()).category_selected);
                    recyclerView.invalidate();
                }
            });
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            List<RequirementList.ReqList> reqLists = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.REQUIREMENT_LIST), RequirementList.class).getReqList();

            latest.clear();
            complete.clear();
            ongoing.clear();

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
                    if(latest.size() == 0) {
                        empty_relative.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_relative.setVisibility(View.GONE);
                    }
                    show_requirement_list(latest);
                    break;
                case 2:
                    if(ongoing.size() == 0) {
                        empty_relative.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_relative.setVisibility(View.GONE);
                    }
                  show_requirement_list(ongoing);
                    break;
                case 3:

                    if(complete.size() == 0) {
                        empty_relative.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_relative.setVisibility(View.GONE);
                    }

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

            Requirement_list_adapter adapter = new Requirement_list_adapter(reqLists, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        public void set_requirement_list(List<RequirementList.ReqList> reqLists) {


            Requirement_list_adapter adapter = new Requirement_list_adapter(reqLists, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        public void setRequirementFilter(final List<RequirementList.ReqList> requirements) {
            final Dialog dialog = new Dialog(getActivity());

            View dialogView= getActivity().getLayoutInflater().inflate(R.layout.dialog_filter_requirement_list,null);

            final Spinner category_spinner = (Spinner) dialogView.findViewById(R.id.category_spinner);
            final Spinner site_spinner = (Spinner) dialogView.findViewById(R.id.site_spinner);
            created_on = (Button) dialogView.findViewById(R.id.created_on_button);
            Button submit = (Button) dialogView.findViewById(R.id.submit);


            final List<String> categories = new ArrayList<>();
            final List<String> sites = new ArrayList<>();

            categories.add(0,"All");
            sites.add(0,"All");

            created_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myCalendar = Calendar.getInstance();
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            for(int i = 0 ; i < requirements.size() ; i++) {
                RequirementList.ReqList.Detail detail = requirements.get(i).getDetails().get(0);
                int j;
                for(j = 0 ; j < categories.size() ; j++) {


                    if(categories.get(j).equalsIgnoreCase(detail.getCategory())) {
                        break;
                    }
                }
                if(j == categories.size()) {
                    categories.add(detail.getCategory());
                }

                for(j = 0 ; j < sites.size() ; j++) {
                    if(sites.get(j).equalsIgnoreCase(detail.getSite())) {
                        break;
                    }
                }
                if(j == sites.size()) {
                    sites.add(detail.getSite());
                }


            }

            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.custom_spinner_text,
                    categories);
            category_spinner.setAdapter(category_adapter);

            ArrayAdapter<String> site_adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.custom_spinner_text,
                    sites);
            site_spinner.setAdapter(site_adapter);



            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<RequirementList.ReqList> filtered = new ArrayList<>();

                    for(int i = 0 ; i < requirements.size() ; i++) {
                        RequirementList.ReqList.Detail detail = requirements.get(i).getDetails().get(0);
                        if(category_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getCategory().equalsIgnoreCase(categories
                                    .get(category_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(site_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getSite().equalsIgnoreCase(sites
                                    .get(site_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(!(created_on.getText().toString().equals(""))) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                                Date date = format.parse(detail.getCreatedOn());
                                long millisTodo = date.getTime();

                                date = format.parse(created_on.getText().toString());
                                long millisFilter = date.getTime();

                                if (!(millisTodo <= millisFilter)) {
                                    continue;
                                }

                            } catch (Exception e) {
                                AppUtil.logger("Date Parse", e.toString());
                            }
                        }

                        filtered.add(requirements.get(i));

                    }
                    set_requirement_list(filtered);
                }
            });


            dialog.setContentView(dialogView);
            dialog.show();
        }



        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                created_on.setText(sdf.format(myCalendar.getTime()));
            }

        };

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        private List<Fragment> fragments = new ArrayList<>();

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
        public Fragment getFragment(int position) {
            return fragments.get(position);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            fragments.add(position,createdFragment);

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
