package com.app.rbc.admin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.ReportActivity;
import com.app.rbc.admin.api.APIController;
import com.app.rbc.admin.models.db.models.Vehicle;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.Compress;
import com.app.rbc.admin.utils.FileUtils;
import com.itextpdf.text.BadElementException;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.pedant.SweetAlert.SweetAlertDialog;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RecievedVehicle extends Fragment implements View.OnClickListener {

    private View view;


    private Spinner vehicle_number_spinner;
    private EditText challan_number;
    private TextView error,form_title;

    private ImageView challan_image_tick,invoice_image_tick,
            vehicle_goods_tick,vehicle_unloading_tick;
    private Button challan_image_upload,invoice_image_upload,
            vehicle_goods_upload,vehicle_unloading_upload,save;
    private CardView upload_cardview;

    public static final int RESULT_LOAD_IMG = 1,CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;;

    private File photoFile;
    private int file_upload_code;
    private SweetAlertDialog sweetAlertDialog;

    private File challan_image,invoice_image,
    vehicle_goods,vehicle_unloading;

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<String> vehicleNumbers = new ArrayList<>();
    private List<String> challanNumbers = new ArrayList<>();

    public static final int FETCH_VEHICLE_API = 11,ONRECEIVE_VEHICLE_API = 12;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recieved_vehicle, container, false);
        callfetchVehicleAPI();
        return view;
    }

    private void callfetchVehicleAPI() {
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Processing");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        APIController controller = new APIController(getActivity(),FETCH_VEHICLE_API,
                ReportActivity.ACTIVITY);
        controller.fetchVehicleList();
    }


    private void initializeUI() {

        vehicles = Vehicle.listAll(Vehicle.class);
        for(int i = 0 ; i < vehicles.size() ; i++) {
            vehicleNumbers.add(vehicles.get(i).getVehiclenumber());
            challanNumbers.add(vehicles.get(i).getChallannum());
        }

        vehicle_number_spinner = (Spinner) view.findViewById(R.id.vehicle_number);
        ArrayAdapter<String> vehicle_number_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                vehicleNumbers);
        vehicle_number_spinner.setAdapter(vehicle_number_adapter);


        challan_number = (EditText) view.findViewById(R.id.challan_number);
        error = (TextView) view.findViewById(R.id.error);
        form_title = (TextView) view.findViewById(R.id.form_title);

        challan_image_tick = (ImageView) view.findViewById(R.id.challan_image_tick);
        invoice_image_tick = (ImageView) view.findViewById(R.id.invoice_image_tick);
        vehicle_goods_tick = (ImageView) view.findViewById(R.id.vehicle_goods_tick);
        vehicle_unloading_tick = (ImageView) view.findViewById(R.id.vehicle_unloading_tick);

        upload_cardview = (CardView) view.findViewById(R.id.upload_cardview);




        challan_image_upload = (Button) view.findViewById(R.id.challan_image_upload);
        invoice_image_upload = (Button) view.findViewById(R.id.invoice_image_upload);
        vehicle_goods_upload = (Button) view.findViewById(R.id.vehicle_goods_upload);
        vehicle_unloading_upload = (Button) view.findViewById(R.id.vehicle_unloading_upload);
        save = (Button) view.findViewById(R.id.save);

        // Listeners
        challan_image_upload.setOnClickListener(this);
        invoice_image_upload.setOnClickListener(this);
        vehicle_goods_upload.setOnClickListener(this);
        vehicle_unloading_upload.setOnClickListener(this);
        save.setOnClickListener(this);


        challan_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == challanNumbers.get(vehicle_number_spinner.getSelectedItemPosition()).length()) {
                    if(s.toString().equalsIgnoreCase(challanNumbers.get(vehicle_number_spinner.getSelectedItemPosition()))) {

                        challan_number.setEnabled(false);
                        challan_number.setClickable(false);
                        challan_number.setBackground(getActivity().getResources().getDrawable(R.drawable.round_edittext_unselectable));

                        form_title.setText("Challan Verified");
                        form_title.setTextColor(getResources().getColor(R.color.verified));

                        ViewGroup.LayoutParams params = upload_cardview.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        upload_cardview.setLayoutParams(params);

                    }
                    else {
                        challan_number.setError("Verification Failed");
                        challan_number.requestFocus();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.challan_image_upload:
                file_upload_code = 1;
                show_attachment_dialog();
                break;
            case R.id.invoice_image_upload:
                file_upload_code = 2;
                show_attachment_dialog();
                break;
            case R.id.vehicle_goods_upload:
                file_upload_code = 3;
                show_attachment_dialog();
                break;
            case R.id.vehicle_unloading_upload:
                file_upload_code = 4;
                show_attachment_dialog();
                break;
            case R.id.save :
                validateUploads();
        }
    }



    public void show_attachment_dialog()
    {
        final Dialog dialog = new Dialog(getContext());

        dialog.setTitle("Choose option");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_camera_options);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView gallery,camera,pdf;

        gallery = (ImageView) dialog.findViewById(R.id.gallery);
        camera = (ImageView) dialog.findViewById(R.id.camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportActivity)getActivity()).askPermission(RESULT_LOAD_IMG);

                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportActivity)getActivity()).askPermission(CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void loadImagefromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        try {
            startActivityForResult(
                    Intent.createChooser(galleryIntent,"Complete action using"),
                    RESULT_LOAD_IMG);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "No application found for action",
                    Toast.LENGTH_SHORT).show();
        }
    }



    public void captureImage() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            photoFile = null;
            try {
                photoFile = FileUtils.createImageFile();
            } catch (IOException ex) {
                Log.e("Vehicle Recieved",ex.toString());

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMG
                && null != data) {

            Uri selectedImage = data.getData();
            try {


                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);

                AppUtil.logger("App Activity","imgDecodableString : "+imgDecodableString);
                cursor.close();
                compress_and_send_image(imgDecodableString);


            } catch (Exception e) {
                Log.e("Parse Image",e.toString());
            }

        }

        // when image is clicked
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                AppUtil.logger("Task_create", "Image Path : " + photoFile.getPath());
                try {
                    compress_and_send_image(photoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                //  compress_create_pdf_and_show_card(fileUri.getPath());
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }



        else {
            AppUtil.showToast(getContext(),"You haven't selected any image");

        }







    }

    private void compress_and_send_image(String image_path) throws IOException, BadElementException {
        Compress compress = new Compress();
        String image_returned = compress.compressImage(image_path);
        AppUtil.logger("Returned path : ", image_returned);
        Uri fileUri = Uri.parse(image_returned);
        AppUtil.logger("Uri of image ",fileUri.toString());
        File file= new File(image_returned);
        switch (file_upload_code) {
            case 1: challan_image = file;
                challan_image_tick.setBackground(getResources().getDrawable(R.drawable.tick_circle_icon));
                break;
            case 2: invoice_image = file;
                invoice_image_tick.setBackground(getResources().getDrawable(R.drawable.tick_circle_icon));
                break;
            case  3: vehicle_goods = file;
                vehicle_goods_tick.setBackground(getResources().getDrawable(R.drawable.tick_circle_icon));
                break;
            case  4: vehicle_unloading = file;
                vehicle_unloading_tick.setBackground(getResources().getDrawable(R.drawable.tick_circle_icon));
                break;
        }
    }

    private void validateUploads() {
        if(challan_image != null && invoice_image != null  && vehicle_goods != null &&
                vehicle_unloading != null){
            sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            sweetAlertDialog.setTitleText("Processing");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();

            APIController controller = new APIController(getActivity(),
                    ONRECEIVE_VEHICLE_API,ReportActivity.ACTIVITY);
            controller.receiveVehicle(vehicles.get(vehicle_number_spinner.getSelectedItemPosition()),
                    challan_image,invoice_image,vehicle_goods,vehicle_unloading);
        }
        else {
            error.setText("Please upload all the attachments");
        }
    }


    public void publishAPIResponse(int status,int code,String... message) {
        sweetAlertDialog.dismiss();
        initializeUI();
        switch(status) {
            case 2 :
                    if(code == ONRECEIVE_VEHICLE_API) {
                        Toast.makeText(getActivity(),
                                "Verified",
                                Toast.LENGTH_SHORT).show();
                    }
                break;
            case 1 : if(code == FETCH_VEHICLE_API) {
                error.setText(message[0]);
            }
            else if(code == ONRECEIVE_VEHICLE_API) {
             error.setText(message[0]);
            }
                break;
            case 0: if(code == FETCH_VEHICLE_API) {
                error.setText(message[0]);
            }
            else if(code == ONRECEIVE_VEHICLE_API) {
                error.setText(message[0]);
            }
                break;
        }
    }

}
