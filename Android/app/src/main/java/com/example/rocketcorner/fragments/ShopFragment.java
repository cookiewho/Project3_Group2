package com.example.rocketcorner.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rocketcorner.ItemDetailsActivity;
import com.example.rocketcorner.R;
import com.example.rocketcorner.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> items;
    private List<String> images;
    private RecyclerView rv;
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
        rv = view.findViewById(R.id.rvItems);

        // TODO items & images may change when pulling from api
        items = new ArrayList<>();
        images = new ArrayList<>();
        final ItemAdapter adapter = new ItemAdapter(view.getContext(), items, images, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        items.add("Slowpoke Tail");
        items.add("Pikachu");
        items.add("Dragonair");
        images.add("https://i.imgur.com/tySRzD6.jpg");
        images.add("https://i.imgur.com/Zq0iBJK.jpg");
        images.add("https://i.imgur.com/GrwUHJO.png");
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        // TODO replace to be product class
        List<String> result = new ArrayList<>();
        result.add(items.get(position));
        result.add(images.get(position));

        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
//        intent.putExtra("product", "RESULT");
        startActivity(intent);
    }
}