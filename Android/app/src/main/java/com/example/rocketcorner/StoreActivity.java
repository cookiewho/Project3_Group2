
package com.example.rocketcorner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketcorner.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {
    List<String> items; // this will change into a class with its own attributes
    List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_page);
        RecyclerView rv = findViewById(R.id.rvItems);

        items = new ArrayList<>();
        images = new ArrayList<>();
        final ItemAdapter adapter = new ItemAdapter(this, items, images);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // simulate adding items from api
        items.add("Slowpoke Tail");
        items.add("Pikachu");
        items.add("Dragonair");
        images.add("https://i.imgur.com/tySRzD6.jpg");
        images.add("https://i.imgur.com/Zq0iBJK.jpg");
        images.add("https://i.imgur.com/GrwUHJO.png");
        adapter.notifyDataSetChanged();


    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, StoreActivity.class);
        return intent;
    }
}