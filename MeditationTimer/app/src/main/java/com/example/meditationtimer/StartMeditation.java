package com.example.meditationtimer;// StartMeditation.java

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class StartMeditation extends AppCompatActivity {

    private TextView timerDisplay;
    private RadioGroup durationRadioGroup;
    private Button startButton, cancelButton;

    private MeditationTimerModel meditationTimerModel;
    private AppDatabase appDatabase;

    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meditation);

        // Initialize Room Database
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Assuming you have a TextView to display the timer
        timerDisplay = findViewById(R.id.timerDisplay);
        durationRadioGroup = findViewById(R.id.durationRadioGroup);
        startButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Create an instance of MeditationTimerModel
        meditationTimerModel = new MeditationTimerModel(this);

        // Set up the timer tick listener
        meditationTimerModel.setOnTimerTickListener(new MeditationTimerModel.OnTimerTickListener() {
            @Override
            public void onTick(int remainingTimeInSeconds) {
                // Update the UI with the remaining time
                updateTimerDisplay(remainingTimeInSeconds);
            }

            @Override
            public void onFinish() {
                // Handle meditation session completion
                // For example, show a completion message or navigate to another screen
                timerDisplay.setText("Meditation Completed!");
                isTimerRunning = false; // Reset the timer running state
            }
        });

        // Set up click listener for the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    int selectedDurationId = durationRadioGroup.getCheckedRadioButtonId();

                    if (selectedDurationId == -1) {
                        // No duration selected, show a message or handle it as needed
                        Toast.makeText(StartMeditation.this, "Please select a meditation duration", Toast.LENGTH_SHORT).show();
                        return; // Do not proceed further
                    }

                    RadioButton selectedRadioButton = findViewById(selectedDurationId);

                    // Get the selected duration (assuming radio button text is in the format "X mins")
                    String selectedDurationText = selectedRadioButton.getText().toString();
                    int selectedDuration = Integer.parseInt(selectedDurationText.split(" ")[0]);

                    // Save the selected duration to Room Database
                    saveMeditationDurationToDatabase(selectedDuration);

                    // Start the meditation timer
                    meditationTimerModel.startMeditationTimer(selectedDuration);
                    isTimerRunning = true; // Set the timer running state
                } else {
                    Toast.makeText(StartMeditation.this, "The timer is already running", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel button click
                meditationTimerModel.stopMeditationTimer();
                isTimerRunning = false; // Reset the timer running state
                // Finish the activity
                finish();
            }
        });
    }

    // Update the UI with the remaining time
    private void updateTimerDisplay(int remainingTimeInSeconds) {
        int minutes = remainingTimeInSeconds / 60;
        int seconds = remainingTimeInSeconds % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        timerDisplay.setText(timeString);
    }

    private void saveMeditationDurationToDatabase(int duration) {
        // Use coroutine for database operations in a non-blocking way
        Executors.newSingleThreadExecutor().execute(() -> {
            ProgressEntry progressEntry = new ProgressEntry(System.currentTimeMillis(), duration);
            appDatabase.progressEntryDao().insert(progressEntry);
        });
    }
}
