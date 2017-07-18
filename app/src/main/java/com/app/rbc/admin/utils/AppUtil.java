package com.app.rbc.admin.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.Employee;
import com.facebook.drawee.generic.RoundingParams;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * Created by Rohit on 6/4/17.
 */

public class AppUtil {
    public static void showToast(Context context, String tostMessage) {
        Toast.makeText(context, tostMessage, Toast.LENGTH_SHORT).show();
    }

    public static void logger(String tag,String text) {
        boolean check=true;
        if (check)
        {
            Log.d(tag,text);
//            Log.e(tag,text);
//            Log.w(tag,text);
//            Log.i(tag,text);
//            Log.v(tag,text);

        }
    }

    public static boolean isConected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo[] networkInfos =  connectivityManager.getAllNetworkInfo();
            if(networkInfos!=null){
                for (int i=0;i<networkInfos.length;i++){
                    if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getEncodeParams(String params) {
        try {
            byte[] dataset = params.getBytes("UTF-8");
            String base64 = Base64.encodeToString(dataset, Base64.DEFAULT);
            return base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void progressDialog(Context context,boolean check)
    {


        if(check) {
            ProgressDialog progress = new ProgressDialog(context);
            progress.setMessage("Data fetching...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            AppUtil.logger("test dialod","true found");
        }else {
            ProgressDialog progress = new ProgressDialog(context);
            progress.setMessage("Data fetching...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.dismiss();
            AppUtil.logger("test dialod","false found");
        }
    }



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean validPhone(String phone) {
        Pattern pattern = Patterns.PHONE;
        if(pattern.matcher(phone).matches()){
            // if(phone.length()>=6&&phone.length()<=10){
            if(phone.length()==10)
            {
                return true;
            }else{

                return false;
            }
        }
        return false;
    }


    public static boolean passwordLength(String phone) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(phone).matches()) {
            if (phone.length()==8) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }


    /*shared data */


    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getString(Context context,String key )
    {  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        String value=sharedPreferences.getString(key, "");
        return value.toString();
    }

    public static void putBoolean(Context context,String key,boolean value)
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static boolean getBoolean(Context context,String key )
    {  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        boolean value=sharedPreferences.getBoolean(key, false);
        return value;
    }


    public static void putInt(Context context,String key,int value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static int getInt(Context context,String key )
    {  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        int value=sharedPreferences.getInt(key, 0);
        return value;
    }


    public static void putLong(Context context,String key,long value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    public static Long getLong(Context context,String key )
    {  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        Long value=sharedPreferences.getLong(key, 0);
        return value;
    }

    public static String[] get_employee_from_user_id(Context context, String user_id) {
        Employee employee = new Gson().fromJson(AppUtil.getString(context.getApplicationContext(), TagsPreferences.EMPLOYEE_LIST), Employee.class);
        for (int i = 0; i < employee.getData().size(); i++) {
            if (employee.getData().get(i).getUserId().equalsIgnoreCase(user_id)) {

                String[] user = {employee.getData().get(i).getUserName(),employee.getData().get(i).getMpic_url(),employee.getData().get(i).getRole()};

                return user;

            }
        }
        return null;
    }
}

