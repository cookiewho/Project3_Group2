package com.example.rocketcorner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextUsername, editTextPassword;
    private Button signIn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = pref.getString("user_id", null);

        if(user_id != null) {
            Intent intent = MainActivity.getIntent(getApplicationContext());
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }


    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
            case R.id.signIn:
                try {
                    userLogin();
                } catch (IOException ioe){
                    Toast.makeText(LoginActivity.this, "Error processing login credentials", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void userLogin() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Integer err = 0;

        if(username.isEmpty()){
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            err +=1;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            err +=1;
        }
        if(password.length() > 0 && password.length() < 6){
            editTextPassword.setError("Min password length is 6 characters");
            editTextPassword.requestFocus();
            err +=1;
        }

        if (err>=1){
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        Call<String> callAsync = rocketApi.createService().loginUser(username, password);
        callAsync.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.code() == 200)
                {
                    String apiResponse = response.body();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user_id", apiResponse);
                    editor.putString("user_password", password);
                    editor.apply();

                    Intent intent = MainActivity.getIntent(getApplicationContext());
                    startActivity(intent);
                } else if (response.code() == 403){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(LoginActivity.this, "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("Improper request Type :: " + response.code());
                    Toast.makeText(LoginActivity.this, "Devs didn't update the request type :^(", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, "Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });
    }

}