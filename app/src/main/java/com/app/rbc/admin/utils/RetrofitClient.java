package com.app.rbc.admin.utils;



import com.app.rbc.admin.interfaces.ApiServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitClient {


    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://plethron.pythonanywhere.com/eagle/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiServices getApiService() {
        return getRetrofitInstance().create(ApiServices.class);
    }
}
