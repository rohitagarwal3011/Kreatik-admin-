package com.app.rbc.admin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.activities.SettingsActivity;
import com.app.rbc.admin.adapters.CustomSetingsListAdapter;


public class SettingsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private String[] settings_titles;
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
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ((SettingsActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        ((SettingsActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeUI();
        return view;
    }


    private void initializeUI() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        settings_titles = getActivity().getResources().getStringArray(R.array.settings_titles);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int a=rv.getChildPosition(child);

                    ((SettingsActivity)getActivity()).setFragment(a+1);



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

        setRecyclerView(settings_titles);
    }


    public void setRecyclerView(String[] settings_titles) {
        CustomSetingsListAdapter adapter = new CustomSetingsListAdapter(getContext(),settings_titles);
        recyclerView.setAdapter(adapter);
    }


}
