package com.example.mobile_apis_framework;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditActivity extends AppCompatActivity {

    private ApiRepository apiRepository;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText ratingEditText;
    private EditText isbnEditText;

    private boolean isEditMode = false;
    private Book bookToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        apiRepository = new ApiRepository(this);

        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        ratingEditText = findViewById(R.id.ratingEditText);
        isbnEditText = findViewById(R.id.isbnEditText);

        Button submitButton = findViewById(R.id.submitButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Check if this activity is for editing an existing book
        if (getIntent().hasExtra("EDIT_BOOK")) {
            isEditMode = true;
            bookToEdit = (Book) getIntent().getSerializableExtra("EDIT_BOOK");
            titleEditText.setText(bookToEdit.getTitle());
            authorEditText.setText(bookToEdit.getAuthor());
            ratingEditText.setText(String.valueOf(bookToEdit.getRating()));
            isbnEditText.setText(String.valueOf(bookToEdit.getIsbn()));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    editBook();
                } else {
                    addBook();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });
    }

    private void editBook() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String ratingStr = ratingEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(ratingStr)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double rating = Double.parseDouble(ratingStr);

        apiRepository.editBook(bookToEdit.getId(), new Book(title, author, rating), new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    onEditCallback(true, "Book edited successfully");
                } else {
                    onEditCallback(false, "Failed to edit book");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                onEditCallback(false, "Failed to edit book");
            }
        });
    }


    private void onEditCallback(boolean success, String message) {
        if (success) {
            Toast.makeText(AddEditActivity.this, message, Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(AddEditActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }


    private void addBook() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String ratingStr = ratingEditText.getText().toString().trim();
        String isbn = isbnEditText.getText().toString().trim();


        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(ratingStr) || TextUtils.isEmpty(isbn)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double rating = Double.parseDouble(ratingStr);

        apiRepository.addBook(new Book(title, author, rating, isbn, title), new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    onAddCallback(true, "Book added successfully");
                } else {
                    onAddCallback(false, "Failed to add book");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                onAddCallback(false, "Failed to add book");
            }
        });
    }

    private void onAddCallback(boolean success, String message) {
        if (success) {
            Toast.makeText(AddEditActivity.this, message, Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(AddEditActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
