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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.db.models.User;
import com.app.rbc.admin.utils.AppUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.itextpdf.text.BadElementException;

import java.io.IOException;
import java.util.List;
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
    private String profilePath = "";
    private String[] emp_roles;
    private SweetAlertDialog sweetAlertDialog;
    private static boolean edit = false;
    private static long editId;
    private com.app.rbc.admin.models.db.models.Employee editEmployee;

    private com.app.rbc.admin.models.db.models.Employee state_store;
    private boolean save_state_store = true;


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
            editEmployee = com.app.rbc.admin.models.db.models.Employee
                    .findById(com.app.rbc.admin.models.db.models.Employee.class,editId);

            if(editEmployee != null) {
                fullname.setText(editEmployee.getUserName());
                password.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(editEmployee.getMobile().equals("0")) {
                    password.setText("Not Registered");
                }
                else {
                    password.setText(editEmployee.getMobile());

                }
                email.setText(editEmployee.getEmail());

                email.setBackground(getResources().getDrawable(R.drawable.round_edittext_unselectable));
                password.setBackground(getResources().getDrawable(R.drawable.round_edittext_unselectable));

                email.setFocusable(false);
                password.setFocusable(false);
                emp_image.setOnClickListener(null);


                for(int i = 0 ; i < emp_roles.length ; i++) {
                    if(emp_roles[i].equalsIgnoreCase(editEmployee.getRole())) {
                        emp_role_spinner.setSelection(i);
                        break;
                    }
                }
                Uri imageUri = Uri.parse(editEmployee.getPicUrl());
                emp_image.setImageURI(imageUri);

                TextView title_or = (TextView) view.findViewById(R.id.title_or);
                title_or.setText("");
                ViewGroup.LayoutParams params = save_add_another.getLayoutParams();
                params.height = 0;
                save_add_another.setLayoutParams(params);

            }
        }

        else {
            List<com.app.rbc.admin.models.db.models.Employee> state_stores = com.app.rbc.admin.models.db.models.Employee.find(
                    com.app.rbc.admin.models.db.models.Employee.class,"statestore = ?",1+"");
            if(state_stores.size() != 0) {
                state_store = state_stores.get(0);
                fullname.setText(state_store.getUserName());
                email.setText(state_store.getEmail());

                for(int i = 0 ; i < emp_roles.length ; i++) {
                    if(emp_roles[i].equalsIgnoreCase(state_store.getRole())) {
                        emp_role_spinner.setSelection(i);
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onPause() {
        if(save_state_store) {
            if (edit != true) {
                com.app.rbc.admin.models.db.models.Employee state_store;
                if (this.state_store == null) {
                    state_store = new com.app.rbc.admin.models.db.models.Employee();
                } else {
                    state_store = this.state_store;
                }
                state_store.setEmail(email.getText().toString());
                state_store.setUserName(fullname.getText().toString());
                state_store.setRole(emp_roles[emp_role_spinner.getSelectedItemPosition()]);
                state_store.setStatestore(1);
                state_store.save();
            }
        }

        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.form_employee_image:
                ((IndentRegisterActivity)getActivity()).checkPermission(READ_EXTERNAL_CARD);
                break;
            case R.id.save:
                if(edit != true) {
                    validateUserAddForm(60);
                }
                else {
                    validateUserAddForm(62);
                }
                break;
            case R.id.save_add_another:
                validateUserAddForm(61);
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

    private void validateUserAddForm(int code) {
        int validate = 1;
        if(fullname.getText().equals("") || fullname.getText().toString().split(" ").length < 2) {
            fullname.setError("Please enter you full name");
            fullname.requestFocus();
        }
        if(edit != true) {
            if (password.getText().equals("") || password.getText().toString().length() < 8 ||
                    password.getText().toString().length() > 18) {
                validate = 0;
                password.setError(error.getText() + "\nPassword must be 8-18 characters");
                password.requestFocus();
            }

            if (email.getText().equals("") || !emailValidator(email.getText().toString())) {
                validate = 0;
                email.setError(error.getText() + "\nInvalid Email Format");
                email.requestFocus();
            }
        }
        if(validate == 1) {
            if(code == 60 || code == 61) {
                callUserAddAPI(code);
            }
            else if(code == 62) {
                callUserUpdateAPI(code);
            }
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

        APIController controller = new APIController(getContext(),code,IndentRegisterActivity.ACTIVITY);
        controller.addUser(user);
    }

    private void callUserUpdateAPI(int code) {
        editEmployee.setRole(emp_roles[emp_role_spinner.getSelectedItemPosition()]);
        editEmployee.setUserName(fullname.getText().toString());


        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIController controller = new APIController(getContext(),code,IndentRegisterActivity.ACTIVITY);
        controller.updateUser(editEmployee);
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
                APIController controller = new APIController(getContext(),20,IndentRegisterActivity.ACTIVITY);
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

    public void deleteStateStores() {
        List<com.app.rbc.admin.models.db.models.Employee> state_stores =
                com.app.rbc.admin.models.db.models.Employee.find(com.app.rbc.admin.models.db.models.Employee.class,
                        "statestore = ?",1+"");
        com.app.rbc.admin.models.db.models.Employee.deleteInTx(state_stores);
    }

    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        switch(status) {
            case 2 : if(code == 61) {
                deleteStateStores();
                if(state_store != null) {

                    this.state_store = null;
                }
                refreshUI();
            }
            else if(code == 60){
                deleteStateStores();
                this.save_state_store = false;
                if(state_store != null) {

                    this.state_store = null;
                }
                callEmployeeFetchApi();
                ((IndentRegisterActivity)getActivity()).popBackStack();
            }
            else if(code == 62) {
                callEmployeeFetchApi();
                editEmployee.save();
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
