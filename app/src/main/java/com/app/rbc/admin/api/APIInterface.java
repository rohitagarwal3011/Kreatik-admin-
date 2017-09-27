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

public interface    APIInterface {

    @Multipart
    @POST("add_user/")
    Call<String> addUser(@Part("name") RequestBody name,
                         @Part("email") RequestBody email,
                         @Part("pwd") RequestBody pwd,
                         @Part("role") RequestBody role,
                         @Part("file_present") RequestBody file_present,
                         @Part MultipartBody.Part myfile,
                         @Part("admin_user_id") RequestBody admin_user_id);

    @Multipart
    @POST("update_user/")
    Call<String> updateUser(@Part("name") String name,
                         @Part("email") String email,
                         @Part("user_id") String user_id,
                         @Part("role") String role,
                         @Part("file_present") String file_present,
                         @Part MultipartBody.Part myfile);

    @FormUrlEncoded
    @POST("add_all_sites/")
    Call<String> addSite(@Field("name") String name,
                         @Field("type") String type,
                         @Field("location") String location,
                         @Field("site_incharge") String site_incharge);

    @FormUrlEncoded
    @POST("send_otp/")
    Call<String> sendOTP(@Field("user_id") String user_id,
                         @Field("phone") String phone);



    @FormUrlEncoded
    @POST("verify_otp/")
    Call<String> verifyOTP(@Field("user_id") String user_id,
                           @Field("otp") String otp,
                         @Field("phone") String phone);



    @FormUrlEncoded
    @POST("update_site/")
    Call<String> updateSite(@Field("name") String name,
                         @Field("type") String type,
                         @Field("location") String location,
                         @Field("site_incharge") String site_incharge,
                            @Field("id") long id);


    @FormUrlEncoded
    @POST("add_vendor/")
    Call<String> addVendor(@Field("name") String name,
                           @Field("address") String address,
                           @Field("phone") String phone);

    @FormUrlEncoded
    @POST("update_vendor/")
    Call<String> updateVendor(@Field("name") String name,
                           @Field("address") String address,
                           @Field("phone") String phone,
                              @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST("add_category/")
    Call<String> addCategoryProduct(@Field("category") String category,
                                    @Field("product") String product,
                                    @Field("unit") String unit);

    @FormUrlEncoded
    @POST("update_category/")
    Call<String> updateCategory(@Field("old_cat") String old_cat,
                                    @Field("new_cat") String new_cat,
                                @Field("unit") String unit);

    @FormUrlEncoded
    @POST("site_overview/")
    Call<String> siteOverview(@Field("site") String site);



    @FormUrlEncoded
    @POST("update_product/")
    Call<String> updateProdut(@Field("category") String category,
                                @Field("old_prod") String old_prod,
                                @Field("new_prod") String new_prod);

    @Multipart
    @POST("receive_vehicle/")
    Call<String> receiveVehicle(@Part("trans_id") RequestBody trans_id,
                                @Part("prod_list") RequestBody prod_list,
                                @Part("challan_num") RequestBody challan_num,
                                @Part MultipartBody.Part challan_img,
                                @Part MultipartBody.Part invoice_img,
                                @Part MultipartBody.Part onreceive_img,
                                @Part MultipartBody.Part unloaded_img);


    @FormUrlEncoded
    @POST("add_stock/")
    Call<String> addStock(@Field("site_id") String site_id,
                          @Field("site_type") String site_type,
                          @Field("category") String category,
                          @Field("product") String product,
                          @Field("qty") String qty);



    @FormUrlEncoded
    @POST("vehicle_list/")
    Call<String> vehicleList(@Field("site") int site);

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
