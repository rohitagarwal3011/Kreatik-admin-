package com.app.rbc.admin.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeet on 19/9/17.
 */

public class CustomSiteOverviewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Menu menu;
    private String[] titles = {"Stocks", "Requirements", "Transactions"};

    public CustomSiteOverviewPagerAdapter(FragmentManager fm, ViewPager pager, TabLayout tabLayout,
                                   List<Fragment> fragments) {
        super(fm);
        this.pager = pager;
        this.tabLayout = tabLayout;
        this.fragments = new ArrayList<>();
        tabLayout.setupWithViewPager(pager);
    }

    public void addFragment(Fragment fragment,int position) {
        this.fragments.add(position,fragment);
    }
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void refreshAdapter(List<Fragment> fragments) {
        this.fragments.clear();
        this.fragments.addAll(fragments);
        notifyDataSetChanged();
    }
}
