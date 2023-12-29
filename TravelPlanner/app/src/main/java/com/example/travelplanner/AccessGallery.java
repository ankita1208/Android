package com.example.travelplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AccessGallery extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    private RecyclerView photoRecyclerView;
    private MediaAdapter mediaAdapter;
    private AppDatabase mediaDatabase;

    private Button viewMediaBtn, editMediaDetailsBtn, addMediaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_gallery);

        photoRecyclerView = findViewById(R.id.photoRecyclerView);

        mediaAdapter = new MediaAdapter(new ArrayList<>(), this);

        viewMediaBtn = findViewById(R.id.viewMediaBtn);
        editMediaDetailsBtn = findViewById(R.id.editMediaDetailsBtn);
        addMediaBtn = findViewById(R.id.deleteMediaBtn);
        mediaDatabase = AppDatabase.getInstance(this);

        // Check for runtime permissions
        if (checkReadExternalStoragePermission()) {
            // Permission already granted, proceed with loading media
            loadMedia();
        }

        viewMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccessGallery.this, ViewMedia.class));
            }
        });

        editMediaDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccessGallery.this, EditMediaDetails.class));
            }
        });

        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccessGallery.this, AddMedia.class));
            }
        });

        // Observe changes in the media list
        mediaDatabase.mediaDao().getAllMedia().observe(this, new Observer<List<MediaEntity>>() {
            @Override
            public void onChanged(List<MediaEntity> mediaEntities) {
                photoRecyclerView.setAdapter(mediaAdapter);
                // Update the adapter with the new media list
                mediaAdapter.setMediaList(mediaEntities);
            }
        });
    }

    private boolean checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE);
                return false;
            } else {
                // Permission already granted
                return true;
            }
        } else {
            // Runtime permissions are not required for versions below Android 6.0
            return true;
        }
    }

    private void loadMedia() {
       mediaDatabase.mediaDao().getAllMedia();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading media
                loadMedia();
            } else {
                // Permission denied
                // Handle accordingly (e.g., show a message or disable functionality)
            }
        }
    }
}
