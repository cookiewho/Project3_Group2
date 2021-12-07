package com.example.rocketcorner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rocketInterface rocketApi = retrofit.create(rocketInterface.class);
        Call<Map<String, Product>> call = com.example.rocketcorner.rocketApi.createService().getAllProdData();
        call.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                Map<String, Product> allProd = response.body();

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

            }
        });





        /*
        IDEA
        1. have adapter contain a <product, int> list pair, set it it null
        2. when populate check if this is set to null, if it is then we pull data from original dataset otherwise we pull data from this
        3. make an invisible amount textview and set it to visible if its not null
         */
    }

}