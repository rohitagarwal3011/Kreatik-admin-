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
import com.app.rbc.admin.activities.SettingsActivity;
import com.app.rbc.admin.adapters.CustomSiteListAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Site;

import java.util.List;


public class SitesFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerView;
    private CustomSiteListAdapter adapter;
    private List<Site> sites;
    SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout empty_relative;

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
        view = inflater.inflate(R.layout.fragment_sites, container, false);
        ((SettingsActivity)getActivity()).getSupportActionBar().setTitle("Sites");

        initializeViews();
        return view;
    }

    private void initializeViews() {
        RelativeLayout add_factory_container = (RelativeLayout) view.findViewById(R.id.add_factory_container);
        TextView add_factory_title = (TextView) view.findViewById(R.id.add_factory_title);
        ImageView add_factory_icon = (ImageView) view.findViewById(R.id.add_factory_icon);
        Button add_factory_next = (Button) view.findViewById(R.id.add_factory_next);
        empty_relative = (RelativeLayout) view.findViewById(R.id.empty_relative);

        add_factory_container.setOnClickListener(this);
        add_factory_title.setOnClickListener(this);
        add_factory_icon.setOnClickListener(this);
        add_factory_next.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callSitesFetchApi();
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
        sites = Site.find(Site.class,"statestore != ?",1+"");
        setRecyclerView(sites);

        if(sites.size() == 0) {
            empty_relative.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            callSitesFetchApi();

        }

        // Recycler Listener

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    //Log.e("Listener Id",jobList.get(a).getId()+"");
                    ((SettingsActivity)getContext()).setFragment(7,sites.get(a).getId());


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

    public void setRecyclerView(List<Site> sites) {
        adapter = new CustomSiteListAdapter(getContext(),sites);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_factory_container:
            case R.id.add_factory_icon:
            case R.id.add_factory_title:
            case R.id.add_factory_next:
                ((SettingsActivity)getActivity()).setFragment(7);
                break;
        }
    }

    private void callSitesFetchApi() {
        APIController controller = new APIController(getContext(),30,SettingsActivity.ACTIVITY);
        controller.fetchSites();
    }

    public void publishAPIResponse(int status,int code,String... message) {
        swipeRefreshLayout.setRefreshing(false);
        switch(status) {
            case 2 :
                if(Site.count(Site.class) == 0) {
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
        adapter.refreshAdapter(Site.find(Site.class,"statestore != ?",1+""));
    }
}
