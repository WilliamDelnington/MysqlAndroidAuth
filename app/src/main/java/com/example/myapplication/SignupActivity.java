package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignupActivity extends AppCompatActivity {
    TextInputEditText getFullname, getUsername, getEmail, getPassword, getRetypePassword;
    Button signupSubmit;
    TextView loginRoute;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SignupActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getFullname = findViewById(R.id.getFullname);
        getUsername = findViewById(R.id.getUsername);
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        getRetypePassword = findViewById(R.id.getRetypePassword);
        signupSubmit = findViewById(R.id.signupSubmit);
        loginRoute = findViewById(R.id.loginRoute);
        progressBar = findViewById(R.id.progressBar);

        loginRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname, username, email, password, retypePassword;

                fullname = String.valueOf(getFullname.getText());
                username = String.valueOf(getUsername.getText());
                email = String.valueOf(getEmail.getText());
                password = String.valueOf(getPassword.getText());
                retypePassword = String.valueOf(getRetypePassword.getText());

                if (fullname.isEmpty()
                        || username.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
                        || retypePassword.isEmpty()
                ) {
                    Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                } else if (!retypePassword.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match.", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "email";
                            field[3] = "password";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = email;
                            data[3] = password;
                            PutData putData = new PutData("http://192.168.56.1/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                    Log.i("PutData", result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            }
        });
    }
}