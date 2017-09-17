package com.app.rbc.admin.api;


import android.content.Context;

import android.util.Log;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.activities.ReportActivity;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.User;
import com.app.rbc.admin.models.db.models.Vehicle;
import com.app.rbc.admin.models.db.models.Vendor;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by jeet on 5/9/17.
 */

public class APIController{

    private final String BASE_URL = "http://plethron.pythonanywhere.com/eagle/";
    private Retrofit retrofit;
    private APIInterface apiInterface;
    private Context context;
    private int code;
    private Gson gson;
    private int activity;

    public APIController(Context context,int code,int activity) {
        this.context = context;
        this.code = code;
        this.activity = activity;
        this.gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(APIInterface.class);
    }

    public void addUser(User user) {
        try {
            File file;
            if (user.getFile_present() == 0) {
                file = null;
            } else {
                file = new File(user.getMyfile());
            }
            Log.e("file",file.getPath());
            RequestBody filepart = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part myfile = MultipartBody.Part.createFormData("myfile",file.getName(), filepart);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), user.getName());
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
            RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"), user.getPwd());
            RequestBody file_present = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getFile_present()));
            RequestBody role = RequestBody.create(MediaType.parse("text/plain"), user.getRole());
            RequestBody admin_user_id = RequestBody.create(MediaType.parse("text/plain"), user.getAdmin_user_id());


            Call<String> call = apiInterface.addUser(name, email, pwd, role, file_present, myfile, admin_user_id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    try {
                        if(response.errorBody() != null) {
                            Log.e("Error",response.errorBody().string()+" "+response.code());
                            sendAPIResult(0);
                        }
                        else {
                            JSONObject body = new JSONObject(response.body().toString());
                            int status = body.getJSONObject("meta").getInt("status");
                            String message = body.getJSONObject("meta").getString("message");
                            sendAPIResult(status,message);
                        }
                    } catch (Exception e) {
                        Log.e("Add user res parsing", e.toString());
                        sendAPIResult(0,"Parsing Error Encountered");
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("Error", t.toString());
                    sendAPIResult(0);
                }
            });
        } catch (Exception e) {
            Log.e("Add User Exception",e.toString());
        }
    }

    public void updateMe(User user) {
        MultipartBody.Part myfile;
        try {
            File file;
            if (user.getFile_present() == 0) {
                myfile = null;
            } else {
                file = new File(user.getMyfile());
                Log.e("file",file.getPath());
                RequestBody filepart = RequestBody.create(MediaType.parse("image/*"), file);
                myfile = MultipartBody.Part.createFormData("myfile",file.getName(), filepart);
            }


            Call<String> call = apiInterface.updateUser(user.getName(), user.getEmail(),
                    user.getUser_id(), user.getRole(), user.getFile_present()+"" , myfile);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    try {
                        if(response.errorBody() != null) {
                            Log.e("Error",response.errorBody().string()+" "+response.code());
                            sendAPIResult(0);
                        }
                        else {
                            JSONObject body = new JSONObject(response.body().toString());
                            int status = body.getJSONObject("meta").getInt("status");
                            String message = body.getJSONObject("meta").getString("message");
                            sendAPIResult(status,message);
                        }
                    } catch (Exception e) {
                        Log.e("Add user res parsing", e.toString());
                        sendAPIResult(0,"Parsing Error Encountered");
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("Error", t.toString());
                    sendAPIResult(0);
                }
            });
        } catch (Exception e) {
            Log.e("Add User Exception",e.toString());
        }
    }

    public void addSite(Site site) {
        Call<String> call = apiInterface.addSite(site.getName(),
                site.getType(),
                site.getLocation(),
                site.getIncharge());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }


    public void updateSite(Site site) {
        Call<String> call = apiInterface.updateSite(site.getName(),
                site.getType(),
                site.getLocation(),
                site.getIncharge(),
                site.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void updateProduct(Categoryproduct categoryproduct,String old_prod) {
        Call<String> call = apiInterface.updateProdut(categoryproduct.getCategory(),
                old_prod,
                categoryproduct.getProduct());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void addVendor(Vendor vendor) {
        Call<String> call = apiInterface.addVendor(vendor.getName(),
                vendor.getAddress(),
                vendor.getPhone());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }
    public void sendOTP(Employee employee) {
        Call<String> call = apiInterface.sendOTP(employee.getUserid(),
                employee.getMobile());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("OTP Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void fetchVehicleList() {
        Call<String> call = apiInterface.vehicleList(1);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Vehicle List Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");


                        JSONArray vehiclelistArray = body.getJSONArray("vehiclelist");
                        List<Vehicle> vehicleList = new ArrayList<>();
                        for(int i = 0 ; i < vehiclelistArray.length() ; i++) {
                            Vehicle vehicle = new Vehicle();

                            JSONObject vehicleObj = vehiclelistArray.getJSONObject(i);
                            String trans_id = vehicleObj.getString("trans_id");
                            vehicle.setTransid(trans_id);
                            JSONArray detailsArray = vehicleObj.getJSONArray("details");



                            if(detailsArray.length() > 0) {

                                JSONObject details = detailsArray.getJSONObject(0);
                                vehicle.setStatus(details.getString("status"));
                                vehicle.setVehiclenumber(details.getString("vehicle_number"));
                                vehicle.setDispatchdt(details.getString("dispatch_dt"));
                                vehicle.setSource(details.getString("source"));
                                vehicle.setSourcetype(details.getString("source_type"));
                                vehicle.setDesttype(details.getString("dest_type"));
                                vehicle.setDestination(details.getString("destination"));
                                vehicle.setChallannum(details.getString("challan_num"));
                                vehicle.setChallanimg(details.getString("challan_img"));
                            }
                            vehicleList.add(vehicle);

                        }

                        Vehicle.deleteAll(Vehicle.class);
                        Vehicle.saveInTx(vehicleList);
                        sendAPIResult(status,message);
                    }catch (Exception e) {
                        Log.e("Error vehicle list",e.toString());
                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void receiveVehicle(Vehicle vehicle,File challan_img,File invoice_img,
                               File onreceive_img,File unloaded_img) {
        try {
            RequestBody challan_img_rb = RequestBody.create(MediaType.parse("image/*"), challan_img);
            MultipartBody.Part challan_img_part = MultipartBody.Part.createFormData("challan_img",challan_img.getName(), challan_img_rb);


            RequestBody invoice_img_rb = RequestBody.create(MediaType.parse("image/*"), invoice_img);
            MultipartBody.Part invoice_img_part = MultipartBody.Part.createFormData("invoice_img",invoice_img.getName(), invoice_img_rb);

            RequestBody onreceive_img_rb = RequestBody.create(MediaType.parse("image/*"), onreceive_img);
            MultipartBody.Part onreceive_img_part = MultipartBody.Part.createFormData("onreceive_img",onreceive_img.getName(), onreceive_img_rb);

            RequestBody unloaded_img_rb = RequestBody.create(MediaType.parse("image/*"), unloaded_img);
            MultipartBody.Part unloaded_img_part = MultipartBody.Part.createFormData("unloaded_img",unloaded_img.getName(), unloaded_img_rb);

            RequestBody trans_id = RequestBody.create(MediaType.parse("text/plain"), vehicle.getTransid());
            RequestBody challan_num = RequestBody.create(MediaType.parse("text/plain"), vehicle.getChallannum());


            Call<String> call = apiInterface.receiveVehicle(trans_id, challan_num,challan_img_part,invoice_img_part,
                    onreceive_img_part,unloaded_img_part);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    try {
                        if(response.errorBody() != null) {
                            Log.e("Error",response.errorBody().string()+" "+response.code());
                            sendAPIResult(0);
                        }
                        else {
                            JSONObject body = new JSONObject(response.body().toString());
                            Log.e("Response",body.toString());
                            int status = body.getJSONObject("meta").getInt("status");
                            String message = body.getJSONObject("meta").getString("message");
                            sendAPIResult(status,message);
                        }
                    } catch (Exception e) {
                        Log.e("Onreceive Vehicle", e.toString());
                        sendAPIResult(0,"Parsing Error Encountered");
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("Error", t.toString());
                    sendAPIResult(0);
                }
            });
        } catch (Exception e) {
            Log.e("Onreceive Vehicle",e.toString());
        }
    }


    public void verifyOTP(Employee employee,String otp) {
        Call<String> call = apiInterface.verifyOTP(employee.getUserid(),
                otp,
                employee.getMobile());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("OTP Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        JSONObject data = body.getJSONObject("data");

                        AppUtil.putString(context, TagsPreferences.NAME, data.getString("Username"));
                        AppUtil.putLong(context, TagsPreferences.MOBILE, data.getLong("Mobile"));
                        AppUtil.putString(context, TagsPreferences.EMAIL, data.getString("Email"));
                        AppUtil.putString(context, TagsPreferences.ROLE, data.getString("Role"));
                        AppUtil.putString(context, TagsPreferences.USER_ID, data.getString("User_id"));
                        AppUtil.putString(context,TagsPreferences.PROFILE_IMAGE,data.getString("Profile_image"));

                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void updateUser(Employee employee) {
        Call<String> call = apiInterface.updateUser(employee.getUserName(),
                employee.getEmail(),
                employee.getUserid(),
                employee.getRole(),
                "0",
                null);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }



    public void updateVendor(Vendor vendor) {
        Call<String> call = apiInterface.updateVendor(vendor.getName(),
                vendor.getAddress(),
                vendor.getPhone(),
                vendor.getVendor_id());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void addCategoryProduct(Categoryproduct categoryproduct) {
        Call<String> call = apiInterface.addCategoryProduct(categoryproduct.getCategory(),
                categoryproduct.getProduct(),
                categoryproduct.getUnit());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {
                        Log.e("Error Add Category",e.toString());
                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }


    public void updateCategory(String old_cat,String new_cat,String unit) {
        Call<String> call = apiInterface.updateCategory(old_cat,
                new_cat,
                unit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        sendAPIResult(status,message);
                    }catch (Exception e) {
                        Log.e("Error Update Category",e.toString());
                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0);
            }
        });
    }

    public void fetchEmp() {
        Call<String> call= apiInterface.fetchEmp(context.getResources().getString(R.string.admin_user_id));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        if(status != 2) {
                            sendAPIResult(status, message);
                        }

                        else {
                            JSONArray data = body.getJSONArray("data");
                            Type listType = new TypeToken<List<Employee>>(){}.getType();
                            List<Employee> employees = gson.fromJson(data.toString(), listType);
                            Employee.deleteAll(Employee.class);
                            Employee.saveInTx(employees);
                            Log.e("Employees Count",employees.size()+"");
                            sendAPIResult(status,message);
                        }

                    }catch (Exception e) {
                        Log.e("Error Fetch Emp",e.toString());
                        sendAPIResult(0,"Service encountered an error");
                    }
                }
                else {
                    Log.e("Error Fetch Emp",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendAPIResult(0,"Service encountered error");
            }
        });
    }

    public void fetchSites() {
        Call<String> call= apiInterface.fetchSites();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        if(status != 2) {
                            sendAPIResult(status, message);
                        }

                        else {
                            JSONArray data = body.getJSONArray("site_list");
                            Type listType = new TypeToken<List<Site>>(){}.getType();
                            List<Site> sites = gson.fromJson(data.toString(), listType);
                            Site.deleteAll(Site.class);
                            Site.saveInTx(sites);
                            Log.e("Site Count",sites.size()+"");
                            sendAPIResult(status,message);
                        }

                    }catch (Exception e) {
                        Log.e("Error Fetch Sites",e.toString());
                        sendAPIResult(0,"Service encountered an error");
                    }
                }
                else {
                    Log.e("Error Fetch Sites",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendAPIResult(0,"Service encountered error");
            }
        });
    }

    public void fetchVendors() {
        Call<String> call= apiInterface.fetchVendors();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        if(status != 2) {
                            sendAPIResult(status, message);
                        }

                        else {
                            JSONArray data = body.getJSONArray("vendor_list");
                            Type listType = new TypeToken<List<Vendor>>(){}.getType();
                            List<Vendor> vendors = gson.fromJson(data.toString(), listType);
                            Vendor.deleteAll(Vendor.class);
                            Vendor.saveInTx(vendors);
                            Log.e("Vendor Count",vendors.size()+"");
                            sendAPIResult(status,message);
                        }

                    }catch (Exception e) {
                        Log.e("Error Fetch Vendors",e.toString());
                        sendAPIResult(0,"Service encountered an error");
                    }
                }
                else {
                    Log.e("Error Fetch Vendors",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendAPIResult(0,"Service encountered error");
            }
        });
    }

    public void fetchCategoriesProducts() {
        Call<String> call= apiInterface.fetchCategoriesProducts();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        if(status != 2) {
                            sendAPIResult(status, message);
                        }

                        else {
                            JSONArray data = body.getJSONArray("category_list");
                            List<Categoryproduct> categoryproducts = new ArrayList<>();

                            for(int i = 0 ; i < data.length() ; i++) {
                                JSONObject categoryObj = data.getJSONObject(i);
                                String category = categoryObj.getString("category");
                                String unit = categoryObj.getString("unit");
                                JSONArray products = categoryObj.getJSONArray("products");

                                for(int j = 0 ; j < products.length() ; j++) {
                                    JSONObject productObj = products.getJSONObject(j);
                                    String product = productObj.getString("product");

                                    Categoryproduct categoryproduct = new Categoryproduct();
                                    categoryproduct.setProduct(product);
                                    categoryproduct.setCategory(category);
                                    categoryproduct.setUnit(unit);
                                    categoryproducts.add(categoryproduct);
                                }
                            }

                            Categoryproduct.deleteAll(Categoryproduct.class);
                            Categoryproduct.saveInTx(categoryproducts);


                            Log.e("Category Product Count",categoryproducts.size()+"");
                            sendAPIResult(status,message);
                        }

                    }catch (Exception e) {
                        Log.e("Error Fetch Categories",e.toString());
                        sendAPIResult(0,"Service encountered an error");
                    }
                }
                else {
                    Log.e("Error Fetch Categories",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendAPIResult(0,"Service encountered error");
            }
        });
    }




    private void sendAPIResult(int status,String... message) {
        switch (activity) {
            case 5: ((IndentRegisterActivity) context).publishAPIResponse(status, code, message[0]);
                break;
            case 8 : ((ReportActivity) context).publishAPIResponse(status, code, message[0]);
                break;

        }
    }

}
