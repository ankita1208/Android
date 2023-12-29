package com.example.mobile_apis_framework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSignupActivity extends AppCompatActivity {

    private ApiRepository apiRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        apiRepository = new ApiRepository(this);

        // Set up login and signup buttons
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> openLoginScreen());
        signupButton.setOnClickListener(v -> openSignupScreen());
    }

    private void openLoginScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        builder.setView(view);

        builder.setPositiveButton("Login", (dialog, which) -> {
            EditText usernameEditText = view.findViewById(R.id.usernameEditText);
            EditText passwordEditText = view.findViewById(R.id.passwordEditText);

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginSignupActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
            } else {
                apiRepository.login(new LoginRequest(username, password), new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginSignupActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // After successful login, navigate to the main activity
                            startActivity(new Intent(LoginSignupActivity.this, MainActivity.class));
                            finish(); // Finish the current activity
                        } else {
                            Toast.makeText(LoginSignupActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginSignupActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void openSignupScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_signup, null);
        builder.setView(view);

        builder.setPositiveButton("Sign Up", (dialog, which) -> {
            EditText usernameEditText = view.findViewById(R.id.usernameEditText);
            EditText passwordEditText = view.findViewById(R.id.passwordEditText);
            EditText emailEditText = view.findViewById(R.id.emailEditText);

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(LoginSignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                apiRepository.signUp(new SignUpRequest(username, password, email), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginSignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginSignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LoginSignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
}