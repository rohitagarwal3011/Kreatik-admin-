package com.app.rbc.admin.activities;




import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.rbc.admin.Manifest;
import com.app.rbc.admin.R;
import com.app.rbc.admin.fragments.AddEmployeeFragment;
import com.app.rbc.admin.fragments.AddProductFragment;
import com.app.rbc.admin.fragments.AddSiteFragment;
import com.app.rbc.admin.fragments.AddVendorFragment;
import com.app.rbc.admin.fragments.CategoriesProductFragment;
import com.app.rbc.admin.fragments.EmployeesFragment;
import com.app.rbc.admin.fragments.MyDetailsFragment;
import com.app.rbc.admin.fragments.SettingsFragment;
import com.app.rbc.admin.fragments.SitesFragment;
import com.app.rbc.admin.fragments.VendorsFragment;
import com.app.rbc.admin.utils.AppUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orm.SugarContext;

public class IndentRegisterActivity extends AppCompatActivity {
    private MyDetailsFragment myDetailsFragment;
    private AddEmployeeFragment addEmployeeFragment;
    private AddSiteFragment addSiteFragment;
    private AddVendorFragment addVendorFragment;
    private CategoriesProductFragment categoriesProductFragment;
    private AddProductFragment addProductFragment;
    private EmployeesFragment employeesFragment;
    private SitesFragment sitesFragment;
    private VendorsFragment vendorsFragment;
    private Toolbar toolbar;


    public static final int ACTIVITY = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_registers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SugarContext.init(this);
        Fresco.initialize(this);
        MultiDex.install(this);

        setFragment(0);
    }

    public void setFragment(int code,Object... data) {
        FragmentManager fm = getSupportFragmentManager();
        switch (code) {
            case 0 :
                    fm.beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .commit();
                break;
            case 1 :
                myDetailsFragment = new MyDetailsFragment();
                fm.beginTransaction()
                        .replace(R.id.fragment_container, myDetailsFragment).addToBackStack(null)
                        .commit();
                break;
            case 2 :
                employeesFragment = new EmployeesFragment();
                    fm.beginTransaction()
                        .replace(R.id.fragment_container, employeesFragment).addToBackStack(null)
                        .commit();
                break;
            case 3 :
                sitesFragment = new SitesFragment();
                fm.beginTransaction()
                        .replace(R.id.fragment_container, sitesFragment).addToBackStack(null)
                        .commit();
                break;
            case 4 :
                vendorsFragment = new VendorsFragment();
               fm.beginTransaction()
                        .replace(R.id.fragment_container, vendorsFragment).addToBackStack(null)
                        .commit();
                break;
            case 5:
                categoriesProductFragment = new CategoriesProductFragment();
                fm.beginTransaction()
                        .replace(R.id.fragment_container, categoriesProductFragment).addToBackStack("null")
                        .commit();
                break;
            case 6:
                addEmployeeFragment = AddEmployeeFragment.newInstance(data);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, addEmployeeFragment).addToBackStack("null")
                        .commit();
                break;
            case 7:
                addSiteFragment = AddSiteFragment.newInstance(data);
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, addSiteFragment).addToBackStack("null")
                            .commit();
                break;
            case 8:
                addVendorFragment = AddVendorFragment.newInstance(data);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, addVendorFragment).addToBackStack("null")
                        .commit();
                break;
            case 9:
                addProductFragment = AddProductFragment.newInstance(String.valueOf(data[0]));
                fm.beginTransaction()
                        .replace(R.id.fragment_container, addProductFragment).addToBackStack("null")
                        .commit();
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
                onBackPressed();
                return true;
            case R.id.playlist:
                startYoutubeIntent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.indent_register_menu, menu);
        return true;
    }


    private void startYoutubeIntent() {
        Intent intent = new Intent(IndentRegisterActivity.this, YoutubeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate();
    }


    public void checkPermission(int permissionCode) {
        switch (permissionCode) {
            case 6: if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        permissionCode);
                break;

            }
            else {
                addEmployeeFragment.startImageGalleryIntent();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 6: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    addEmployeeFragment.startImageGalleryIntent();

                } else {

                    Toast.makeText(this,
                            "Permission Denied!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }



    public void publishAPIResponse(int status,int code,String... message) {
        switch (code) {
            case 12:
            case 14:
            case 15:
                myDetailsFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 20 :
                employeesFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 30 :
                sitesFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 40 :
                vendorsFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 50:
            case 51:
                categoriesProductFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 60 :
            case 61:
            case 62:
                addEmployeeFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 70:
            case 71:
            case 72:
                addSiteFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 80:
            case 81:
            case 82:
                addVendorFragment.publishAPIResponse(status,code,message[0]);
                break;
            case 90:
            case 92:
            case 93:
                addProductFragment.publishAPIResponse(status,code,message[0]);


        }
    }
}
