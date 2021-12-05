package com.example.rocketcorner;

//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button button;

    public static final String BASE_URL = "http://rocketcorner.herokuapp.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("accountDetails", Context.MODE_PRIVATE);
        String user = pref.getString("user", null);

        if(user != null) {
            Intent intent = MainActivity.getIntent(getApplicationContext());
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        
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
                break;

            case R.id.signIn:
                userLogin();
                break;
        }
    }


    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
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


        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    rocketAPI rocketApi = retrofit.create(rocketAPI.class);
                    Call<Map<String, User>> call = rocketApi.getUserData();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("accountDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();

                    call.enqueue(new Callback<Map<String, User>>() {
                        @Override
                        public void onResponse(Call<Map<String, User>> call, Response<Map<String, User>> response) {
                            if (!response.isSuccessful()) {
                                Log.d("== Response ==", "Response is outside of the 200-300 range!");
                                return;
                            }

                            Map<String, User> m = response.body();
                            for(Map.Entry<String, User> e : m.entrySet()){
                                if (e.getValue().getEmail().equals(email) && e.getValue().getPassword().equals(password)){
                                    Map<String, User> user = (Map<String, User>) e;
                                    String user_string = user.toString();
                                    editor.putString("user", user_string);
                                    editor.apply();
                                    break;
                                }
                            }

                            Intent intent = MainActivity.getIntent(getApplicationContext());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Map<String, User>> call, Throwable t) {
                            Log.d("== ERROR ==", t.getMessage());
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}