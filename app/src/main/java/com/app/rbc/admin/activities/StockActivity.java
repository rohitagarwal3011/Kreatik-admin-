package com.app.rbc.admin.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.PO_detail_adapter;
import com.app.rbc.admin.adapters.Stock_detail_adapter;
import com.app.rbc.admin.adapters.Stock_product_adapter;
import com.app.rbc.admin.adapters.Transaction_detail_adapter;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.fragments.Stock_po_details;
import com.app.rbc.admin.fragments.Stock_products;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.StockProductDetails;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.rbc.admin.utils.ChangeFragment.changeFragment;

public class StockActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.frame_main)
    FrameLayout frameMain;
    @BindView(R.id.fab)
    FloatingActionButton fab;
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
    public ViewPager mViewPager;

    StockProductDetails productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        // mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories(), Stock_categories.TAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock, menu);
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

    SweetAlertDialog pDialog;


    public void get_attendance_record(String product) {
        pDialog = new SweetAlertDialog(StockActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<StockProductDetails> call = apiServices.stock_product_list(product);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Attendance Activity ", "Get Attendance for Employee : " + call.request().toString() + "Product :" + product);
        call.enqueue(new Callback<StockProductDetails>() {
            @Override
            public void onResponse(Call<StockProductDetails> call, Response<StockProductDetails> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getApplicationContext(), TagsPreferences.PRODUCT_DETAILS, new Gson().toJson(response.body()));
                    productDetails = new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.PRODUCT_DETAILS), StockProductDetails.class);
                    AppUtil.logger("Product Details : ", AppUtil.getString(getApplicationContext(), TagsPreferences.PRODUCT_DETAILS));
                    show_tablayout();
                }

            }

            @Override
            public void onFailure(Call<StockProductDetails> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(StockActivity.this, "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_tablayout() {

        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        frameMain.setVisibility(View.GONE);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


    }

    public void hide_tablayout()
    {
        tabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        frameMain.setVisibility(View.VISIBLE);
        fab.hide();
    }

    public void show_po_details(String po)
    {
        changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_po_details().newInstance(po), Stock_po_details.TAG);
        hide_tablayout();
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
        public static final String TAG = "Placeholder Fragment";
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        Unbinder unbinder;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        int position;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt(ARG_SECTION_NUMBER);

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stock_details, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            unbinder = ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            switch (position) {
                case 0:
                    show_stock_details();
                    break;
                case 1:
                   show_PO_details();
                    break;
                case 2:
                   show_transaction_details();
                    break;

            }
        }

        public void show_stock_details() {

            List<StockProductDetails.StockDetail> stockDetail = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.PRODUCT_DETAILS), StockProductDetails.class).getStockDetails();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


            Stock_detail_adapter adapter = new Stock_detail_adapter(stockDetail,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        public void show_transaction_details() {
            List<StockProductDetails.TransactionDetail> transactionDetails = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.PRODUCT_DETAILS), StockProductDetails.class).getTransactionDetails();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


            Transaction_detail_adapter adapter = new Transaction_detail_adapter(transactionDetails,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public void show_PO_details() {
            List<StockProductDetails.PoDetail> poDetails = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.PRODUCT_DETAILS), StockProductDetails.class).getPoDetails();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


            PO_detail_adapter adapter = new PO_detail_adapter(poDetails,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        String po_selected;

        public void set_product_type(final String po) throws NullPointerException {
            po_selected = po;

            ((StockActivity)getActivity()).get_attendance_record("Ambuja Cement");
            changeFragment(getActivity().getSupportFragmentManager(), R.id.frame_main, new Stock_po_details().newInstance(po_selected), Stock_po_details.TAG);


//            new Timer().schedule(
//                    new TimerTask() {
//                        @Override
//                        public void run() {
//
//                            getActivity().runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//
////                                                            ((TaskActivity)getContext()).setToolbar(toolbar_string);
////                                                            details_page=true;
////                                                            selectEmployee.setVisibility(View.GONE);
////                                                            taskDetailsPage.setVisibility(View.VISIBLE);
////                                                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
////////stuff that updates ui
//
//                                                            }
//                                                        }
//
//                            );
//
//                        }
//                    },
//                    400
//            );

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

        String tag1,tag2,tag3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);

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
                    tag1=firstTag;
                    break;
                case 1:
                    String secondTag = createdFragment.getTag();
                    tag2=secondTag;
                    break;
                case 2:
                    String thirdTag= createdFragment.getTag();
                    tag3=thirdTag;
                    break;
            }
            // ... save the tags somewhere so you can reference them later
            return createdFragment;
        }

        public void onclick_method(final String po) {
            // do work on the referenced Fragments, but first check if they
            // even exist yet, otherwise you'll get an NPE.

            if (tag1 != null) {
                // m1stFragment.doWork();
            }

            if (tag2 != null) {


                show_po_details(po);


            }

            if (tag3 != null)
            {

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Stock Details";
                case 1:
                    return "Purchase Orders";
                case 2:
                    return "Transactions";
            }
            return null;
        }
    }
}
