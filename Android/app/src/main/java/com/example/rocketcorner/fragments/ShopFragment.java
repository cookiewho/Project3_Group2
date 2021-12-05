package com.example.rocketcorner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rocketcorner.ItemDetailsActivity;
import com.example.rocketcorner.Product;
import com.example.rocketcorner.R;
import com.example.rocketcorner.adapters.ItemAdapter;
import com.example.rocketcorner.rocketAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment implements ItemAdapter.OnItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rv;
    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";
    HashMap<String, Product> products = new HashMap<>();
    ArrayList<Map.Entry<String, Product>> product_list = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rocketAPI rocketApi = retrofit.create(rocketAPI.class);
        Call<Map<String, Product>> call = rocketApi.getProdData();
        ShopFragment sh = this;

        call.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                if (!response.isSuccessful()) {
                    Log.d("== Response ==", "Response is outside of the 200-300 range!");
                    return;
                }


                Map<String, Product> m = response.body();
                for (Map.Entry<String, Product> e: m.entrySet()){
                    product_list.add(e);
                }

                rv = view.findViewById(R.id.rvItems);
                final ItemAdapter adapter = new ItemAdapter(view.getContext(), product_list, sh);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Map<String, Product>> call, Throwable t) {
                Log.d("== ERROR ==", t.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        String k = product_list.get(position).getKey();
        Product p = product_list.get(position).getValue();

        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        Bundle args = new Bundle();
        args.putString("KEY", k);
        args.putSerializable("VALUE", (Serializable) p);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }
}