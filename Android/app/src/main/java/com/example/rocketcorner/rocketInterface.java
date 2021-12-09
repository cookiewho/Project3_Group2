package com.example.rocketcorner;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface rocketInterface {
    @POST("newUser")
    Call<String> registerUser(@Query("username") String fullName, @Query("email") String email, @Query("password") String password);

    @POST("login")
    Call<String> loginUser(@Query("username") String username, @Query("password") String password);

    @GET("getAllUsers")
    Call<Map<String, User>> getAllUserData();

    @PATCH("/updateCart")
    Call<Map<String, Integer>> updateCart(@Query("userId") String userId, @Query("password") String password, @Query("cartUpdatesMapStr") String cartUpdatesMapStr);

    @GET("/getCart")
    Call<Map<String, Integer>> getCart(@Query("userId") String userId, @Query("password") String password);

    @GET("getAllProducts")
    Call<Map<String, Product>> getAllProdData();

    @GET("getUser")
    Call<Map<String, User>> getUserData(@Query("userId") String userId);

    @GET("clearCart")
    Call<String> purchase(@Query("userId") String userId, @Query("password") String password);

    @PATCH("updateFunds")
    Call<Double> updateFunds(@Query("userId") String userId, @Query("newFunds") double newFunds);
}