package com.example.rocketcorner;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface rocketAPI {
    @GET("getAllUsers")
    Call<Map<String, User>> getUserData();
    @GET("getAllProducts")
    Call<Map<String, Product>> getProdData();
}
