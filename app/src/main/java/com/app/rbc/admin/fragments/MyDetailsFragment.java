package com.app.rbc.admin.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.User;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyDetailsFragment extends Fragment implements View.OnClickListener{
    private View view;
    private EditText me_mobile,me_fullname;
    private TextView me_role,me_email;
    private SimpleDraweeView me_pic;
    private Button save;
    private final int PICK_FROM_GALLERY = 1;
    private String profilePath = null;
    private TextView error;
    private SweetAlertDialog sweetAlertDialog;

    public final int OTP_API = 14;
    public final int VERIFY_OTP = 15;
    public final int UPDATE_ME_API = 12;

    private Employee employee;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_details, container, false);
        initializeUI();
        return view;
    }


    private void initializeUI() {
        me_email = (TextView) view.findViewById(R.id.form_employee_email);
        me_mobile = (EditText) view.findViewById(R.id.form_employee_mobile);
        me_fullname = (EditText) view.findViewById(R.id.form_employee_fullname);
        me_role = (TextView) view.findViewById(R.id.form_employee_role);
        me_pic = (SimpleDraweeView) view.findViewById(R.id.form_employee_pic);
        save = (Button) view.findViewById(R.id.save_me);
        error = (TextView) view.findViewById(R.id.error);

        me_fullname.setText(AppUtil.getString(getContext(), TagsPreferences.NAME));
        me_role.setText(AppUtil.getString(getContext(), TagsPreferences.ROLE));
        me_mobile.setText(AppUtil.getLong(getContext(), TagsPreferences.MOBILE).toString());
        me_email.setText(AppUtil.getString(getContext(), TagsPreferences.EMAIL));
        me_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getActivity()).load(AppUtil.getString(getActivity(), TagsPreferences.PROFILE_IMAGE)).into(me_pic);


        //Listeners

        me_pic.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_me:
                validateUser();
                break;
            case R.id.form_employee_pic:
                startImageGalleryIntent();
                break;
        }
    }

    public void startImageGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        try {
            startActivityForResult(
                    Intent.createChooser(galleryIntent,"Complete action using"),
                    PICK_FROM_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "No application found for action",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void setEmpImage(Uri contentUri) {
        try {
            me_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            me_pic.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                    contentUri));
        }
        catch (Exception e) {
            Log.e("Add Employee Bitmap",e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {


                    Uri selectedImage = data.getData();
                    try {
                        setEmpImage(selectedImage);


                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        // Get the cursor
                        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String imgDecodableString = cursor.getString(columnIndex);
                        profilePath = imgDecodableString;
                        AppUtil.logger("App Activity","imgDecodableString : "+imgDecodableString);
                        cursor.close();


                    } catch (Exception e) {
                        Log.e("Parse Image",e.toString());
                    }




                }
                break;
        }
    }

    private void validateUser() {
        int validate = 1;

        if(me_mobile.getText().toString().length() != 10 || me_mobile.getText().toString().equals("")) {
            validate = 0;
            me_mobile.setError("Enter a valid mobile number");
            me_mobile.requestFocus();
        }
        if(me_fullname.getText().toString().length() < 5 || me_fullname.getText().toString().equals("")) {
            validate  = 0;
            me_fullname.setError("Enter a valid username");
            me_fullname.requestFocus();
        }

        if(validate == 1) {
            checkMobileChanged();
        }
    }

    private void checkMobileChanged() {
        if(!(me_mobile.getText().toString().equalsIgnoreCase(
                AppUtil.getLong(getContext(), TagsPreferences.MOBILE).toString()
        ))) {
            callOtpAPI(OTP_API);
        }
        else {
            callUpdateMe(UPDATE_ME_API);
        }
    }

    private void callOtpAPI(int code) {
        employee = new Employee();
        employee.setUserid(AppUtil.getString(getActivity(), TagsPreferences.USER_ID));
        employee.setMobile(me_mobile.getText().toString());

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code,IndentRegisterActivity.ACTIVITY);
        controller.sendOTP(employee);
    }


    private void callUpdateMe(int code) {
        User user = new User();
        user.setName(AppUtil.getString(getContext(), TagsPreferences.NAME));
        user.setEmail(AppUtil.getString(getContext(), TagsPreferences.EMAIL));
        user.setRole(AppUtil.getString(getContext(), TagsPreferences.ROLE));
        user.setUser_id(AppUtil.getString(getActivity(), TagsPreferences.USER_ID));


        if(profilePath != null) {
            user.setFile_present(1);
            user.setMyfile(profilePath);
        }


        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code,IndentRegisterActivity.ACTIVITY);
        controller.updateMe(user);
    }
    private void inflateOTPDialog() {
        final Dialog dialog = new Dialog(getActivity());
        View otpDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_mobile,null);
        TextView enter_otp = (TextView) otpDialog.findViewById(R.id.enter_phone_text);
        final EditText otp = (EditText) otpDialog.findViewById(R.id.phone);
        Button send_otp = (Button) otpDialog.findViewById(R.id.send_otp);

        // Setting values
        enter_otp.setText("Enter OTP");
        send_otp.setText("Verify OTP");


        // Listeners
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIController controller = new APIController(getActivity(),VERIFY_OTP,IndentRegisterActivity.ACTIVITY);
                controller.verifyOTP(employee,otp.getText().toString());
                dialog.dismiss();
                sweetAlertDialog.show();
            }
        });



        dialog.setContentView(otpDialog);
        dialog.show();
    }
    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 :
                if(code == 12) {
                    ((IndentRegisterActivity)getActivity()).popBackStack();
                }
                else if(code == 14) {
                    inflateOTPDialog();
                }
                else if(code == 15) {
                    callUpdateMe(UPDATE_ME_API);
                }
                break;
            case 1 : error.setText(message[0]);
                break;
            case 0: error.setText("Request Failed");
                break;
        }
    }
}
