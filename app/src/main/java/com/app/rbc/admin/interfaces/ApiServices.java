package com.app.rbc.admin.interfaces;



import com.app.rbc.admin.fragments.Stock_categories;
import com.app.rbc.admin.models.DatewiseAttendance;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.EmployeewiseAttendance;
import com.app.rbc.admin.models.Otp;
import com.app.rbc.admin.models.StockCategories;
import com.app.rbc.admin.models.StockPoDetails;
import com.app.rbc.admin.models.StockProductDetails;
import com.app.rbc.admin.models.Tasklogs;
import com.app.rbc.admin.models.TodaysAbsentees;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.models.login;

import org.json.JSONArray;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServices {
    /*
   Retrofit get annotation with our URL
   And our method that will return us the List of ContactList
   */

    /*Vendor login api REQUEST*/
    @FormUrlEncoded
    @POST("auth_view_admin/")
    Call<login> vendorLogin(@Field("user_id") String username , @Field("pwd") String password ,@Field("gcm_id")String gcm_id);


    /*User Otp REQUEST*/
    @FormUrlEncoded
    @POST("send_otp/")
    Call<Otp> send_otp(@Field("user_id") String username ,@Field("phone") String phone );

    /*User Verify Otp REQUEST*/
    @FormUrlEncoded
    @POST("verify_otp/")
    Call<login> verify_otp(@Field("user_id") String username ,@Field("otp") String otp, @Field("phone") String phone);


    /*User Update Password*/
    @FormUrlEncoded
    @POST("forget_pwd/")
    Call<ResponseBody> forget_pwd(@Field("user_id") String username ,@Field("new_pwd") String password );


    /*Fetch user list*/
    @FormUrlEncoded
    @POST("fetch_emp/")
    Call<Employee> fetch_emp(@Field("user_id") String username);


    /* Create Task */
    @Multipart
    @POST("create_task/")
    Call<ResponseBody> create_task(@Part("task_type") RequestBody task_type , @Part("to_user") RequestBody to_user , @Part("from_user") RequestBody from_user , @Part("title") RequestBody title , @Part("desc") RequestBody desc , @Part("deadline") RequestBody deadline , @Part("docs") RequestBody docs , @Part MultipartBody.Part file);



    /*To-do List */
    @FormUrlEncoded
    @POST("todo_list/")
    Call<Todolist> todo_list(@Field("user_id") String username);


    /*Task details */
    @FormUrlEncoded
    @POST("task_details/")
    Call<Tasklogs> task_details(@Field("task_id") String task_id , @Field("user_id") String username);

    /*Update task*/
    @Multipart
    @POST("update_task/")
    Call<ResponseBody> update_task(@Part("log_type") RequestBody log_type,@Part("cmt") RequestBody comment,@Part("status") RequestBody status,@Part("user_id") RequestBody username ,@Part("task_id") RequestBody task_id ,@Part("type") RequestBody type , @Part("docs") RequestBody docs ,@Part MultipartBody.Part file);

    /*Delete Task*/
    @FormUrlEncoded
    @POST("delete_task/")
    Call<ResponseBody> delete_task(@Field("user_id") String username ,@Field("task_id") String task_id );

    /*Mark Attendance */
    @FormUrlEncoded
    @POST("mark_attendance/")
    Call<ResponseBody> mark_attendance(@Field("data") JSONArray attendance , @Field("date") String date);

    /*Fetch Attendance datewise*/
    @FormUrlEncoded
    @POST("daywise_search_admin/")
    Call<DatewiseAttendance> daywise_search_admin( @Field("date") String date);

    /*Fetch Attendance employee-wise*/
    @FormUrlEncoded
    @POST("emp_wise_attendance/")
    Call<EmployeewiseAttendance> emp_wise_attendance(@Field("user_id") String user_id,@Field("date") String date);

    /*Fetch todays attendance*/
    @GET("absent_hd_status")
    Call<TodaysAbsentees> absent_hd_status();

    /*Fetch Attendance monthwise*/
    @FormUrlEncoded
    @POST("monthwise_search_admin/")
    Call<TodaysAbsentees> monthwise_search_admin( @Field("year") String year, @Field("month") String month);

    /*Fetch Attendance monthwise*/
    @FormUrlEncoded
    @POST("monthwise_search/")
    Call<EmployeewiseAttendance> monthwise_search(@Field("user_id") String user_id, @Field("year") String year, @Field("month") String month);



    /*Fetch stock categories*/
    @GET("stock_category")
    Call<StockCategories> stock_category();

    /*Fetch Stock Products*/
    @FormUrlEncoded
    @POST("stock_product_list/")
    Call<StockProductDetails> stock_product_list(@Field("product") String product);


    /*Fetch PO Details*/
    @FormUrlEncoded
    @POST("po_details/")
    Call<StockPoDetails> po_details(@Field("po_num") String po_number);


//
//    /*Task_assigned */
//    @FormUrlEncoded
//    @POST("tasks_assigned/")
//    Call<Todolist> tasks_assigned(@Field("user_id") String username);








//    /*Subscription package list API*/
//    @GET("v1/services-list/")
//    Call<SubscriptonList> getSubscriptionList(@Header("Authorization") String Authorization);
//
//    /*Order Status list*/
//    @GET("customapi/orders-list/")
//    Call<OrdersStatus> getOrdersStatus(@Header("Authorization") String Authorization);
//
//    /*Order Details*/
//    @FormUrlEncoded
//    @POST("customapi/orders-detail/")
//    Call<OrderDetails> getOrdersDetails(@Field("order_number") String order_number, @Header("Authorization") String Authorization);
//
//    /*Order Acccept,reject and shhipped  API*/
//    @FormUrlEncoded
//    @POST("customapi/orders-shipping-status/")
//    Call<OrdersStatus> getChangeOrderStatus(@Field("order_number") String order_number, @Field("status") String status, @Field("reason") String reason, @Header("Authorization") String Authorization);
//
//    /* HERE CRM BASED API BE CARE FULL */
//
////    @FormUrlEncoded
////    @POST("api/v1/customers/")
////    Call<ResponseBody>setCrmNumber(@Field("mobile")String mobilenumber,@Header("Authorization") String Authorization);
////
//
//    @FormUrlEncoded
//    @POST("v1/customers/sms/1")
//    Call<ResponseBody> sendMessage(@Field("mobile") String reason, @Header("Authorization") String Authorization);
//
//
//    //    @FormUrlEncoded
////    @POST("customapi/dashboard_details/")
////    Call<ResponseBody>getDashboardData(@Field("order_number")String order_number,@Header("Authorization") String Authorization);
//    /*Total list of customers api*/
//    @GET("v1/customers/")
//    Call<ListOfCustomers> getCustomerList(@Header("Authorization") String Authorization);
//
//    @GET("v1/website-users/")
//    Call<OnlineCustomerList> getOnLineCustomer(@Header("Authorization") String Authorization);
//
//
////    @FormUrlEncoded
////    @POST("api/v1/customers/")
////    Call<ResponseBody>sendCustomeSignup(@Field("first_name")String first_name,@Field("last_name")String last_name,@Field("email")String email,@Field("mobile")String mobile,@Header("Authorization") String Authorization);
//
//    /*customer registration and save mobile number apis*/
//    @POST("v1/customers/")
//    Call<LoginModel> sendCustomeSignup(@Header("Authorization") String Authorization, @Body LoginModel loginModel);
//
//
//    @GET("v1/dash-details/")
//    Call<DashboardDetail> getCrmData(@Header("Authorization") String Authorization);
//
//
///*////////////////////////////RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR*/
//
//
//    @GET("customapi/category-list/")
//    Call<Category> getCategories(@Header("Authorization") String Authorization);
//
//
//    /*Product create*/
//    @FormUrlEncoded
//    @POST("customapi/product-create/")
//    Call<ResponseBody> createProduct(@Field("data") JSONArray data, @Header("Authorization") String Authorization);
//
//
//    /*Product List*/
//    @GET("customapi/products-list/")
//    Call<Products> getProducts(@Header("Authorization") String Authorization);
//
//    /*Product update*/
//    @FormUrlEncoded
//    @POST("customapi/product-update/")
//    Call<ResponseBody> upadteProduct(@Field("data") JSONArray data, @Header("Authorization") String Authorization);
//
//    /*Create Order*/
//    @FormUrlEncoded
//    @POST("customapi/offline-basket/")
//    Call<Checkout>placeOrder(@Field("data") JSONObject data, @Header("Authorization") String Authorization);
//
//
//    /*Create Order*/
//    @FormUrlEncoded
//    @POST("customapi/offline-orders/")
//
//    Call<ResponseBody>confirmOrder(@Field("data") JSONObject data, @Header("Authorization") String Authorization);
//
//    /*Product inventory update*/
//    @FormUrlEncoded
//    @POST("customapi/inventory-update/")
//    Call<ResponseBody>upadteInventory(@Field("data") JSONArray data, @Header("Authorization") String Authorization);
//
//    @FormUrlEncoded
//    @POST("v1/service-price/")
//    Call<ResponseBody>priceChecker(@Field("data") JSONObject data, @Header("Authorization") String Authorization);
//
//    @FormUrlEncoded
//    @POST("v1/subscription-activation/")
//    Call<ResponseBody>activateService(@Field("data") JSONObject data, @Header("Authorization") String Authorization);


}
