package com.example.mobile_apis_framework;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/api/signup")
    Call<Void> signUp(@Body SignUpRequest signUpRequest);

    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/books")
    Call<List<Book>> getBooks(@Header("Authorization") String token);

    @POST("/api/books")
    Call<Book> addBook(@Header("Authorization") String token, @Body Book book);

    @PUT("/api/books/{id}")
    Call<Book> editBook(@Header("Authorization") String token, @Path("id") String bookId, @Body Book book);

    @DELETE("/api/books/{id}")
    Call<Void> deleteBook(@Path("id") String bookId);

    @POST("/api/logout")
    Call<Void> logout();
}
