package com.example.rocketcorner;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface rocketInterface {
    @POST("newUser")
    Call<String> registerUser(@Query("username") String fullName, @Query("email") String email, @Query("password")String password);
    @POST("login")
    Call<String> loginUser(@Query("username") String username, @Query("password") String password);
    @GET("getAllUsers")
    Call<Map<String, User>> getUserData();
    @GET("getAllProducts")
    Call<Map<String, Product>> getProdData();

}