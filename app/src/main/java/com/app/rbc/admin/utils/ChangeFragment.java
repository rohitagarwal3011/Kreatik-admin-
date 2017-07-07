package com.app.rbc.admin.utils;

/**
 * Created by rohit on 29/7/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ChangeFragment {
    public static final String TAG = ChangeFragment.class.getSimpleName();

    public ChangeFragment() {
        // TODO Auto-generated constructor stub
    }

    public static void addFragment(FragmentManager fm,int container, Fragment target,String TAG)
    {

        Fragment fragment = target;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;
        fragmentManager.executePendingTransactions();
        fragmentManager.beginTransaction()
                .add(container, fragment,TAG)
                .addToBackStack("replaced_with_"+TAG)
                .commit();
    }


    public static void changeFragment(FragmentManager fm, int container, Fragment target,String tag) {


        Fragment fragment = target;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;

        fragmentManager.executePendingTransactions();
        fragmentManager.beginTransaction()
                .replace(container, fragment,tag)
                .addToBackStack(null)
                .commit();

    }

    public void changeFragmentWithExtra(FragmentManager fm, int container, Fragment target, Bundle bundle) {

        Fragment fragment = target;
        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;
        fragmentManager.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
