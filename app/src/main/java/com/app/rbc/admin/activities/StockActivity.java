package com.app.rbc.admin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.adapters.PO_detail_adapter;
import com.app.rbc.admin.adapters.Stock_detail_adapter;
import com.app.rbc.admin.adapters.Transaction_detail_adapter;
import com.app.rbc.admin.fragments.Attendance_all;
import com.app.rbc.admin.fragments.Employee_list;
import com.app.rbc.admin.fragments.Product_selection;
import com.app.rbc.admin.fragments.Requirement_fulfill_task;
import com.app.rbc.admin.fragments.Stock_add_po_details;
import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.fragments.Stock_po_create_task;
import com.app.rbc.admin.fragments.Stock_po_details;
import com.app.rbc.admin.fragments.Vendor_list;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.StockCategoryDetails;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.ChangeFragment;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.backends.pipeline.Fresco;
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

public class StockActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

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

     public String category_selected ="";
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

    StockCategoryDetails productDetails;
    private Menu menu;

    public List<Order> po_task = new ArrayList<>();
    public List<Order> po_details = new ArrayList<>();

    public Bundle task_finalform,details_finalform;



    public static Boolean show_tabs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);
        Fresco.initialize(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

//                ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, Employee_list.newInstance(category_selected,Stock_po_create_task.TAG),Stock_po_create_task.TAG);
               // ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, Product_selection.newInstance(category_selected,Stock_po_create_task.TAG),Stock_po_create_task.TAG);
                show_dialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        hide_tablayout();
        changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories().newInstance("StockActivity"), Stock_categories.TAG);
    }

    public void show_dialog()
    {
        final Dialog dialog = new Dialog(StockActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_attendance_mark_or_edit);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView add_po_details = (TextView) dialog.findViewById(R.id.mark_attendance);
        add_po_details.setText("Add PO details");
        TextView assign_po_task = (TextView) dialog.findViewById(R.id.modify_attendance);
        assign_po_task.setText("Assign a PO task");

        add_po_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hide_tablayout();
                ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, Vendor_list.newInstance(category_selected),Stock_add_po_details.TAG);

            }
        });

        assign_po_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hide_tablayout();
                ChangeFragment.changeFragment(getSupportFragmentManager(),R.id.frame_main, Employee_list.newInstance(category_selected),Stock_po_create_task.TAG);
            }
        });



        dialog.show();
    }

    public void setToolbar(String title)
    {
        toolbar.setTitle(title);
    }




    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(tabLayout.getVisibility() == View.VISIBLE)
        {
            hide_tablayout();
            //changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_categories().newInstance("StockActivity"), Stock_categories.TAG);
        }
        else {

            if(getSupportFragmentManager().findFragmentByTag(Stock_categories.TAG).isVisible())
            {
                getSupportFragmentManager().popBackStackImmediate();
                Intent intent = new Intent(StockActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else if (show_tabs)
            {
                show_tablayout();
                getSupportFragmentManager().popBackStackImmediate();
            }

//            else if(getSupportFragmentManager()..isVisible())
//            {
//                show_tablayout();
//            }
//            else if(getSupportFragmentManager().findFragmentByTag(Stock_add_po_details.TAG).isVisible())
//            {
//                show_tablayout();
//            }
            else {
                getSupportFragmentManager().popBackStackImmediate();
               // super.onBackPressed();

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        this.menu = menu;

        if(!show_tabs)
        {
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);

        }
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.filter) {
            PlaceholderFragment placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter.getFragment(
                    mViewPager.getCurrentItem());
           switch (mViewPager.getCurrentItem()) {
               case 0:placeholderFragment.setStockDetailFilter();
                   break;
               case 1:placeholderFragment.setStockPOFilter();
                   break;
               case 2:placeholderFragment.setStockTransFilter();
                   break;
           }
        }
        if(id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    SweetAlertDialog pDialog;


    public void get_product_details(String category) {
        pDialog = new SweetAlertDialog(StockActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        category_selected = category;

        final ApiServices apiServices = RetrofitClient.getApiService();
        // AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
        Call<StockCategoryDetails> call = apiServices.stock_category_list(category);
        //AppUtil.logger("Date :", String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        AppUtil.logger("Stock Activity ", "Get Product details : " + call.request().toString() + "Category :" + category);
        call.enqueue(new Callback<StockCategoryDetails>() {
            @Override
            public void onResponse(Call<StockCategoryDetails> call, Response<StockCategoryDetails> response) {
                pDialog.dismiss();
                if (response.body().getMeta().getStatus() == 2) {


                    AppUtil.putString(getApplicationContext(), TagsPreferences.CATEGORY_DETAILS, new Gson().toJson(response.body()));
                    productDetails = new Gson().fromJson(AppUtil.getString(getApplicationContext(), TagsPreferences.CATEGORY_DETAILS), StockCategoryDetails.class);
                    AppUtil.logger("Product Details : ", AppUtil.getString(getApplicationContext(), TagsPreferences.CATEGORY_DETAILS));
                    show_tablayout();
                }

            }

            @Override
            public void onFailure(Call<StockCategoryDetails> call1, Throwable t) {

                pDialog.dismiss();


                AppUtil.showToast(StockActivity.this, "Network Issue. Please check your connectivity and try again");
            }
        });
    }

    public void show_tablayout() {

        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        frameMain.setVisibility(View.GONE);
        fab.show();

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.filter).setVisible(true);

    }

    public void hide_tablayout()
    {
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

    public void show_po_details(String po)
    {

        changeFragment(getSupportFragmentManager(), R.id.frame_main, new Stock_po_details().newInstance(po), Stock_po_details.TAG);
        hide_tablayout();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        PlaceholderFragment placeholderFragment = (PlaceholderFragment) mSectionsPagerAdapter
                .getFragment(mViewPager.getCurrentItem());
        switch (mViewPager.getCurrentItem()) {
            case 0 :
                List<StockCategoryDetails.StockDetail> stockDetail = new Gson().
                        fromJson(AppUtil.getString(this.getApplicationContext(), TagsPreferences.CATEGORY_DETAILS),
                                StockCategoryDetails.class).getStockDetails();
                List<StockCategoryDetails.StockDetail> searchDetails = new ArrayList<>();

                for(int i = 0 ; i < stockDetail.size() ; i++) {
                    if(stockDetail.get(i).getProduct().toLowerCase().contains(newText.toLowerCase()) || stockDetail.get(i)
                            .getWhere().toLowerCase().contains(newText.toLowerCase())) {
                        searchDetails.add(stockDetail.get(i));
                    }
                }
                placeholderFragment.set_show_stock_recycler(searchDetails);
                break;

            case 1 :
                List<StockCategoryDetails.PoDetail> poDetails = new Gson().
                        fromJson(AppUtil.getString(this .getApplicationContext(),
                                TagsPreferences.CATEGORY_DETAILS), StockCategoryDetails.class).getPoDetails();
                List<StockCategoryDetails.PoDetail> searchPO = new ArrayList<>();

                for(int i = 0 ; i < poDetails.size() ; i++) {
                    StockCategoryDetails.PoDetail.Detail detail = poDetails.get(i).getDetails().get(0);

                    if(detail.getTitle().toLowerCase().contains(newText.toLowerCase()) || detail.getStatus()
                            .toLowerCase().contains(newText.toLowerCase()) ||
                            detail.getCreatedBy().toLowerCase().equals(newText.toLowerCase())) {
                        searchPO.add(poDetails.get(i));
                    }
                }
                placeholderFragment.set_show_PO_recycler(searchPO);
                break;

            case 2 :
                List<StockCategoryDetails.TransactionDetail> transactionDetails = new Gson().
                        fromJson(AppUtil.getString(this.getApplicationContext(), TagsPreferences.CATEGORY_DETAILS),
                                StockCategoryDetails.class).getTransactionDetails();

                List<StockCategoryDetails.TransactionDetail> searchTrans = new ArrayList<>();

                for(int i = 0 ; i < transactionDetails.size() ; i++) {
                    StockCategoryDetails.TransactionDetail.Detail detail = transactionDetails.get(i).getDetails().get(0);
                    if(detail.getChallanNum().toLowerCase().contains(newText.toLowerCase())
                            || detail.getDestination().toLowerCase().contains(newText.toLowerCase())
                            || detail.getSource().toLowerCase().contains(newText.toLowerCase())) {
                        searchTrans.add(transactionDetails.get(i));
                    }
                }
                placeholderFragment.set_show_transaction_recycler(searchTrans);
                break;
        }

        return true;
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final String TAG = "Placeholder Fragment";
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;

        @BindView(R.id.empty_relative)
        RelativeLayout empty_relative;

        Unbinder unbinder;
        private View view;
        private List<StockCategoryDetails.StockDetail> stockDetail;
        private List<StockCategoryDetails.TransactionDetail> transactionDetails;
        private List<StockCategoryDetails.PoDetail> poDetails;



        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        int position;
        SwipeRefreshLayout swipeRefreshLayout;

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
            view = inflater.inflate(R.layout.fragment_stock_details, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            unbinder = ButterKnife.bind(this, view);
            swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ((StockActivity)getActivity()).get_product_details(
                            ((StockActivity)getActivity()).category_selected);
                    recyclerView.invalidate();
                }
            });
            return view;
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

            stockDetail = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.CATEGORY_DETAILS), StockCategoryDetails.class).getStockDetails();

            if(stockDetail.size() == 0) {
                empty_relative.setVisibility(View.VISIBLE);
            }
            else {
                empty_relative.setVisibility(View.GONE);
            }

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

        private void set_show_stock_recycler(List<StockCategoryDetails.StockDetail> stockDetail) {
            Stock_detail_adapter adapter = new Stock_detail_adapter(stockDetail,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public void show_transaction_details() {
            transactionDetails = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.CATEGORY_DETAILS), StockCategoryDetails.class).getTransactionDetails();
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

        private void set_show_transaction_recycler(List<StockCategoryDetails.TransactionDetail> transactionDetails) {
            Transaction_detail_adapter adapter = new Transaction_detail_adapter(transactionDetails,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public void show_PO_details() {
            poDetails = new Gson().fromJson(AppUtil.getString(getContext().getApplicationContext(), TagsPreferences.CATEGORY_DETAILS), StockCategoryDetails.class).getPoDetails();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
           // recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


            PO_detail_adapter adapter = new PO_detail_adapter(poDetails,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        private void set_show_PO_recycler(List<StockCategoryDetails.PoDetail> poDetails) {
            PO_detail_adapter adapter = new PO_detail_adapter(poDetails,getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public void setStockDetailFilter() {
            final Dialog dialog = new Dialog(getActivity());

            View dialogView= getActivity().getLayoutInflater().inflate(R.layout.dialog_filter_stock_detail,null);

            final Spinner product_spinner = (Spinner) dialogView.findViewById(R.id.product_spinner);
            final Spinner site_spinner = (Spinner) dialogView.findViewById(R.id.site_spinner);
            final Spinner stock_type_spinner = (Spinner) dialogView.findViewById(R.id.stock_type_spinner);
            Button submit = (Button) dialogView.findViewById(R.id.submit);


            final List<String> products = new ArrayList<>();
            final List<String> sites = new ArrayList<>();
            final List<String> stock_types = new ArrayList<>();

            products.add(0,"All");
            sites.add(0,"All");
            stock_types.add(0,"All");


            for(int i = 0 ; i < stockDetail.size() ; i++) {
                int j;
                for(j = 0 ; j < products.size() ; j++) {
                    if(products.get(j).equalsIgnoreCase(stockDetail.get(i).getProduct())) {
                        break;
                    }
                }
                if(j == products.size()) {
                    products.add(stockDetail.get(i).getProduct());
                }

                for(j = 0 ; j < sites.size() ; j++) {
                    if(sites.get(j).equalsIgnoreCase(stockDetail.get(i).getWhere())) {
                        break;
                    }
                }
                if(j == sites.size()) {
                    sites.add(stockDetail.get(i).getWhere());
                }

                for(j = 0 ; j < stock_types.size() ; j++) {
                    if(stock_types.get(j).equalsIgnoreCase(stockDetail.get(i).getMstock_type())) {
                        break;
                    }
                }
                if(j == stock_types.size()) {
                    stock_types.add(stockDetail.get(i).getMstock_type());
                }
            }

            ArrayAdapter<String> product_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    products);
            product_spinner.setAdapter(product_adapter);

            ArrayAdapter<String> site_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    sites);
            site_spinner.setAdapter(site_adapter);

            ArrayAdapter<String> stock_type_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    stock_types);
            stock_type_spinner.setAdapter(stock_type_adapter);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<StockCategoryDetails.StockDetail> filtered = new ArrayList<>();

                    for(int i = 0 ; i < stockDetail.size() ; i++) {
                        if(product_spinner.getSelectedItemPosition() != 0) {
                            if(!(stockDetail.get(i).getProduct().equalsIgnoreCase(products
                                    .get(product_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(site_spinner.getSelectedItemPosition() != 0) {
                            if(!(stockDetail.get(i).getWhere().equalsIgnoreCase(sites
                                    .get(site_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(stock_type_spinner.getSelectedItemPosition() != 0) {
                            if(!(stockDetail.get(i).getMstock_type().equalsIgnoreCase(stock_types
                                    .get(stock_type_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        filtered.add(stockDetail.get(i));
                    }
                    set_show_stock_recycler(filtered);
                }
            });


            dialog.setContentView(dialogView);
            dialog.show();
        }

        public void setStockPOFilter() {
            final Dialog dialog = new Dialog(getActivity());

            View dialogView= getActivity().getLayoutInflater().inflate(R.layout.dialog_filter_stock_po,null);

            final Spinner category_spinner = (Spinner) dialogView.findViewById(R.id.category_spinner);
            final Spinner status_spinner = (Spinner) dialogView.findViewById(R.id.status_spinner);
            final Spinner created_by_spinner = (Spinner) dialogView.findViewById(R.id.created_by_spinner);
            Button submit = (Button) dialogView.findViewById(R.id.submit);


            final List<String> categories = new ArrayList<>();
            final List<String> statuses = new ArrayList<>();
            final List<String> created_bys = new ArrayList<>();

            categories.add(0,"All");
            statuses.add(0,"All");
            created_bys.add(0,"All");


            for(int i = 0 ; i < poDetails.size() ; i++) {
                StockCategoryDetails.PoDetail.Detail detail = poDetails.get(i).getDetails().get(0);
                int j;
                for(j = 0 ; j < categories.size() ; j++) {


                    if(categories.get(j).equalsIgnoreCase(detail.getCategory())) {
                        break;
                    }
                }
                if(j == categories.size()) {
                    categories.add(detail.getCategory());
                }

                for(j = 0 ; j < statuses.size() ; j++) {
                    if(statuses.get(j).equalsIgnoreCase(detail.getStatus())) {
                        break;
                    }
                }
                if(j == statuses.size()) {
                    statuses.add(detail.getStatus());
                }

                for(j = 0 ; j < created_bys.size() ; j++) {
                    if(created_bys.get(j).equalsIgnoreCase(detail.getCreatedBy())) {
                        break;
                    }
                }
                if(j == created_bys.size()) {
                    created_bys.add(detail.getCreatedBy());
                }
            }

            ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    categories);
            category_spinner.setAdapter(category_adapter);

            ArrayAdapter<String> statuses_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    statuses);
            status_spinner.setAdapter(statuses_adapter);

            ArrayAdapter<String> created_bys_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    created_bys);
            created_by_spinner.setAdapter(created_bys_adapter);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<StockCategoryDetails.PoDetail> filtered = new ArrayList<>();

                    for(int i = 0 ; i < poDetails.size() ; i++) {
                        StockCategoryDetails.PoDetail.Detail detail = poDetails.get(i).getDetails().get(0);
                        if(category_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getCategory().equalsIgnoreCase(categories
                                    .get(category_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(status_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getStatus().equalsIgnoreCase(statuses
                                    .get(status_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(created_by_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getCreatedBy().equalsIgnoreCase(created_bys
                                    .get(created_by_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        filtered.add(poDetails.get(i));
                    }
                    set_show_PO_recycler(filtered);
                }
            });


            dialog.setContentView(dialogView);
            dialog.show();
        }

        public void setStockTransFilter() {
            final Dialog dialog = new Dialog(getActivity());

            View dialogView= getActivity().getLayoutInflater().inflate(R.layout.dialog_stock_trans_filter,null);

            final Spinner vehicle_number_spinner = (Spinner) dialogView.findViewById(R.id.vehicle_number_spinner);
            final Spinner destination_type_spinner = (Spinner) dialogView.findViewById(R.id.destination_type_spinner);
            final Spinner status_spinner = (Spinner) dialogView.findViewById(R.id.status_spinner);
            Button submit = (Button) dialogView.findViewById(R.id.submit);


            final List<String> vehicle_numbers = new ArrayList<>();
            final List<String> destination_types = new ArrayList<>();
            final List<String> statuses = new ArrayList<>();

            vehicle_numbers.add(0,"All");
            statuses.add(0,"All");
            destination_types.add(0,"All");


            for(int i = 0 ; i < transactionDetails.size() ; i++) {
                StockCategoryDetails.TransactionDetail.Detail detail = transactionDetails.get(i).getDetails().get(0);
                int j;
                for(j = 0 ; j < vehicle_numbers.size() ; j++) {
                    if(vehicle_numbers.get(j).equalsIgnoreCase(detail.getVehicleNumber())) {
                        break;
                    }
                }
                if(j == vehicle_numbers.size()) {
                    vehicle_numbers.add(detail.getVehicleNumber());
                }

                for(j = 0 ; j < statuses.size() ; j++) {
                    if(statuses.get(j).equalsIgnoreCase(detail.getStatus())) {
                        break;
                    }
                }
                if(j == statuses.size()) {
                    statuses.add(detail.getStatus());
                }

                for(j = 0 ; j < destination_types.size() ; j++) {
                    if(destination_types.get(j).equalsIgnoreCase(detail.getDestType())) {
                        break;
                    }
                }
                if(j == destination_types.size()) {
                    destination_types.add(detail.getDestType());
                }
            }

            ArrayAdapter<String> vehicle_number_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    vehicle_numbers);
            vehicle_number_spinner.setAdapter(vehicle_number_adapter);

            ArrayAdapter<String> statuses_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    statuses);
            status_spinner.setAdapter(statuses_adapter);

            ArrayAdapter<String> destination_type_adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.custom_spinner_text,
                    destination_types);
            destination_type_spinner.setAdapter(destination_type_adapter);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<StockCategoryDetails.TransactionDetail> filtered = new ArrayList<>();

                    for(int i = 0 ; i < transactionDetails.size() ; i++) {
                        StockCategoryDetails.TransactionDetail.Detail detail = transactionDetails.get(i).getDetails().get(0);
                        if(vehicle_number_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getVehicleNumber().equalsIgnoreCase(vehicle_numbers
                                    .get(vehicle_number_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(status_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getStatus().equalsIgnoreCase(statuses
                                    .get(status_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        if(destination_type_spinner.getSelectedItemPosition() != 0) {
                            if(!(detail.getDestType().equalsIgnoreCase(destination_types
                                    .get(destination_type_spinner.getSelectedItemPosition()))))  {
                                continue;
                            }
                        }
                        filtered.add(transactionDetails.get(i));
                    }

                    if(filtered.size() == 0) {
                        empty_relative.setVisibility(View.GONE);
                    }
                    else {
                        empty_relative.setVisibility(View.VISIBLE);
                    }
                    set_show_transaction_recycler(filtered);
                }
            });


            dialog.setContentView(dialogView);
            dialog.show();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String tag1,tag2,tag3;
        private List<Fragment> fragments = new ArrayList<>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position);

        }

        public Fragment getFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            fragments.add(position,createdFragment);
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
