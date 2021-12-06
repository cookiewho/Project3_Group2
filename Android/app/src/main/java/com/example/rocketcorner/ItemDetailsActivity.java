package com.example.rocketcorner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;


public class ItemDetailsActivity extends AppCompatActivity {
    TextView name;
    ImageView img;
    TextView desc;
    TextView price;
    Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                // TODO get user and add to their cart via /updateCart
                Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}