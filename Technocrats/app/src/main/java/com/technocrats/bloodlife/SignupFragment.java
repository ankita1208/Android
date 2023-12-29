package com.technocrats.bloodlife;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.technocrats.bloodlife.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technocrats.bloodlife.databinding.DialogBloodGroupBinding;
import com.technocrats.bloodlife.databinding.FragmentSignupBinding;

public class SignupFragment extends BaseFragment {
    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Dialog customDialog;

    // Define a constant for SharedPreferences
    private static final String USER_INFO_PREF = "user_info";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        FirebaseApp.initializeApp(getActivity());
        firebaseFirestore = FirebaseFirestore.getInstance();
        initUI();
        return view;
    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance();
        binding.signInBT.setOnClickListener(v -> {
            if (!checkForEmptyFields()) {
                signUp(binding.emailET.getText().toString().trim(), binding.passwordET.getText().toString(), binding.nameET.getText().toString().trim(), binding.locationET.getText().toString().trim(), binding.bloodTypeET.getText().toString().trim(), binding.phoneNumberET.getText().toString().trim());
            }
        });

        binding.bloodTypeET.setOnClickListener(v -> {
            openDialog();
        });

        binding.signupTV.setOnClickListener(v -> {
            replaceFragment(R.id.frameFL, new LoginFragment());
        });
    }

    private void signUp(String email, String password, String name, String location, String bloodType, String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        addDataToFirestore(name, email, location, bloodType, phoneNumber,userId);
                    } else {
                        Toast.makeText(getActivity(), "Sign-up failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDataToFirestore(String name, String email, String location, String bloodType, String phoneNumber, String userId) {
        CollectionReference dbCourses = firebaseFirestore.collection("bloodLife");
        ProfileData profileData = new ProfileData(name, location, phoneNumber, email, bloodType,userId);



        dbCourses.add(profileData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentId = documentReference.getId();
                // Save data to SharedPreferences
                saveUserInfoToSharedPreferences(profileData,documentId);
                Toast.makeText(getActivity(), "Signed up successfully!", Toast.LENGTH_SHORT).show();
                replaceFragment(R.id.frameFL, new LoginFragment());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to add user \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserInfoToSharedPreferences(ProfileData profileData, String documentId) {
        // Save the profile data to SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(USER_INFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", profileData.getName());
        editor.putString("email", profileData.getEmail());
        editor.putString("location", profileData.getLocation());
        editor.putString("phoneNumber", profileData.getPhoneNumber());
        editor.putString("bloodType", profileData.getBloodType());
        editor.putString("docId", documentId);

        editor.apply();
    }

    private boolean checkForEmptyFields() {
        if (binding.nameET.getText().toString().trim().isEmpty()) {
            showToast("Name cannot be empty.");
        } else if (binding.emailET.getText().toString().trim().isEmpty()) {
            showToast("Email cannot be empty.");
        } else if (!binding.emailET.getText().toString().trim().isEmpty() && !isValidEmail(binding.emailET.getText().toString().trim())) {
            showToast("Email address entered is not valid.");
        } else if (binding.phoneNumberET.getText().toString().trim().isEmpty()) {
            showToast("Phone number cannot be empty.");
        } else if (binding.bloodTypeET.getText().toString().trim().isEmpty()) {
            showToast("Please select Blood type");
        } else if (binding.locationET.getText().toString().isEmpty()) {
            showToast("Location cannot be empty.");
        } else if (binding.passwordET.getText().toString().trim().isEmpty()) {
            showToast("Password cannot be empty");
        } else if (binding.passwordET.getText().toString().length() < 8) {
            showToast("Password should not be less than 8 characters");
        } else if (!isValidPassword(binding.passwordET.getText().toString().trim())) {
            showToast("Password should be a combination of upper case, lower case and special character");
        } else if (binding.confirmPasswordET.getText().toString().trim().isEmpty()) {
            showToast("Confirm password cannot be empty.");
        } else if (!binding.confirmPasswordET.getText().toString().trim().isEmpty() && !binding.confirmPasswordET.getText().toString().trim().equals(binding.passwordET.getText().toString().trim())) {
            showToast("Password and confirm password should be the same.");
        } else {
            return false;
        }
        return true;
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
            binding.bloodTypeET.setText(dialogBinding.abNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });

        dialogBinding.abPositiveTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.abPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.bPositiveTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.bPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.bNegativeTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.bNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.aPositiveTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.aPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.aNegativeTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.abNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.oPositiveTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.oPositiveTV.getText().toString().trim());
            customDialog.dismiss();
        });
        dialogBinding.oNegativeTV.setOnClickListener(v -> {
            binding.bloodTypeET.setText(dialogBinding.oNegativeTV.getText().toString().trim());
            customDialog.dismiss();
        });

        customDialog.show();
    }
}
