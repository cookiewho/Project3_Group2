package com.example.rocketcorner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketcorner.adapters.CartAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";
    Map<String, Product> allProd;
    Button purchase;
    double balance = 0;
    String user_id;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        User u = (User) args.getSerializable("USER");


        Call<Map<String, Product>> call = rocketApi.createService().getAllProdData();
        call.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                allProd = response.body();

                HashMap<Product, Integer> cart = new HashMap<>();
                for(Map.Entry<String, Integer> e:u.getCart().entrySet()){
                    cart.put(allProd.get(e.getKey()), e.getValue());
                }

                ArrayList<Map.Entry<Product, Integer>> cart_list = new ArrayList<>();
                for(Map.Entry<Product, Integer> e : cart.entrySet()){
                    cart_list.add(e);
                }

                RecyclerView rv = findViewById(R.id.rvItems);
                final CartAdapter adapter = new CartAdapter(getApplicationContext(), cart_list);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Map<String, Product>> call, Throwable t) {
                Log.d("==ERROR==", t.toString());
            }
        });

        purchase = findViewById(R.id.purchase);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", null);
        password = pref.getString("password", null);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> call = rocketApi.createService().purchase(user_id, password);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();

                        AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                        alert.setMessage(result);
                        alert.setNegativeButton(android.R.string.ok, null);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("==ERROR==", t.toString());
                    }
                });

            }
        });
    }

}