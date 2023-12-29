package com.example.mobile_apis_framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mobile_apis_framework.ApiService;
import com.example.mobile_apis_framework.Book;
import com.example.mobile_apis_framework.LoginRequest;
import com.example.mobile_apis_framework.LoginResponse;
import com.example.mobile_apis_framework.SignUpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static final String TOKEN_KEY = "auth_token";

    private final ApiInstance apiInstance;
    private final SharedPreferences sharedPreferences;

    public ApiRepository(Context context) {
        apiInstance = ApiInstance.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void signUp(SignUpRequest signUpRequest, Callback<Void> callback) {
        apiInstance.getApiService().signUp(signUpRequest).enqueue(callback);
    }

    public void login(LoginRequest loginRequest, Callback<LoginResponse> callback) {
        apiInstance.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    saveToken(token);
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void getBooks(Callback<List<Book>> callback) {
        String token = getToken();
        if (token != null) {
            apiInstance.getApiService().getBooks("Bearer " + token).enqueue(callback);
        }
    }
    public void addBook(Book book, Callback<Book> callback) {
        Call<Book> call = apiInstance.getApiService().addBook(getToken(), book);
        call.enqueue(callback);
    }

    public void editBook(String bookId, Book book, Callback<Book> callback) {
        Call<Book> call = apiInstance.getApiService().editBook(getToken(), bookId, book);
        call.enqueue(callback);
    }

    public void deleteBook(String id , Callback<Void> callback) {
        Call<Void> call = apiInstance.getApiService().deleteBook(id);
        call.enqueue(callback);
    }

    public void logout(Callback<Void> callback) {
        Call<Void> call = apiInstance.getApiService().logout();
        call.enqueue(callback);
    }

    private void saveToken(String token) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }

    private String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }
}
