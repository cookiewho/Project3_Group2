package com.example.rocketcorner;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rocketcorner.adapters.ItemAdapter;
import java.util.ArrayList;
import java.util.List;


import com.example.rocketcorner.HomeAdapters.CategoriesAdapter;
import com.example.rocketcorner.HomeAdapters.FeaturedAdapter;
import com.example.rocketcorner.HomeAdapters.MostViewedAdpater;
import com.example.rocketcorner.HomeAdaptersHelperClasses.CategoriesHelperClass;
import com.example.rocketcorner.HomeAdaptersHelperClasses.FeaturedHelperClass;
import com.example.rocketcorner.HomeAdaptersHelperClasses.MostViewedHelperClass;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    RecyclerView.Adapter adapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);

        //Functions will be executed automatically when this activity will be created
        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();

    }
    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    private void categoriesRecycler() {

        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient1, R.drawable.search_icon, "Education"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient2, R.drawable.search_icon, "HOSPITAL"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient3, R.drawable.search_icon, "Restaurant"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient4, R.drawable.search_icon, "Shopping"));
        categoriesHelperClasses.add(new CategoriesHelperClass(gradient1, R.drawable.search_icon, "Transport"));


        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);

    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedItems = new ArrayList<>();
        mostViewedItems.add(new MostViewedHelperClass(R.drawable.search_icon, "McDonald's"));
        mostViewedItems.add(new MostViewedHelperClass(R.drawable.search_icon, "Edenrobe"));
        mostViewedItems.add(new MostViewedHelperClass(R.drawable.search_icon, "J."));
        mostViewedItems.add(new MostViewedHelperClass(R.drawable.search_icon, "Walmart"));

        adapter = new MostViewedAdpater(mostViewedItems);
        mostViewedRecycler.setAdapter(adapter);

    }

    private void featuredRecycler() {


        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredItems = new ArrayList<>();

        featuredItems.add(new FeaturedHelperClass(R.drawable.search_icon, "Mcdonald's", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        featuredItems.add(new FeaturedHelperClass(R.drawable.search_icon, "Edenrobe", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));
        featuredItems.add(new FeaturedHelperClass(R.drawable.search_icon, "Walmart", "asbkd asudhlasn saudnas jasdjasl hisajdl asjdlnas"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);


    }
}
