package com.example.rocketcorner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketcorner.CartActivity;
import com.example.rocketcorner.HomeAdapters.FeaturedAdapter;
import com.example.rocketcorner.HomeAdaptersHelperClasses.FeaturedHelperClass;
import com.example.rocketcorner.ItemDetailsActivity;
import com.example.rocketcorner.LoginActivity;
import com.example.rocketcorner.MainActivity;
import com.example.rocketcorner.Product;
import com.example.rocketcorner.R;
import com.example.rocketcorner.User;
import com.example.rocketcorner.rocketApi;
import com.example.rocketcorner.rocketInterface;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String berry = "berry.png";
    private String ball = "ball.png";
    private String root = "root.png";

    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";
    Button cart;
    String user_id;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private TextView username;
    private TextView funds;

    ProgressBar progressBar;
    RecyclerView featuredItemsRecycler;
    RecyclerView.Adapter adapter;

    Context thiscontext;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        cart = view.findViewById(R.id.cart);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        username = view.findViewById(R.id.username);
        funds = view.findViewById(R.id.funds);
        featuredItemsRecycler = view.findViewById(R.id.featured_Items);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", null);

        if(user_id == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        loadUserData();

        featuredItemsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        fillItems();

        return view;
    }

    public void loadUserData(){
        Call<Map<String, User>> call = rocketApi.createService().getUserData(user_id);
        call.enqueue(new Callback<Map<String, User>>() {
            @Override
            public void onResponse(Call<Map<String, User>> call, Response<Map<String, User>> response) {
                if (response.code() == 200)
                {
                    Map<String, User> m = response.body();
                    User u = m.get(user_id);


                    username.setText("Welcome " + u.getUsername());
                    funds.setText("$".concat(df.format(u.getBalance()).toString()));

                    cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CartActivity.class);
                            Bundle args = new Bundle();
                            args.putSerializable("USER", (Serializable) u);
                            intent.putExtra("BUNDLE", args);
                            startActivity(intent);
                        }
                    });

                } else if (response.code() == 403){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, User>> call, Throwable t) {
                Log.d("== ERROR ==", t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<FeaturedHelperClass> fillItems(){
        ArrayList<FeaturedHelperClass> featuredItems = new ArrayList<>();

        Call<Map<String, Product>> itemCall = rocketApi.createService().getAllProdData();
        itemCall.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                if (response.code() == 200) {
                    Map<String, Product> m = response.body();
                    for (Map.Entry<String, Product> e: m.entrySet()){
                        Product prod = e.getValue();
                        if( prod.getName().equals("Bluk Berry") || prod.getName().equals("Slowpoke Tail") || prod.getName().equals("Dive Ball")) {
                            featuredItems.add(new FeaturedHelperClass(prod.getImgLink(), prod.getName(), prod.getDesc()));
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                    adapter = new FeaturedAdapter(getContext(), featuredItems);
                    featuredItemsRecycler.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<Map<String, Product>> call, Throwable t) {
                System.out.println("ERROR: "+t.getLocalizedMessage());
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(getActivity(), "Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });

        return featuredItems;
    }

}