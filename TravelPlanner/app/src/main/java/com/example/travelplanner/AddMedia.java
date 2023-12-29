package com.example.travelplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class AddMedia extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView addMediaImageView;
    private Button  addMediaButton;
    private TextView selectMediaButton;
    private EditText addMediaTitleEditText, addMediaDescriptionEditText;
    private AppDatabase mediaDatabase;
    private Uri selectedImageUri; // To store the selected image URI

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);

        addMediaImageView = findViewById(R.id.addMediaImageView);
        selectMediaButton = findViewById(R.id.selectMediaButton);
        addMediaTitleEditText = findViewById(R.id.addMediaTitleEditText);
        addMediaDescriptionEditText = findViewById(R.id.addMediaDescriptionEditText);
        addMediaButton = findViewById(R.id.addMediaButton);

        // Initialize your AppDatabase instance
        mediaDatabase = AppDatabase.getInstance(this);

        selectMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaChooser();
            }
        });

        addMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMediaToDatabase();
            }
        });
    }

    private void openMediaChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            addMediaImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveMediaToDatabase() {
            String title = addMediaTitleEditText.getText().toString().trim();
            String description = addMediaDescriptionEditText.getText().toString().trim();
            String mediaUrl = getMediaUrl(selectedImageUri);

            if (!title.isEmpty() && !description.isEmpty() && mediaUrl != null) {
                MediaEntity mediaEntity = new MediaEntity(title, description, mediaUrl);

                // Use AsyncTask to perform database operation in the background
                new InsertMediaAsyncTask(mediaDatabase.mediaDao()).execute(mediaEntity);

                finish();
            } else {
                Toast.makeText(this, "Title, description, or media URL is empty", Toast.LENGTH_SHORT).show();
            }
        }

        private static class InsertMediaAsyncTask extends AsyncTask<MediaEntity, Void, Void> {
            private MediaDao mediaDao;

            private InsertMediaAsyncTask(MediaDao mediaDao) {
                this.mediaDao = mediaDao;
            }

            @Override
            protected Void doInBackground(MediaEntity... mediaEntities) {
                mediaDao.insertMedia(mediaEntities[0]);
                return null;
            }
        }

    // Convert the image URI to a URL
    private String getMediaUrl(Uri imageUri) {
        if (imageUri != null) {
            // Assuming the image URI is in the form of a file URI
            File file = new File(imageUri.getPath());
            return file.getAbsolutePath();
        }
        return null;
    }
}