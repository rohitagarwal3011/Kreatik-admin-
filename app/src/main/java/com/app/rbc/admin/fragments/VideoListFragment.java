package com.app.rbc.admin.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.YoutubeActivity;
import com.app.rbc.admin.adapters.CustomVideoListAdapter;
import com.app.rbc.admin.models.db.models.Video;

import java.util.List;

public class VideoListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Video> videos;
    final GestureDetector mGestureDetector = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
    private CustomVideoListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_video_list, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        // Swipe Refresh Layout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callVideofetchApi();
            }
        });
        initializeCheckRecycler();
    }

    private void initializeCheckRecycler() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        videos = Video.listAll(Video.class);

        setRecyclerView(videos);

        if(videos.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            callVideofetchApi();
        }

        // Recycler Listener

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);
                    Log.e("Id",videos.get(a).getVideoid());
                    watchYoutubeVideo(videos.get(a).getVideoid());



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


    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public static VideoListFragment newInstance(Object... data) {
        VideoListFragment videoListFragment = new VideoListFragment();
        return videoListFragment;
    }
    private void callVideofetchApi() {
        ((YoutubeActivity)getActivity()).YOUTUBE_REQUEST = 1;
        ((YoutubeActivity)getActivity()).getResultsFromApi();
    }

    public void setRecyclerView(List<Video> videos) {
        adapter = new CustomVideoListAdapter(getActivity(),videos);
        recyclerView.setAdapter(adapter);
    }

    public void refreshRecycler() {
        videos = Video.listAll(Video.class);
        swipeRefreshLayout.setRefreshing(false);
        adapter.refreshAdapter(videos);
    }
}
