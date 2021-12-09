package com.example.rocketcorner;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUser extends AppCompatActivity implements  View.OnClickListener{

    private TextView registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private ImageView banner;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (ImageView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Integer err = 0;

//        Checks if fields are entered and valid
        if(fullName.isEmpty()){
            editTextFullName.setError("Name is required");
            editTextFullName.requestFocus();
            err +=1;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            err +=1;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            err +=1;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            err +=1;
        }
        if(password.length() > 0 && password.length() < 6){
            editTextPassword.setError("Password must be more than 6 characters");
            editTextPassword.requestFocus();
            err +=1;
        }

        if (err>=1){
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        Call<String> callAsync = rocketApi.createService().registerUser(fullName, email, password);
        callAsync.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(response.toString());
                if (response.code() == 200)
                {
                    String apiResponse = response.body();
                    System.out.println("user created: " + apiResponse);

                    //add userId to persistence here

                    Toast.makeText(RegisterUser.this, "User has been registered", Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 403) {
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(RegisterUser.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                }
                    else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(RegisterUser.this, "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("Improper request Type :: " + response.code());
                    Toast.makeText(RegisterUser.this, "Devs didn't update the request type :^(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("Network 2 :: " + t);
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(RegisterUser.this, "Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });

    }
}