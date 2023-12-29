package com.example.mobile_apis_framework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnItemClickListener {

    private ApiRepository apiRepository;
    private RecyclerView recyclerView;
    private Button logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiRepository = new ApiRepository(this);
        recyclerView = findViewById(R.id.booksRecyclerView);
        logoutButton = findViewById(R.id.logoutButton);
        FloatingActionButton addBookFab = findViewById(R.id.addBookFab);
        addBookFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddEditActivity when the FAB is clicked
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddEditActivity when the FAB is clicked
                logout();
            }
        });

        // Get books
        getBooks();
    }

    private void getBooks() {
        apiRepository.getBooks(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    displayBooks(books);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBooks(List<Book> books) {
        BookAdapter bookAdapter = new BookAdapter(books, this, new BookAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Book book) {
                // Call the method to delete the book
                deleteBook(book);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);
    }

    private void deleteBook(Book book) {
        apiRepository.deleteBook(book.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the book list after deletion
                    getBooks();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Book book) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra("EDIT_BOOK",  book);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBooks();
    }

    private void logout() {
        apiRepository.logout(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Redirect to the login screen or perform other logout actions
                    redirectToLoginScreen();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

}
