package com.app.rbc.admin.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.app.rbc.admin.activities.SiteOverviewActivity;
import com.app.rbc.admin.adapters.CustomSiteListAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Site;

import java.util.List;


public class SiteOverviewListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private CustomSiteListAdapter adapter;
    private List<Site> sites;
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
        view = inflater.inflate(R.layout.fragment_site_overview_list, container, false);
        ((SiteOverviewActivity)getActivity()).getSupportActionBar().setTitle("Sites");
        ((SiteOverviewActivity)getActivity()).getSupportActionBar().setElevation(0);
        ((SiteOverviewActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        initializeViews();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeViews() {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callSitesFetchApi();
            }
        });

        initializeCheckRecycler();

    }

    private void initializeCheckRecycler() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sites = Site.listAll(Site.class);
        setRecyclerView(sites);

        if(sites.size() == 0) {
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
                    Log.e("Listener Id",sites.get(a).getId()+"");
                    ((SiteOverviewActivity)getContext()).setFragment(2,sites.get(a).getId());


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

    private void callSitesFetchApi() {
        APIController controller = new APIController(getContext(),11, SiteOverviewActivity.ACTIVITY);
        controller.fetchSites();
    }

    public void publishAPIResponse(int status,int code,String... message) {
        swipeRefreshLayout.setRefreshing(false);
        switch(status) {
            case 2 :
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
        adapter.refreshAdapter(Site.listAll(Site.class));
    }


}
