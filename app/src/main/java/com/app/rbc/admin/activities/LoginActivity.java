package com.app.rbc.admin.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.interfaces.ApiServices;
import com.app.rbc.admin.models.Otp;
import com.app.rbc.admin.models.login;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.RetrofitClient;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    Context context;
    public static String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.userid)
    EditText userid;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_in)
    Button signIn;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = this.getApplicationContext();

        AppUtil.logger(TAG, "token" + FirebaseInstanceId.getInstance().getToken());
    }

    @OnClick(R.id.forgot_password)
    public void setForgotPassword(View view) {
        otp_verification("null", "forgot_password");
    }


    @OnClick(R.id.sign_in)
    public void submit(View view) {
        if (verify()) {
            //once verified , pass the credentials to the server
            //AppUtil.showToast(context,"Procced to SignIn");
            progressBar.setVisibility(View.VISIBLE);
            final String username = userid.getText().toString();
            String pwd = password.getText().toString();

            final ApiServices apiServices = RetrofitClient.getApiService();
            AppUtil.logger(TAG, "GCM id : " + FirebaseInstanceId.getInstance().getToken());
            Call<login> call = apiServices.vendorLogin(username, pwd, FirebaseInstanceId.getInstance().getToken());
            AppUtil.logger("Login Activity ", "Login Request : " + call.request().toString());
            call.enqueue(new Callback<login>() {
                @Override
                public void onResponse(Call<login> call, Response<login> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    //Check if the user exists or not
                    //Check if user will login or verify his phone

                    int status = response.body().getMeta().getStatus();

                    AppUtil.logger("Status : ", status + ""+"Message :" + response.body().getMeta().getMessage());


                    switch (status) {
                        case 0:
                            AppUtil.logger(TAG, "User Doesn't Exist");
                            AppUtil.showToast(context, "Invalid UserId. Please check and try again");

                            break;
                        case 1:
                            AppUtil.logger(TAG, "OTP registration needed");
                            AppUtil.showToast(context, "Link mobile to your account");

                            otp_verification(username, "registration");
                            break;

                        case 2:
                            AppUtil.logger(TAG, "Login");

                            AppUtil.logger("Response : ", response.body().getData().getUsername());

                            save_details(response.body().getData().getUsername(), response.body().getData().getMobile(), response.body().getData().getEmail(), response.body().getData().getRole(), response.body().getData().getUser_id() , response.body().getData().getmProfile_image());

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            AppUtil.logger(TAG, "Invalid Password");
                            AppUtil.showToast(context, "UserId and Password doesn't match");

                            break;
                    }


                }

                @Override
                public void onFailure(Call<login> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
                }
            });

        } else {
            AppUtil.showToast(context, "Please Enter the credentials");
        }
    }


    //check if any field is empty or not
    //if not then proceed to login
    public Boolean verify() {
        Boolean proceed = false;
        if (userid.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            proceed = false;
        } else {
            proceed = true;
        }

        return proceed;
    }

    //Save user details
    public void save_details(String name, Long mobile, String email, String role, String user_id ,String profile_image) {
        AppUtil.putString(context, TagsPreferences.NAME, name);
        AppUtil.putLong(context, TagsPreferences.MOBILE, mobile);
        AppUtil.putString(context, TagsPreferences.EMAIL, email);
        AppUtil.putString(context, TagsPreferences.ROLE, role);
        AppUtil.putString(context, TagsPreferences.USER_ID, user_id);
        AppUtil.putString(context,TagsPreferences.PROFILE_IMAGE,profile_image);


    }

    public void otp_verification(final String user_id, final String flow) {

        final ApiServices apiServices = RetrofitClient.getApiService();
        final Dialog dialog = new Dialog(LoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_mobile);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        final TextView enter_phone = (TextView) dialog.findViewById(R.id.enter_phone_text);
        final EditText phone = (EditText) dialog.findViewById(R.id.phone);
        final Button send_otp = (Button) dialog.findViewById(R.id.send_otp);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);

                Call<Otp> call1 = apiServices.send_otp(user_id, phone.getText().toString());
                final String phone_num = phone.getText().toString();
                AppUtil.logger("Login Activity ", "Send Otp : " + call1.request().toString());
                call1.enqueue(new Callback<Otp>() {
                    @Override
                    public void onResponse(Call<Otp> call1, Response<Otp> response) {

                        progressBar.setVisibility(View.GONE);
                        if (response.body().getMeta().getStatus() == 2) {

                            AppUtil.logger(TAG, "OTP Sent ");
                            final Dialog dialog = new Dialog(LoginActivity.this);

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_mobile);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            dialog.show();

                            final TextView enter_phone = (TextView) dialog.findViewById(R.id.enter_phone_text);
                            final EditText phone = (EditText) dialog.findViewById(R.id.phone);
                            final Button send_otp = (Button) dialog.findViewById(R.id.send_otp);
                            enter_phone.setText("Enter OTP");
                            send_otp.setText("Verify OTP");

                            send_otp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    progressBar.setVisibility(View.VISIBLE);
                                    Call<login> call2 = apiServices.verify_otp(user_id, phone.getText().toString(), phone_num);
                                    AppUtil.logger("Login Activity ", "Send Otp : " + call2.request().toString());
                                    call2.enqueue(new Callback<login>() {
                                        @Override
                                        public void onResponse(Call<login> call2, Response<login> response) {

                                            progressBar.setVisibility(View.GONE);
                                            dialog.dismiss();

                                            if (response.body().getMeta().getStatus() == 2)
                                                save_details(response.body().getData().getUsername(), response.body().getData().getMobile(), response.body().getData().getEmail(), response.body().getData().getRole(), response.body().getData().getUser_id(),response.body().getData().getmProfile_image());

                                            if (flow.equalsIgnoreCase("registration")) {
                                                AppUtil.logger(TAG, "Logged in ");

                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            } else {

                                                update_password(response.body().getData().getUser_id());
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<login> call2, Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                            AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
                                        }
                                    });
                                }
                            });


                        } else if (response.body().getMeta().getStatus() == 0) {
                            AppUtil.showToast(context, "Mobile is not linked to any account");
                            progressBar.setVisibility(View.GONE);
                        } else {
                            AppUtil.showToast(context, "Network issue. Please try after sometime");
                            dialog.dismiss();
                        }


                    }

                    @Override
                    public void onFailure(Call<Otp> call1, Throwable t) {
                        dialog.dismiss();
                        AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
                    }
                });


            }
        });

    }


    public void update_password(final String user_id) {

        AppUtil.logger(TAG, "Update Password");
        final Dialog dialog = new Dialog(LoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_new_password);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        final EditText new_password = (EditText) dialog.findViewById(R.id.new_password);
        final EditText confirm_password = (EditText) dialog.findViewById(R.id.confirm_password);
        Button update_password = (Button) dialog.findViewById(R.id.update_password);

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new_password.getText().toString().equals(confirm_password.getText().toString())) {
                    progressBar.setVisibility(View.VISIBLE);
                    final ApiServices apiServices = RetrofitClient.getApiService();
                    AppUtil.logger(TAG, "User id : " + user_id + " Pwd : " + new_password.getText().toString());
                    Call<ResponseBody> call = apiServices.forget_pwd(user_id, new_password.getText().toString());
                    AppUtil.logger("Login Activity ", "Update Password : " + call.request().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            dialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            AppUtil.showToast(context,"Password changed successfully");
//                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(intent);

                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call1, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            AppUtil.showToast(context, "Network Issue. Please check your connectivity and try again");
                        }
                    });

                }
                else {
                    AppUtil.showToast(context,"Passwords doesn't match.");
                }
            }
        });


    }

}
