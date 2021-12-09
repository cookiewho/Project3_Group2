package com.example.rocketcorner;

import android.os.AsyncTask;
import android.os.StatFs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class rocketApi {
    private static String BASE_URL = "https://rocketcorner.herokuapp.com/";
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build();

    private static Gson gson = new GsonBuilder().setLenient().create();

    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .client(client).addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).build();
    private rocketInterface apiService = retrofit.create(rocketInterface.class);

    public static rocketInterface createService(){
        return retrofit.create(rocketInterface.class);
    }
}
