package com.example.meditationtimer;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Audio extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner audioSpinner; // New spinner for audio tracks
    private RecyclerView recyclerView;
    private Button playButton;
    private Button downloadButton;

    private List<AudioItem> audioItems;
    private Button pauseButton;
    private MediaPlayer mediaPlayer;
    private String selectedAudioUrl;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        categorySpinner = findViewById(R.id.categorySpinner);
        audioSpinner = findViewById(R.id.audioSpinner); // Initialize the new spinner
        recyclerView = findViewById(R.id.recyclerView);
        playButton = findViewById(R.id.playButton);
        downloadButton = findViewById(R.id.downloadButton);

        setupCategorySpinner();
        setupAudioSpinnerListener(); // Set up listener for audio spinner
        audioItems = getSampleAudioItems();
        setupRecyclerView();
        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(v -> pauseAudio());
        playButton.setOnClickListener(v -> playSelectedAudio());
        downloadButton.setOnClickListener(v -> downloadSelectedAudio());

        checkStoragePermission();
        initializeAudioSpinner(); // Initialize the audio spinner
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pauseButton.setText("Resume"); // Change button text to "Resume" when paused
            Toast.makeText(this, "Audio paused", Toast.LENGTH_SHORT).show();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                pauseButton.setText("Pause"); // Change button text back to "Pause" when resumed
                Toast.makeText(this, "Audio resumed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCategorySpinner() {
        String[] categoryOptions = {"Guided Meditations", "Background Sounds", "Meditation Resources"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = (String) parentView.getItemAtPosition(position);
                Toast.makeText(Audio.this, "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
                updateAudioContent(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void setupAudioSpinnerListener() {
        audioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedAudioTitle = (String) parentView.getItemAtPosition(position);
                Toast.makeText(Audio.this, "Selected Audio: " + selectedAudioTitle, Toast.LENGTH_SHORT).show();

                // Find the selected audio item based on the title
                for (AudioItem audioItem : audioItems) {
                    if (audioItem.getTitle().equals(selectedAudioTitle)) {
                        selectedAudioUrl = audioItem.getAudioUrl();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void initializeAudioSpinner() {
        audioSpinner = findViewById(R.id.audioSpinner);
        setupAudioSpinnerListener();
    }

    private void setupRecyclerView() {
        List<AudioItem> audioItems = getSampleAudioItems();
        AudioAdapter audioAdapter = new AudioAdapter(audioItems);
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AudioItem audioItem) {
                selectedAudioUrl = audioItem.getAudioUrl();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(audioAdapter);
    }

    private List<AudioItem> getSampleAudioItems() {
        List<AudioItem> audioItems = new ArrayList<>();
        audioItems.add(new AudioItem("Guided Meditation 1", "21:28", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/affectionatebreathing.mp3"));
        audioItems.add(new AudioItem("Guided Meditation 2", "23:55", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/bodyscan.MP3"));
        audioItems.add(new AudioItem("Background Sound 1", "23:53", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/LKM.MP3"));
        audioItems.add(new AudioItem("Background Sound 2", "20:10", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/LKM.self-compassion.MP3"));
        audioItems.add(new AudioItem("Meditation Resource 1", "20:20", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/noting.practice.MP3"));
        audioItems.add(new AudioItem("Meditation Resource 2", "16:01", R.drawable.video_player, "https://self-compassion.org/wptest/wp-content/uploads/soften,soothe,allow.MP3"));
        return audioItems;
    }

    private void updateAudioContent(String selectedCategory) {
        List<AudioItem> updatedAudioItems = fetchDataForCategory(selectedCategory);

        AudioAdapter audioAdapter = (AudioAdapter) recyclerView.getAdapter();
        if (audioAdapter != null) {
            audioAdapter.setAudioItems(updatedAudioItems);
            audioAdapter.notifyDataSetChanged();
        }

        // Set up the audio spinner with the updated audio items
        setupAudioSpinner(updatedAudioItems);
    }


    private void setupAudioSpinner(List<AudioItem> audioItems) {
        List<String> audioTitles = new ArrayList<>();

        // Extract audio titles from the audio items
        for (AudioItem audioItem : audioItems) {
            audioTitles.add(audioItem.getTitle());
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> audioAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, audioTitles);

        // Specify the layout to use when the list of choices appears
        audioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        audioSpinner.setAdapter(audioAdapter);
    }


    private List<AudioItem> fetchDataForCategory(String selectedCategory) {
        List<AudioItem> fetchedAudioItems = new ArrayList<>();

        if ("Guided Meditations".equals(selectedCategory)) {
            fetchedAudioItems.add(new AudioItem("Guided Meditation 1", "21:28", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/affectionatebreathing.mp3"));
            fetchedAudioItems.add(new AudioItem("Guided Meditation 2", "23:55", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/bodyscan.MP3"));
        } else if ("Background Sounds".equals(selectedCategory)) {
            fetchedAudioItems.add(new AudioItem("Background Sound 1", "23:53", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/LKM.MP3"));
            fetchedAudioItems.add(new AudioItem("Background Sound 2", "20:10", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/LKM.self-compassion.MP3"));
        } else if ("Meditation Resources".equals(selectedCategory)) {
            fetchedAudioItems.add(new AudioItem("Meditation Resource 1", "20:20", R.drawable.video_player, "https://self-compassion.org/wp-content/uploads/meditations/noting.practice.MP3"));
            fetchedAudioItems.add(new AudioItem("Meditation Resource 2", "16:01", R.drawable.video_player, "https://self-compassion.org/wptest/wp-content/uploads/soften,soothe,allow.MP3"));
        }

        return fetchedAudioItems;
    }

    private void playSelectedAudio() {
        if (selectedAudioUrl != null && !selectedAudioUrl.isEmpty()) {
            checkStoragePermissionForPlayback();
        } else {
            Toast.makeText(this, "No audio selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadSelectedAudio() {
        if (selectedAudioUrl != null && !selectedAudioUrl.isEmpty()) {
            checkStoragePermissionForDownload();
        } else {
            Toast.makeText(this, "No audio selected for download", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    private void checkStoragePermissionForPlayback() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            playAudio();
        }
    }

    private void checkStoragePermissionForDownload() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            downloadAudio();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (selectedAudioUrl != null && !selectedAudioUrl.isEmpty()) {
                    if (mediaPlayer != null) {
                        playAudio();
                    } else {
                        downloadAudio();
                    }
                }
            } else {
                Toast.makeText(this, "Storage permission denied. Some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playAudio() {
        stopPlaying();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        try {
            mediaPlayer.setDataSource(selectedAudioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAudio() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(selectedAudioUrl));
        request.setTitle("Audio Download");
        request.setDescription("Downloading audio file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "audio_file.mp3");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(this, "Downloading selected audio", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaying();
    }
}