package com.technocrats.bloodlife;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.technocrats.bloodlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends BaseFragment {

    private TextView nameTextView, contactInfoTextView, bloodTypeTextView, availabilityTextView;
    private Switch bloodDonorSwitch;
    private boolean isBloodDonor;
    private EditText bloodAvailabilityEditText, availabilityDateTimeEditText;
    private Button saveButton;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    // SharedPreferences
    private static final String USER_INFO_PREF = "user_info";

    // Calendar for storing selected date and time
    private Calendar selectedDateTime = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        nameTextView = view.findViewById(R.id.nameTextView);
        contactInfoTextView = view.findViewById(R.id.contactInfoTextView);
        bloodTypeTextView = view.findViewById(R.id.bloodTypeTextView);
        bloodDonorSwitch = view.findViewById(R.id.bloodDonorSwitch);
        availabilityTextView = view.findViewById(R.id.availabilityTextView);
        bloodAvailabilityEditText = view.findViewById(R.id.bloodAvailabilityET);
        availabilityDateTimeEditText = view.findViewById(R.id.availabilityTimeET);
        saveButton = view.findViewById(R.id.saveButton);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            fetchBloodAvailabilityFromFirestore();
        }
        // Fetch user data from SharedPreferences
        fetchUserDataFromSharedPreferences();

        // Set listener for the blood donor switch
        bloodDonorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // User has indicated willingness to be a blood donor
                // Show blood availability UI elements
                availabilityTextView.setVisibility(View.VISIBLE);
                bloodAvailabilityEditText.setVisibility(View.VISIBLE);
                availabilityDateTimeEditText.setVisibility(View.VISIBLE);  // Show availability date/time field
                saveButton.setVisibility(View.VISIBLE);
            } else {
                // User has opted out of being a blood donor
                // Hide blood availability UI elements
                availabilityTextView.setVisibility(View.GONE);
                bloodAvailabilityEditText.setVisibility(View.GONE);
                availabilityDateTimeEditText.setVisibility(View.GONE);  // Hide availability date/time field
                saveButton.setVisibility(View.GONE);
            }
        });

        // Set listener for the availability date/time EditText
        availabilityDateTimeEditText.setOnClickListener(v -> showDateTimePicker());

        // Set listener for the save button
        saveButton.setOnClickListener(v -> saveBloodAvailability());
    }

    private void fetchUserDataFromSharedPreferences() {
        // Get user information from SharedPreferences saved during sign-up
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(USER_INFO_PREF, Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString("name", "");
        String userPhoneNumber = sharedPreferences.getString("phoneNumber", "");
        String bloodAvailability = sharedPreferences.getString("bloodAvailability", "");
        long availabilityTimeInMillis = sharedPreferences.getLong("availabilityTime", 0);

        // Use the retrieved data as needed
        nameTextView.setText("Name: " + userName);
        contactInfoTextView.setText("Contact Info: " + userPhoneNumber);
        bloodTypeTextView.setText("Blood Type: " + sharedPreferences.getString("bloodType", ""));

        // Update UI based on the initial state of the blood donor switch
        if (isBloodDonor) {
            availabilityTextView.setVisibility(View.VISIBLE);
            bloodAvailabilityEditText.setVisibility(View.VISIBLE);
            availabilityDateTimeEditText.setVisibility(View.VISIBLE);  // Show availability date/time field
            saveButton.setVisibility(View.VISIBLE);

            // Set values for availability and availability date/time if not null in SharedPreferences
            if (bloodAvailability != null && !bloodAvailability.isEmpty()) {
                bloodAvailabilityEditText.setText(bloodAvailability);
            }
            if (availabilityTimeInMillis != 0) {
                selectedDateTime.setTimeInMillis(availabilityTimeInMillis);
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                availabilityDateTimeEditText.setText(dateTimeFormat.format(selectedDateTime.getTime()));
            }

            // Fetch additional data from Firestore if not null

        } else {
            availabilityTextView.setVisibility(View.GONE);
            bloodAvailabilityEditText.setVisibility(View.GONE);
            availabilityDateTimeEditText.setVisibility(View.GONE);  // Hide availability date/time field
            saveButton.setVisibility(View.GONE);
        }
    }

    private void fetchBloodAvailabilityFromFirestore() {
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(USER_INFO_PREF, Context.MODE_PRIVATE);
        String documentId = sharedPreferences.getString("docId", "");
        // Fetch additional data from Firestore
        DocumentReference userRef = firestore.collection("bloodLife").document(documentId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Check if the fields are not null in Firestore
                    if (document.contains("isBloodDonor")) {
                        boolean firestoreIsBloodDonor = document.getBoolean("isBloodDonor");

                        // Update switch based on the value from Firestore
                        if (firestoreIsBloodDonor != bloodDonorSwitch.isChecked()) {
                            bloodDonorSwitch.setChecked(firestoreIsBloodDonor);
                            isBloodDonor = firestoreIsBloodDonor;
                        }
                    }

                    if (document.contains("bloodAvailability")) {
                        String firestoreBloodAvailability = document.getString("bloodAvailability");
                        // Set the value in the EditText if not null
                        if (firestoreBloodAvailability != null && !firestoreBloodAvailability.isEmpty()) {
                            bloodAvailabilityEditText.setText(firestoreBloodAvailability);
                        }
                    }

                    if (document.contains("availabilityTime")) {
                        long firestoreAvailabilityTimeInMillis = document.getLong("availabilityTime");

                        // Set the value in the EditText if not null
                        if (firestoreAvailabilityTimeInMillis != 0) {
                            selectedDateTime.setTimeInMillis(firestoreAvailabilityTimeInMillis);
                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            availabilityDateTimeEditText.setText(dateTimeFormat.format(selectedDateTime.getTime()));
                        }
                    }
                }
            } else {
                Log.e("ProfileFragment", "Error fetching blood availability from Firestore", task.getException());
            }
        });
    }

    private void saveBloodAvailability() {
        String bloodAvailability = bloodAvailabilityEditText.getText().toString().trim();
        String availabilityDateTime = availabilityDateTimeEditText.getText().toString().trim();
        if (bloodAvailability.isEmpty()) {
            bloodAvailabilityEditText.setError("Blood availability cannot be empty");
            return;
        }

        // Validate the availability date/time field
        if (availabilityDateTime.isEmpty()) {
            availabilityDateTimeEditText.setError("Availability date/time cannot be empty");
            return;
        }
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(USER_INFO_PREF, Context.MODE_PRIVATE);
        String documentId = sharedPreferences.getString("docId", "");
        boolean isBloodDonor = bloodDonorSwitch.isChecked();  // Get the current state of the switch

        // Update the "bloodLife" collection in Firestore with the new blood availability, isBloodDonor, and availabilityDateTime
        if (currentUser != null) {
            DocumentReference userRef = firestore.collection("bloodLife").document(documentId);

            // Use the user ID as the document ID for the update
            userRef.update(
                            "bloodAvailability", bloodAvailability,
                            "isBloodDonor", isBloodDonor,
                            "availabilityTime", selectedDateTime.getTimeInMillis()  // Save time as milliseconds
                    )
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Blood availability and profile details updated", Toast.LENGTH_SHORT).show();
                        replaceFragment(R.id.flMain, new HomeFragment());
                    })
                    .addOnFailureListener(e -> Log.e("ProfileFragment", "Error updating blood availability", e));
        }
    }

    private void showDateTimePicker() {
        // Get the current date and time
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Set the selected date to the Calendar object
                    selectedDateTime.set(Calendar.YEAR, selectedYear);
                    selectedDateTime.set(Calendar.MONTH, selectedMonth);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);

                    // Create a TimePickerDialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (view1, hourOfDay, minuteOfDay) -> {
                                // Set the selected time to the Calendar object
                                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDateTime.set(Calendar.MINUTE, minuteOfDay);

                                // Update the text in the EditText
                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                availabilityDateTimeEditText.setText(dateTimeFormat.format(selectedDateTime.getTime()));
                            },
                            hour, minute, false
                    );

                    // Show the TimePickerDialog
                    timePickerDialog.show();
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
