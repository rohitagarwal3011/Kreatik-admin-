package com.app.rbc.admin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.db.models.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddEmployeeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private EditText fullname,password,email;
    private TextView error;
    private SimpleDraweeView emp_image;
    private Spinner emp_role_spinner;
    private final int PICK_FROM_GALLERY = 1;
    public final int READ_EXTERNAL_CARD = 6;
    private final int RESULT_CROP = 2;
    private String profilePath;
    private String[] emp_roles;
    private SweetAlertDialog sweetAlertDialog;
    private static boolean edit = false;
    private static long editId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_employee, container, false);
        if(edit) {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Edit");
        }
        else {
            ((IndentRegisterActivity) getActivity()).getSupportActionBar().setTitle("Add an employee");
        }
        initializeUI();
        return view;
    }

    private void initializeUI() {
        fullname = (EditText) view.findViewById(R.id.form_employee_fullname);
        password = (EditText) view.findViewById(R.id.form_employee_password);
        email = (EditText) view.findViewById(R.id.form_employee_email);
        emp_image = (SimpleDraweeView) view.findViewById(R.id.form_employee_image);
        emp_role_spinner = (Spinner) view.findViewById(R.id.form_employee_role_spinner);

        Button save = (Button) view.findViewById(R.id.save);
        Button save_add_another = (Button) view.findViewById(R.id.save_add_another);
        error = (TextView) view.findViewById(R.id.error);

        // Setting data

        emp_roles = getActivity().getResources().getStringArray(R.array.emp_roles);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                emp_roles);
        emp_role_spinner.setAdapter(spinnerAdapter);

        // Listeners
        emp_image.setOnClickListener(this);
        save.setOnClickListener(this);
        save_add_another.setOnClickListener(this);

        if(edit == true) {
            com.app.rbc.admin.models.db.models.Employee employee = com.app.rbc.admin.models.db.models.Employee
                    .findById(com.app.rbc.admin.models.db.models.Employee.class,editId);

            if(employee != null) {
                fullname.setText(employee.getUserName());
                password.setText(employee.getMobile());
                password.setInputType(InputType.TYPE_CLASS_NUMBER);
                email.setText(employee.getEmail());
                for(int i = 0 ; i < emp_roles.length ; i++) {
                    if(emp_roles[i].equalsIgnoreCase(employee.getRole())) {
                        emp_role_spinner.setSelection(i);
                        break;
                    }
                }
                Uri imageUri = Uri.parse(employee.getPicUrl());
                emp_image.setImageURI(imageUri);

            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.form_employee_image:
                ((IndentRegisterActivity)getActivity()).checkPermission(READ_EXTERNAL_CARD);
                break;
            case R.id.save:
                validateUserAddForm(60);
                break;
            case R.id.save_add_another:
                validateUserAddForm(61);
                break;
        }
    }


    public void startImageGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent,"Complete action using"),
                    PICK_FROM_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "No application found for action",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setEmpImage(Uri contentUri) {
        try {
            emp_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            emp_image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
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
                    try {
                        Uri selectedImageURI = data.getData();
                        profilePath = getRealPathFromUri(getContext(), selectedImageURI);
                        Log.e("Profile path",profilePath);
                        setEmpImage(selectedImageURI);

                    } catch (Exception e) {
                        Log.e("Gallery Pick Error", e.toString());
                    }
                }
                break;
        }
    }

    private void validateUserAddForm(int code) {
        error.setText("");
        int validate = 1;
        if(fullname.getText().equals("") || fullname.getText().toString().split(" ").length < 2) {
            error.setText("Please enter you full name");
            validate = 0;
        }
        if(password.getText().equals("") || password.getText().toString().length() < 8 ||
                password.getText().toString().length() > 18) {
            validate = 0;
            error.setText(error.getText()+"\nPassword must be 8-18 characters");
        }

        if(email.getText().equals("") || !emailValidator(email.getText().toString())) {
            validate = 0;
            error.setText(error.getText()+"\nInvalid Email Format");
        }
        if(validate == 1) {
            callUserAddAPI(code);
        }
    }

    private boolean emailValidator(String email) {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void callUserAddAPI(int code) {
        User user = new User();
        user.setAdmin_user_id(getActivity().getResources().getString(R.string.admin_user_id));
        user.setEmail(email.getText().toString());
        user.setPwd(password.getText().toString());
        user.setRole(emp_roles[emp_role_spinner.getSelectedItemPosition()]);
        Log.e("Role",user.getRole());
        user.setName(fullname.getText().toString());

        if(profilePath.equals("")) {
            user.setFile_present(0);
        }
        else {
            user.setFile_present(1);
            user.setMyfile(profilePath);
        }

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code);
        controller.addUser(user);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void refreshUI() {
        emp_image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.image_placeholder));
        fullname.setText("");
        emp_role_spinner.setSelection(0);
        email.setText("");
        password.setText("");
    }

    private void callEmployeeFetchApi() {
        new Thread() {
            public void run() {
                APIController controller = new APIController(getContext(),20);
                controller.fetchEmp();
            }
        }.start();

    }

    public static AddEmployeeFragment newInstance(Object... data) {
        AddEmployeeFragment addEmployeeFragment = new AddEmployeeFragment();
        if(data.length != 0) {
            addEmployeeFragment.edit = true;
            addEmployeeFragment.editId = (long)data[0];
        }
        else {
            addEmployeeFragment.edit = false;
            addEmployeeFragment.editId = 0;
        }
        return  addEmployeeFragment;
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 : if(code == 61) {
                refreshUI();
            }
            else if(code == 60){
                callEmployeeFetchApi();
                ((IndentRegisterActivity)getActivity()).popBackStack();
            }
            break;
            case 1 : error.setText(message[0]);
                break;
            case 0: error.setText("Request Failed");
                break;
        }
    }
}
