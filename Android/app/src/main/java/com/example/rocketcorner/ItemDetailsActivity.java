package com.example.rocketcorner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ItemDetailsActivity extends AppCompatActivity {
    TextView name;
    ImageView img;
    TextView desc;
    TextView price;
    Button buyButton;
    Map<String, Integer> cartMap;
    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //first thing first we need to get the cart from the API
        cartMap = new HashMap<>();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = pref.getString("user_id", "");
        String password = pref.getString("user_password", "");
        Call<Map<String, Integer>> callAsync = rocketApi.createService().getCart(username, password);
        callAsync.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.code() == 200)
                {
                    cartMap = response.body();

                } else if (response.code() == 403){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(ItemDetailsActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(ItemDetailsActivity.this, "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                String m = call.request().url().toString();
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(ItemDetailsActivity.this, "First Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        String id = args.getString("KEY");
        Product p = (Product) args.getSerializable("VALUE");

        name = findViewById(R.id.item_name);
        img = findViewById(R.id.item_image);
        desc = findViewById(R.id.item_description);
        price = findViewById(R.id.item_price);
        buyButton = findViewById(R.id.buy_button);

        name.setText(p.getName());
        Glide.with(getApplicationContext()).load(p.getImgLink()).into(img);
        desc.setText(p.getDesc());
        price.setText("$" + Double.toString(p.getPrice()));

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = cartMap.get(id);
                if (value !=null) {
                    cartMap.put(id, value + 1);
                } else {
                    cartMap.put(id, 1);
                }

                ObjectMapper m = new ObjectMapper();
                String mapStr = null;
                try {
                    mapStr = m.writeValueAsString(cartMap);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                mapStr = mapStr.substring(1, mapStr.length() - 1);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                String username = pref.getString("user_id", "");
                String password = pref.getString("user_password", "");
                Call<Map<String, Integer>> callAsync = rocketApi.createService().updateCart(username, password, mapStr);
                callAsync.enqueue(new Callback<Map<String, Integer>>() {
                    @Override
                    public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                        if (response.code() == 200)
                        {
                            cartMap = response.body();
                            Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();

                            Intent intent = MainActivity.getIntent(getApplicationContext());
                            startActivity(intent);
                            
                        } else if (response.code() == 403){
                            System.out.println("Request Error :: " + response.errorBody().toString());
                            Toast.makeText(ItemDetailsActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                        else if (response.code() == 500){
                            System.out.println("Request Error :: " + response.errorBody().toString());
                            Toast.makeText(ItemDetailsActivity.this, "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                        Toast.makeText(ItemDetailsActivity.this, "Request Error, try again", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}