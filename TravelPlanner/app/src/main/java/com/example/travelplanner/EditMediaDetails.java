package com.example.travelplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditMediaDetails extends AppCompatActivity {

    private ImageView editMediaImageView;
    private EditText editMediaTitleEditText, editMediaDescriptionEditText;
    private Button saveChangesButton, cancelBT;

    private TextView uploadMediaButton;
    private AppDatabase mediaDatabase;
    private ImageView addMediaImageView;
    private static final int REQUEST_IMAGE_PICK = 2;

    private int id; // Assuming you have the ID passed from the previous activity
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_details);
        mediaDatabase = AppDatabase.getInstance(this);
        addMediaImageView = findViewById(R.id.addMediaImageView);
        editMediaTitleEditText = findViewById(R.id.addMediaTitleEditText);
        editMediaDescriptionEditText = findViewById(R.id.addMediaDescriptionEditText);
        saveChangesButton = findViewById(R.id.addMediaButton);
        cancelBT = findViewById(R.id.cancelBT);
        uploadMediaButton = findViewById(R.id.selectMediaButton);

        // Initialize your database instance (mediaDatabase)

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("MEDIA_ID", 0);
            String mediaTitle = extras.getString("MEDIA_TITLE", "");
            String mediaDescription = extras.getString("MEDIA_DESCRIPTION", "");
            String mediaImageUrl = extras.getString("MEDIA_IMAGE_URL", "");
            editMediaTitleEditText.setText(mediaTitle);
            editMediaDescriptionEditText.setText(mediaDescription);
            String filePath = mediaImageUrl;
            String mediaId = filePath.substring(filePath.lastIndexOf("/") + 1);

            // Construct content URI
            Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaId);

            Picasso.get().invalidate(contentUri);

// Load image using Picasso with error handling

            // Load image using Picasso with InputStream
            Picasso.get()
                    .load(contentUri)
                    .into(addMediaImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("exception", e.getMessage());
                            addMediaImageView.setImageResource(R.drawable.ic_launcher_background);
                        }
                    });
        }

        uploadMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaChooser();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            Toast.makeText(this, "Selected Image: " + selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChanges() {
        String updatedTitle = editMediaTitleEditText.getText().toString().trim();
        String updatedDescription = editMediaDescriptionEditText.getText().toString().trim();

        if (!updatedTitle.isEmpty() && !updatedDescription.isEmpty()) {
            if (selectedImageUri != null) {
                String imageUrl = selectedImageUri.toString();

                // Use AsyncTask to perform database operation off the main thread
                UpdateMediaTask updateMediaTask = new UpdateMediaTask(mediaDatabase, id, updatedTitle, updatedDescription, imageUrl);
                updateMediaTask.execute();
                finish();
            } else {
                Toast.makeText(this, "Please update the image", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "Title or description is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private static class UpdateMediaTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase mediaDatabase;
        private int id;
        private String updatedTitle;
        private String updatedDescription;
        private String imageUrl;

        UpdateMediaTask(AppDatabase database, int id, String updatedTitle, String updatedDescription, String imageUrl) {
            this.mediaDatabase = database;
            this.id = id;
            this.updatedTitle = updatedTitle;
            this.updatedDescription = updatedDescription;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mediaDatabase.mediaDao().updateMediaWithImage(id, updatedTitle, updatedDescription, imageUrl);
            return null;
        }
    }
}

