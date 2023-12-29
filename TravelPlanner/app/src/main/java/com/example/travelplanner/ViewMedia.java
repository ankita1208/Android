package com.example.travelplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

public class ViewMedia extends AppCompatActivity {

    private ImageView mediaImageView;
    private int id;
    private TextView mediaTitleTextView, mediaDescriptionTextView, mediaDateTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media);

        mediaImageView = findViewById(R.id.mediaImageView);
        mediaTitleTextView = findViewById(R.id.mediaTitleTextView);
        mediaDescriptionTextView = findViewById(R.id.mediaDescriptionTextView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("MEDIA_ID", 0);
            String mediaTitle = extras.getString("MEDIA_TITLE", "");
            String mediaDescription = extras.getString("MEDIA_DESCRIPTION", "");
            String mediaImageUrl = extras.getString("MEDIA_IMAGE_URL", "");
            mediaTitleTextView.setText(mediaTitle);
            mediaDescriptionTextView.setText(mediaDescription);
            String filePath = mediaImageUrl;
            String mediaId = filePath.substring(filePath.lastIndexOf("/") + 1);

            // Construct content URI
            Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaId);

            Picasso.get().invalidate(contentUri);

// Load image using Picasso with error handling

            // Load image using Picasso with InputStream
            Picasso.get()
                    .load(contentUri)
                    .into(mediaImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("exception", e.getMessage());
                            mediaImageView.setImageResource(R.drawable.ic_launcher_background);
                        }
                    });
        }

    }
}
