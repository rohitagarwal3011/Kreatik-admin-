package com.app.rbc.admin.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.SiteOverviewActivity;
import com.app.rbc.admin.adapters.CustomSiteOverviewPagerAdapter;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.site_overview.Stock;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SiteOverviewFragment extends Fragment {

    private View view;
    public long site;
    private SweetAlertDialog sweetAlertDialog;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NestedScrollView nestedScrollView;
    private Site s;
    private TextView site_type,site_location,site_incharge;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_site_overview, container, false);

        s = Site.findById(Site.class,site);
        ((SiteOverviewActivity)getActivity()).getSupportActionBar().setTitle(s.getName());

        initializeViews();
        return view;
    }




    private void initializeViews() {
        site_type = (TextView) view.findViewById(R.id.site_type);
        site_location = (TextView) view.findViewById(R.id.site_location);
        site_incharge = (TextView) view.findViewById(R.id.site_incharge);


        Employee incharge = Employee.find(Employee.class,"userid = ?",s.getIncharge()).get(0);

        site_type.setText(s.getType());
        site_incharge.setText(incharge.getUserName());
        site_location.setText(s.getLocation());
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        nestedScrollView.setFillViewport (true);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        fragments = new ArrayList<>();

        tabLayout.setTabTextColors(
                ContextCompat.getColor(getContext(), R.color.unselectedtext),
                ContextCompat.getColor(getContext(), R.color.white)
        );

        SiteOverviewPlaceholderFragment stocks = new SiteOverviewPlaceholderFragment();
        stocks.site = site;
        stocks.position = 0;
        fragments.add(0,stocks);

        SiteOverviewPlaceholderFragment requirements = new SiteOverviewPlaceholderFragment();
        requirements.site = site;
        requirements.position = 1;
        fragments.add(1,requirements);

        SiteOverviewPlaceholderFragment transactions = new SiteOverviewPlaceholderFragment();
        transactions.site = site;
        transactions.position = 2;
        fragments.add(2,transactions);

        callSiteOverviewApi();
        setCustomOverviewPagerAdapter();

    }

    private void setCustomOverviewPagerAdapter() {
        CustomSiteOverviewPagerAdapter customSiteOverviewPagerAdapter = new CustomSiteOverviewPagerAdapter(
                getChildFragmentManager(),viewPager,tabLayout,fragments);
        viewPager.setAdapter(customSiteOverviewPagerAdapter);
        viewPager.invalidate();
    }


    private void callSiteOverviewApi() {
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),21, SiteOverviewActivity.ACTIVITY);
        controller.siteOverview(site);
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 :
                viewPager.invalidate();
                break;
            case 0:
                Toast.makeText(getContext(),
                        message[0],
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
