package com.example.meditationtimer;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
// Inside your onCreate method
        createNotificationChannel();

// Set up the AlarmManager for daily notifications (e.g., in your onStart or when the app starts)
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
// Set the time for the first notification (e.g., 8:00 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Find buttons by their IDs
        CardView startMeditationButton = findViewById(R.id.startMeditationCardView);
        CardView viewProgressButton = findViewById(R.id.viewProgressCardView);
        CardView audioLibraryButton = findViewById(R.id.audioLibraryCardView);

        // Set click listeners for each button
        startMeditationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMeditation();
            }
        });

        viewProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProgress();
            }
        });

        audioLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAudioLibrary();
            }
        });
    }

    private void startMeditation() {
        // Intent to navigate to the Meditation Timer screen
        Intent intent = new Intent(this, StartMeditation.class);
        startActivity(intent);
    }

    private void viewProgress() {
        // Intent to navigate to the Progress Tracking screen
        Intent intent = new Intent(this, ProgressTracking.class);
        startActivity(intent);
    }

    private void openAudioLibrary() {
        // Intent to navigate to the Audio Library screen
        Intent intent = new Intent(this, Audio.class);
        startActivity(intent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MeditationChannel";
            String description = "Daily meditation reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("MeditationChannelId", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
