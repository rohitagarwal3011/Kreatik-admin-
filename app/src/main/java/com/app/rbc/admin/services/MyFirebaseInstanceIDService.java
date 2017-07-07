package com.app.rbc.admin.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.Constant;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Rohit on 7/04/17.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor preEditor;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor = prefs.edit();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AppUtil.logger(TAG,"Token :" + refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        // sending reg id to your server
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constant.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }



    private void storeRegIdInPref(String token) {
        AppUtil.putString (getApplicationContext(), TagsPreferences.GCM_TOKEN,token);}
}
