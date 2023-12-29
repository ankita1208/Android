package com.technocrats.bloodlife;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.technocrats.bloodlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technocrats.bloodlife.databinding.DialogBloodGroupBinding;
import com.technocrats.bloodlife.databinding.DialogUrgencyLevelBinding;
import com.technocrats.bloodlife.databinding.FragmentBloodRequestManagementBinding;

import java.util.HashMap;
import java.util.Map;

public class BloodRequestManagement extends BaseFragment {

    private FragmentBloodRequestManagementBinding binding;
    private Dialog customDialog;
    private Dialog urgencyDialog;
    private String selectedUrgencyLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBloodRequestManagementBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set user information from SharedPreferences
        setUserInformationFromSharedPreferences();

        handleClick();
        return view;
    }

    private void handleClick() {
        binding.requiredTypeET.setOnClickListener(v -> openDialog());
        binding.urgencyLevelET.setOnClickListener(v -> openUrgencyLevelDialog());
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the UI
                String requesterName = binding.bloodRequesterNameET.getText().toString();
                String requiredType = binding.requiredTypeET.getText().toString();
                String phoneNumber = binding.phoneNumberET.getText().toString();
                String email = binding.emailET.getText().toString();
                String location = binding.locationET.getText().toString();

                // Create a BloodRequest object
                BloodRequest bloodRequest = new BloodRequest(requesterName, requiredType, selectedUrgencyLevel, phoneNumber, email, location, "true");

                // Store the request in Firestore
                addBloodRequestToFirestore(bloodRequest);

                // Navigate back to HomeFragment
                replaceFragment(R.id.flMain, new HomeFragment());
            }
        });
    }

    private void openUrgencyLevelDialog() {
        urgencyDialog = new Dialog(requireActivity());
        urgencyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            DialogUrgencyLevelBinding dialogBinding = (DialogUrgencyLevelBinding) DataBindingUtil
                    .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_urgency_level, null, false);

        urgencyDialog.setContentView(dialogBinding.getRoot());

            // Set up dialog layout and appearance
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
        urgencyDialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        urgencyDialog.getWindow().setGravity(Gravity.CENTER);
        urgencyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set up click listeners for radio buttons
            dialogBinding.lowRadioButton.setOnClickListener(v -> {
                selectedUrgencyLevel = "Low";
                binding.urgencyLevelET.setText(selectedUrgencyLevel);
                urgencyDialog.dismiss();
            });
            dialogBinding.moderateRadioButton.setOnClickListener(v -> {selectedUrgencyLevel = "Moderate";
                binding.urgencyLevelET.setText(selectedUrgencyLevel);
                urgencyDialog.dismiss();
            });
            dialogBinding.highRadioButton.setOnClickListener(v -> {
                selectedUrgencyLevel = "High";
                binding.urgencyLevelET.setText(selectedUrgencyLevel);
                urgencyDialog.dismiss();

            });

        urgencyDialog.show();
    }

    private void addBloodRequestToFirestore(BloodRequest bloodRequest) {
        // Get the currently signed-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            // User is signed in, you can get the user's information
            String uid = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            String userName = sharedPreferences.getString("name", "");;

            // Now, you can use this information when adding the blood request to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference bloodRequestsRef = db.collection("bloodRequests");

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("uid", uid); // Optionally, if you want to associate the blood request with the user's ID
            requestMap.put("userId", uid); // Optionally, if you want to associate the blood request with the user's ID
            requestMap.put("requesterName", bloodRequest.getRequesterName());
            requestMap.put("requiredType", bloodRequest.getRequiredType());
            requestMap.put("urgencyLevel", bloodRequest.getUrgencyLevel());
            requestMap.put("phoneNumber", bloodRequest.getPhoneNumber());
            requestMap.put("email", bloodRequest.getEmail());
            requestMap.put("location", bloodRequest.getLocation());
            requestMap.put("userName", userName); // Store the user's name
            requestMap.put("userEmail", userEmail); // Store the user's email
            requestMap.put("isRequester", bloodRequest.getIsRequester());

            bloodRequestsRef.add(requestMap)
                    .addOnSuccessListener(documentReference -> {
                        showToast("Blood request added successfully");
                    })
                    .addOnFailureListener(e -> {
                        showToast("Error adding blood request");
                        Log.e("BRM", "Error adding blood request", e);
                    });
        } else {
            // User is not signed in, handle accordingly
            showToast("User not signed in");
        }
    }

    private void setUserInformationFromSharedPreferences() {
        // Get user information from SharedPreferences saved during sign-up
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString("name", "");
        String userLocation = sharedPreferences.getString("location", "");
        String userPhoneNumber = sharedPreferences.getString("phoneNumber", "");
        String userEmail = sharedPreferences.getString("email", "");

        // Set the user information to the UI
        binding.bloodRequesterNameET.setText(userName);
        binding.emailET.setText(userEmail);
        binding.locationET.setText(userLocation);
        binding.phoneNumberET.setText(userPhoneNumber);
    }


    private void openDialog() {
        customDialog = new Dialog(requireActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogBloodGroupBinding dialogBinding = (DialogBloodGroupBinding) DataBindingUtil
                .inflate(LayoutInflater.from(getActivity())
                        , R.layout.dialog_blood_group, null, false);

        customDialog.setContentView(dialogBinding.getRoot());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        customDialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        customDialog.getWindow().setGravity(Gravity.CENTER);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogBinding.abNegativeTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.abNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });

        dialogBinding.abPositiveTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.abPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.bPositiveTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.bPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.bNegativeTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.bNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.aPositiveTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.aPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.aNegativeTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.abNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.oPositiveTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.oPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.oNegativeTV.setOnClickListener(v -> {
            binding.requiredTypeET.setText(dialogBinding.oNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });

        customDialog.show();
    }
}
