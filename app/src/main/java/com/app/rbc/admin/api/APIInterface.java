package com.app.rbc.admin.api;



import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by jeet on 5/9/17.
 */

public interface APIInterface {

    @Multipart
    @POST("add_user/")
    Call<String> addUser(@Part("name") RequestBody name,
                         @Part("email") RequestBody email,
                         @Part("pwd") RequestBody pwd,
                         @Part("role") RequestBody role,
                         @Part("file_present") RequestBody file_present,
                         @Part MultipartBody.Part myfile,
                         @Part("admin_user_id") RequestBody admin_user_id);
    @FormUrlEncoded
    @POST("add_all_sites/")
    Call<String> addSite(@Field("name") String name,
                         @Field("type") String type);

    @FormUrlEncoded
    @POST("add_vendor/")
    Call<String> addVendor(@Field("name") String name,
                           @Field("address") String address,
                           @Field("phone") String phone);

    @FormUrlEncoded
    @POST("add_category/")
    Call<String> addCategoryProduct(@Field("category") String category,
                                    @Field("product") String product);

    @FormUrlEncoded
    @POST("fetch_emp/")
    Call<String> fetchEmp(@Field("user_id") String user_id);


    @GET("all_site_list/")
    Call<String> fetchSites();

    @GET("total_vendor_list//")
    Call<String> fetchVendors();

    @GET("stock_category//")
    Call<String> fetchCategoriesProducts();
}
