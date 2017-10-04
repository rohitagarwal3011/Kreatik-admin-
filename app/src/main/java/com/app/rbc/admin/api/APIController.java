package com.app.rbc.admin.api;


import android.content.Context;

import android.util.Log;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.activities.ReportActivity;
import com.app.rbc.admin.activities.SettingsActivity;
import com.app.rbc.admin.activities.SiteOverviewActivity;
import com.app.rbc.admin.activities.Splash;
import com.app.rbc.admin.fragments.InitialSyncFragment;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.User;
import com.app.rbc.admin.models.db.models.Vehicle;
import com.app.rbc.admin.models.db.models.Vendor;
import com.app.rbc.admin.models.db.models.site_overview.Order;
import com.app.rbc.admin.models.db.models.site_overview.Requirement;
import com.app.rbc.admin.models.db.models.site_overview.Stock;
import com.app.rbc.admin.models.db.models.site_overview.Trans;
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
    public Retrofit retrofit;
    public APIInterface apiInterface;
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

    public APIInterface getInterface() {
        return this.apiInterface;
    }

    public void addUser(User user) {
        try {
            File file;
            RequestBody filepart;
            MultipartBody.Part myfile;
            if (user.getFile_present() == 0) {
                file = null;
                myfile = null;
            } else {
                file = new File(user.getMyfile());
                filepart = RequestBody.create(MediaType.parse("image/*"), file);
                myfile = MultipartBody.Part.createFormData("myfile",file.getName(), filepart);
            }


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
                    sendAPIResult(0,"Service encountered an error");
                }
            });
        } catch (Exception e) {
            sendAPIResult(0,"Service encountered an error");

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
                    sendAPIResult(0,"Service encountered an error");
                }
            });
        } catch (Exception e) {
            sendAPIResult(0,"Service encountered an error");

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
                        sendAPIResult(0,"Service encountered an error");

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                        sendAPIResult(0,"Service encountered an error");

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                        sendAPIResult(0,"Service encountered an error");

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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

                            JSONArray products_array = vehicleObj.getJSONArray("products");
                            String products = "",quantities = "";
                            for(int j = 0 ; j < products_array.length() ; j++) {
                                JSONObject productObj = products_array.getJSONObject(j);
                                if(j == products_array.length()-1) {
                                    products += productObj.getString("product");
                                    quantities += productObj.getString("quantity");
                                }
                                else {
                                    products += productObj.getString("product")+"|";
                                    quantities += productObj.getString("quantity")+"|";
                                }
                            }
                            vehicle.setProducts(products);
                            vehicle.setQuantities(quantities);
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
            }
        });
    }

    public void receiveVehicle(Vehicle vehicle,JSONArray prod_list,File challan_img,File invoice_img,
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
            RequestBody prod_list_part = RequestBody.create(MediaType.parse("text/plain"), prod_list.toString());


            Call<String> call = apiInterface.receiveVehicle(trans_id,prod_list_part,challan_num,challan_img_part,invoice_img_part,
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
                    sendAPIResult(0,"Service encountered an error");
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                        sendAPIResult(0,"Service encountered an error");

                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
            }
        });
    }



    public void updateVendor(Vendor vendor) {
        Call<String> call = apiInterface.updateVendor(vendor.getName(),
                vendor.getAddress(),
                vendor.getPhone(),
                vendor.getVendorid());

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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
            }
        });
    }

    public void siteOverview(final long site) {
        Call<String> call = apiInterface.siteOverview(site+"");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.errorBody() == null) {
                    try {
                        JSONObject body = new JSONObject(response.body().toString());
                        Log.e("Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");

                        JSONArray stock_details_array = body.getJSONArray("stock_details");

                        List<Stock> stocks = Stock.find(Stock.class,"site = ?",site+"");
                        Stock.deleteInTx(stocks);


                        for(int i = 0 ; i < stock_details_array.length() ; i++) {
                            JSONObject stockObj = stock_details_array.getJSONObject(i);
                            Stock stock = new Stock();
                            stock.setSite(site+"");
                            Log.e("Site",stock.getSite());
                            stock.setWheresite(stockObj.getString("where"));
                            stock.setProduct(stockObj.getString("product"));
                            stock.setQuantity(stockObj.getString("quantity"));
                            stock.setStocktype(stockObj.getString("stock_type"));

                            stock.save();
                        }

                        List<Requirement> requirements = Requirement.find(Requirement.class,"site = ?",site+"");
                        Requirement.deleteInTx(requirements);

                        JSONArray req_details_array = body.getJSONArray("req_details");

                        for(int i = 0 ; i < req_details_array.length() ; i++) {
                            Requirement requirement = new Requirement();

                            JSONObject requirementObj = req_details_array.getJSONObject(i);
                            requirement.setReqid(requirementObj.getString("rq_id"));

                            JSONObject detailsObj = requirementObj.getJSONArray("details").getJSONObject(0);

                            requirement.setTitle(detailsObj.getString("title"));
                            requirement.setCreatedon(detailsObj.getString("created_on"));
                            requirement.setStatus(detailsObj.getString("status"));
                            requirement.setPurpose(detailsObj.getString("purpose"));
                            requirement.setRaisedby(detailsObj.getString("raised_by"));
                            requirement.setFulfilled(detailsObj.getString("fulfilled"));
                            requirement.setSite(detailsObj.getString("site"));
                            requirement.setCategory(detailsObj.getString("category"));
                            requirement.setDesc(detailsObj.getString("desc"));

                            JSONArray products_array = requirementObj.getJSONArray("products");

                            String products = "",quantities = "",rem_quantity="";
                            for(int j = 0 ; j < products_array.length() ; j++) {
                                JSONObject productObj = products_array.getJSONObject(j);
                                if(j == products_array.length() -1) {
                                    products = products + productObj.getString("product");
                                    quantities = quantities + productObj.getString("quantity");
                                    rem_quantity = rem_quantity + productObj.getString("rem_quantity");
                                }
                                else {
                                    products = products + productObj.getString("product") + "|";
                                    quantities = quantities + productObj.getString("quantity") + "|";
                                    rem_quantity = rem_quantity + productObj.getString("rem_quantity") + "|";
                                }

                            }

                            requirement.setProducts(products);
                            requirement.setQuantities(quantities);
                            requirement.setRemquantities(rem_quantity);
                            requirement.save();
                        }

                        List<Trans> transactions = Trans.find(Trans.class,"source = ? or destination = ?",site+"",site+"");
                        Trans.deleteInTx(transactions);


                        JSONArray trans_from_site_array = body.getJSONArray("trans_fromsite");

                        for(int i = 0 ; i < trans_from_site_array.length() ; i++) {
                            Trans transaction = new Trans();

                            JSONObject transactionObj = trans_from_site_array.getJSONObject(i);

                            transaction.setTransid(transactionObj.getString("trans_id"));

                            JSONObject detailsObj = transactionObj.getJSONArray("details").getJSONObject(0);

                            transaction.setStatus(detailsObj.getString("status"));
                            transaction.setVehiclenumber(detailsObj.getString("vehicle_number"));
                            transaction.setDriver(detailsObj.getString("driver"));
                            transaction.setSource(detailsObj.getString("source"));
                            transaction.setDispatchdt(detailsObj.getString("dispatch_dt"));
                            transaction.setSourcetype(detailsObj.getString("source_type"));
                            transaction.setDestination(detailsObj.getString("destination"));
                            transaction.setDesttype(detailsObj.getString("dest_type"));
                            transaction.setChallannum(detailsObj.getString("challan_num"));
                            transaction.setChallanimg(detailsObj.getString("challan_img"));

                            JSONArray products_array = transactionObj.getJSONArray("products");
                            String products = "",quantities = "";
                            for(int j = 0 ; j < products_array.length() ; j++) {
                                JSONObject productObj = products_array.getJSONObject(j);
                                if(j == products_array.length() -1) {
                                    products = products + productObj.getString("product");
                                    quantities = quantities + productObj.getString("quantity");
                                }
                                else {
                                    products = products + productObj.getString("product") + "|";
                                    quantities = quantities + productObj.getString("quantity") + "|";
                                }

                            }
                            Log.e("Products",products);
                            Log.e("Quantities",quantities);
                            transaction.setProducts(products);
                            transaction.setQuantites(quantities);
                            transaction.save();

                        }

                        JSONArray trans_to_site_array = body.getJSONArray("trans_tosite");

                        for(int i = 0 ; i < trans_to_site_array.length() ; i++) {
                            Trans transaction = new Trans();

                            JSONObject transactionObj = trans_to_site_array.getJSONObject(i);

                            transaction.setTransid(transactionObj.getString("trans_id"));

                            JSONObject detailsObj = transactionObj.getJSONArray("details").getJSONObject(0);

                            transaction.setStatus(detailsObj.getString("status"));
                            transaction.setVehiclenumber(detailsObj.getString("vehicle_number"));
                            transaction.setDriver(detailsObj.getString("driver"));
                            transaction.setSource(detailsObj.getString("source"));
                            transaction.setDispatchdt(detailsObj.getString("dispatch_dt"));
                            transaction.setSourcetype(detailsObj.getString("source_type"));
                            transaction.setDestination(detailsObj.getString("destination"));
                            transaction.setDesttype(detailsObj.getString("dest_type"));
                            transaction.setChallannum(detailsObj.getString("challan_num"));
                            transaction.setChallanimg(detailsObj.getString("challan_img"));

                            JSONArray products_array = transactionObj.getJSONArray("products");
                            String products = "",quantities = "";
                            for (int j = 0; j < products_array.length(); j++) {
                                JSONObject productObj = products_array.getJSONObject(j);
                                if(j == products_array.length() -1) {
                                    products = products + productObj.getString("product");
                                    quantities = quantities + productObj.getString("quantity");
                                }
                                else {
                                    products = products + productObj.getString("product") + "|";
                                    quantities = quantities + productObj.getString("quantity") + "|";
                                }

                            }
                            Log.e("Products",products);
                            Log.e("Quantities",quantities);
                            transaction.setProducts(products);
                            transaction.setQuantites(quantities);
                            transaction.save();
                        }

                        sendAPIResult(status,message);
                    }catch (Exception e) {
                        Log.e("Error Site Overview",e.toString());
                    }
                }
                else {
                    Log.e("Error",response.errorBody().toString());
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                    sendAPIResult(0,"Service encountered an error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.toString());
                sendAPIResult(0,"Service encountered an error");
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
                            List<Employee> deleteEmps = Employee.find(Employee.class,"statestore != ?",1+"");
                            Employee.deleteInTx(deleteEmps);
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
                        AppUtil.logger("Fetch Site Response",body.toString());
                        int status = body.getJSONObject("meta").getInt("status");
                        String message = body.getJSONObject("meta").getString("message");
                        if(status != 2) {
                            sendAPIResult(status, message);
                        }

                        else {
                            JSONArray data = body.getJSONArray("site_list");
                            Type listType = new TypeToken<List<Site>>(){}.getType();
                            List<Site> sites = gson.fromJson(data.toString(), listType);
                            List<Site> deleteSites = Site.find(Site.class,"statestore != ?",1+"");
                            Site.deleteInTx(deleteSites);
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
                            List<Vendor> deleteVendors = Vendor.find(Vendor.class,"statestore != ?",1+"");

                            Vendor.deleteInTx(deleteVendors);
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
            case 1 : final InitialSyncFragment frag = (InitialSyncFragment)((Splash)context).getSupportFragmentManager().findFragmentByTag("InitialSync");
                    frag.publichApiResponse(status,code,message[0]);
                break;
            case 2 : ((SiteOverviewActivity) context).publishAPIResponse(status, code, message[0]);
                break;
            case 5: ((SettingsActivity) context).publishAPIResponse(status, code, message[0]);
                break;
            case 8 : ((ReportActivity) context).publishAPIResponse(status, code, message[0]);
                break;

        }
    }

}
