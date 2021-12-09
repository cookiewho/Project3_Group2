package com.example.rocketcorner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rocketcorner.LoginActivity;
import com.example.rocketcorner.MainActivity;
import com.example.rocketcorner.NumberTextWatcher;
import com.example.rocketcorner.R;
import com.example.rocketcorner.rocketApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FundsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FundsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button add_funds;
    private EditText amountText;

    private ProgressBar progressBar;

    public FundsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FundsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FundsFragment newInstance(String param1, String param2) {
        FundsFragment fragment = new FundsFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.Theme_RocketCorner);
        View view = inflater.inflate(R.layout.fragment_funds, container, false);

        add_funds = view.findViewById(R.id.add_funds);
        amountText = view.findViewById(R.id.fundAmount);
        amountText.addTextChangedListener(new NumberTextWatcher(amountText));
        amountText.setText("$0.00");

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        add_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountStr = amountText.getText().toString();
                if ((amountStr != null || !amountStr.isEmpty()) && !amountStr.equals("$0.00")){
                    System.out.println("FUNDS: INSIDE THE MAINFRAME");
                    updateUserFunds(Double.parseDouble(amountStr.replace("$","")), view);
                }
                else{
                    Toast.makeText(getActivity(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }
    public void updateUserFunds(double new_funds, View view){
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = pref.getString("user_id", null);

        Call<Double> callAsync = rocketApi.createService().updateFunds(user_id, new_funds);
        callAsync.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.code() == 200)
                {
                    Toast.makeText(getActivity(), "Funds added!", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(getActivity(), "Invalid amount, try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 403){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(getActivity(), "Invalid User, please login", Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(getActivity(), "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("Improper request Type :: " + response.code());
                    Toast.makeText(getActivity(), "Devs didn't update the request type :^(", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(getActivity(), "Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}