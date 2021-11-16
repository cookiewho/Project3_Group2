package com.example.rocketcorner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button button;
    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Testing retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rocketAPI rocketApi = retrofit.create(rocketAPI.class);

        System.out.println("==== USERS ====");

        Call<Map<String, User>> call = rocketApi.getUserData();
        call.enqueue(new Callback<Map<String, User>>() {
            @Override
            public void onResponse(Call<Map<String, User>> call, Response<Map<String, User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("== Response ==", "Response is outside of the 200-300 range!");
                    return;
                }
                // idea: create another class that returns the data of the dictionary
                // load the map from the response and use that map for whatever??
                Map<String, User> m = response.body();
//                User u = m.get("th09DJpV5zP5lVWdcHFd");
                for (Map.Entry entry : m.entrySet()){
                    String id = (String)entry.getKey();
                    User u = (User)entry.getValue();
                    Log.d("== Response ==", id + "->" + u.toString());
                }
            }

            @Override
            public void onFailure(Call<Map<String, User>> call, Throwable t) {
                Log.d("== ERROR ==", t.getMessage());
            }
        });

        button = findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStore(view);
            }
        });
    }

    private void loadStore(View view){
        Intent intent = StoreActivity.getIntent(getApplicationContext());
        startActivity(intent);
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}