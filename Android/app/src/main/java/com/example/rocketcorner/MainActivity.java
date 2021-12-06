package com.example.rocketcorner;

//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextUsername, editTextPassword;
    private Button signIn;

    private boolean mAuth;
    private ProgressBar progressBar;
    private Button button;
    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.signIn:
                try {
                    userLogin();
                } catch (IOException ioe){
                    Toast.makeText(MainActivity.this, "Error processing login credentials", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void userLogin() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Min password length is 6 characters");
            editTextPassword.requestFocus();
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
                    System.out.println("Respnse: " + apiResponse);

                    //add userId to persistence here

                    Intent intent = profileActivity.getIntent(getApplicationContext());
                    startActivity(intent);
                } else if (response.code() == 403){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500){
                    System.out.println("Request Error :: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Internal Server Error, try again later!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("Network Error :: " + t.getLocalizedMessage());
                Toast.makeText(MainActivity.this, "Request Error, try again", Toast.LENGTH_LONG).show();
            }
        });



    }

}