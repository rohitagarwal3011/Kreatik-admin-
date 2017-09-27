package com.app.rbc.admin.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.VideoListFragment;
import com.app.rbc.admin.models.db.models.Video;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.SearchListResponse;
import com.orm.SugarContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class YoutubeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };

    public int YOUTUBE_REQUEST;

    private VideoListFragment videoListFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SugarContext.init(this);
        Fresco.initialize(this);
        setFragment(1);
    }



    public void setFragment(int code,Object... data) {
        switch (code) {
            case 1 :
                videoListFragment = VideoListFragment.newInstance(data);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, videoListFragment)
                        .commit();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void getResultsFromApi() {
        Log.e("Youtube","getResultsFromApi");
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Toast.makeText(this,
                    "No network connection available.",
                    Toast.LENGTH_SHORT).show();
        } else {
            new MakeRequestTask(mCredential,YOUTUBE_REQUEST).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                YoutubeActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;
        private int code;
        MakeRequestTask(GoogleAccountCredential credential, int code) {
            Log.e("Youtube","MakeRequestTask");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("YouTube Data API Android Quickstart")
                    .build();
            this.code = code;
        }

        /**
         * Background task to call YouTube Data API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            Log.e("Youtube","doInBackground");

            String response = "";
            try {
                switch (code) {
                    case 1: response = getVideos();
                        break;
                }
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return  response;
        }

        private String getVideos() throws IOException {
            try {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("part", "snippet");
                parameters.put("maxResults", "50");
                parameters.put("forMine", "true");
                parameters.put("type", "video");

                YouTube.Search.List searchListMineRequest = mService.search().list(parameters.get("part").toString());
                if (parameters.containsKey("maxResults")) {
                    searchListMineRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
                }

                if (parameters.containsKey("forMine") && parameters.get("forMine") != "") {
                    boolean forMine = (parameters.get("forMine") == "true") ? true : false;
                    searchListMineRequest.setForMine(forMine);
                }

                if (parameters.containsKey("q") && parameters.get("q") != "") {
                    searchListMineRequest.setQ(parameters.get("q").toString());
                }

                if (parameters.containsKey("type") && parameters.get("type") != "") {
                    searchListMineRequest.setType(parameters.get("type").toString());
                }

                SearchListResponse response = searchListMineRequest.execute();
                Log.e("Response",response.toPrettyString());
                return  response.toString();
            }
            catch (UserRecoverableAuthIOException e) {
                startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            }
            catch (Exception e) {
                Log.e("Video Item", e.toString());
            }
            return null;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String output) {
            try {
                if (output == null || output.length() == 0) {

                    Toast.makeText(YoutubeActivity.this,
                            "No results returned.",
                            Toast.LENGTH_SHORT).show();
                    switch (code) {
                        case 1 : videoListFragment.refreshRecycler();
                            break;
                    }
                } else {

                    switch (code) {
                        case 1:
                            parseVideos(output);
                            videoListFragment.refreshRecycler();
                            break;
                    }
                }
            }catch (Exception e) {
                Log.e("Error",e.toString());
            }
        }

        @Override
        protected void onCancelled() {

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            YoutubeActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(YoutubeActivity.this,
                            "Request Cancelled",
                            Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(YoutubeActivity.this,
                        "Request Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }


        private void parseVideos(String response) {
            try {
                Video.deleteAll(Video.class);
                JSONObject responseObj = new JSONObject(response);

                JSONArray itemsArray = responseObj.getJSONArray("items");

                for(int i = 0 ; i < itemsArray.length() ; i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    Video video = new Video();

                    video.setVideoid(item.getJSONObject("id").getString("videoId"));

                    JSONObject snippet = item.getJSONObject("snippet");

                    video.setPublishedat(snippet.getString("publishedAt"));
                    video.setChannel(snippet.getString("channelId"));
                    video.setTitle(snippet.getString("title"));
                    video.setDescription(snippet.getString("description"));

                    if(snippet.has("thumbnails")) {
                        JSONObject thumbnailsObj = snippet.getJSONObject("thumbnails");

                        if(thumbnailsObj.has("high")) {
                            video.setThumbnail(thumbnailsObj.getJSONObject("high")
                                    .getString("url"));
                        }

                        else if(thumbnailsObj.has("default")) {
                            video.setThumbnail(thumbnailsObj.getJSONObject("default")
                                    .getString("url"));
                        }
                    }
                    video.save();
                }

            }
            catch (Exception e) {
                Log.e("Video Parsing Error",e.toString());
            }
        }
    }

}
