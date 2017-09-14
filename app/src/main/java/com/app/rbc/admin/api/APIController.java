package com.app.rbc.admin.api;


import android.content.Context;

import android.util.Log;


import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.IndentRegisterActivity;
import com.app.rbc.admin.models.db.models.Categoryproduct;
import com.app.rbc.admin.models.db.models.Employee;
import com.app.rbc.admin.models.db.models.Site;
import com.app.rbc.admin.models.db.models.User;
import com.app.rbc.admin.models.db.models.Vendor;
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

    public APIController(Context context,int code) {
        this.context = context;
        this.code = code;

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

    public void addSite(Site site) {
        Call<String> call = apiInterface.addSite(site.getName(),
                site.getType());

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

    public void addCategoryProduct(Categoryproduct categoryproduct) {
        Call<String> call = apiInterface.addCategoryProduct(categoryproduct.getCategory(),
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
                                JSONArray products = categoryObj.getJSONArray("products");

                                for(int j = 0 ; j < products.length() ; j++) {
                                    JSONObject productObj = products.getJSONObject(j);
                                    String product = productObj.getString("product");

                                    Categoryproduct categoryproduct = new Categoryproduct();
                                    categoryproduct.setProduct(product);
                                    categoryproduct.setCategory(category);
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
        ((IndentRegisterActivity)context).publishAPIResponse(status,code,message[0]);
    }


}
