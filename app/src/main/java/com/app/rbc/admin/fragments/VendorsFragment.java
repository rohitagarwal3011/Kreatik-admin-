package com.app.rbc.admin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.activities.SettingsActivity;
import com.app.rbc.admin.adapters.CustomVendorListAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Vendor;

import java.util.List;
import java.util.Set;


public class VendorsFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerView;
    private CustomVendorListAdapter adapter;
    private List<Vendor> vendors;
    private RelativeLayout empty_relative;
    SwipeRefreshLayout swipeRefreshLayout;

    final GestureDetector mGestureDetector = new GestureDetector(getContext(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendors, container, false);
        ((SettingsActivity)getActivity()).getSupportActionBar().setTitle("Vendor");

        initializeViews();
        return view;
    }

    private void initializeViews() {
        RelativeLayout add_vendor_container = (RelativeLayout) view.findViewById(R.id.add_vendor_container);
        TextView add_vendor_title = (TextView) view.findViewById(R.id.add_vendor_title);
        ImageView add_vendor_icon = (ImageView) view.findViewById(R.id.add_vendor_icon);
        Button add_vendor_next = (Button) view.findViewById(R.id.add_vendor_next);
        empty_relative = (RelativeLayout) view.findViewById(R.id.empty_relative);

        add_vendor_container.setOnClickListener(this);
        add_vendor_title.setOnClickListener(this);
        add_vendor_icon.setOnClickListener(this);
        add_vendor_next.setOnClickListener(this);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callVendorsFetchApi();
            }
        });

        // Initializing Recycler
        initializeCheckRecycler();
    }

    private void initializeCheckRecycler() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        vendors = Vendor.find(Vendor.class,"statestore != ?",1+"");
        setRecyclerView(vendors);

        if(vendors.size() == 0) {
            empty_relative.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            callVendorsFetchApi();
        }

        // Recycler Listener

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    //Log.e("Listener Id",jobList.get(a).getId()+"");
                    ((SettingsActivity)getContext()).setFragment(8,vendors.get(a).getId());


                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public void setRecyclerView(List<Vendor> vendors) {
        adapter = new CustomVendorListAdapter(getContext(),vendors);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_vendor_container:
            case R.id.add_vendor_icon:
            case R.id.add_vendor_title:
            case R.id.add_vendor_next:
                ((SettingsActivity)getActivity()).setFragment(8);
                break;
        }
    }

    private void callVendorsFetchApi() {
        APIController controller = new APIController(getContext(),40,SettingsActivity.ACTIVITY);
        controller.fetchVendors();
    }

    public void publishAPIResponse(int status,int code,String... message) {
        swipeRefreshLayout.setRefreshing(false);
        switch(status) {
            case 2 :
                if(Vendor.count(Vendor.class) == 0) {
                    empty_relative.setVisibility(View.VISIBLE);
                }
                else {
                    empty_relative.setVisibility(View.GONE);
                }
                refreshAdapter();
                break;
            case 0:
                Toast.makeText(getContext(),
                        message[0],
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void refreshAdapter() {
        adapter.refreshAdapter(Vendor.find(Vendor.class,"statestore != ?",1+""));
    }
}
