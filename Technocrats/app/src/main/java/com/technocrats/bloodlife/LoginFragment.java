package com.technocrats.bloodlife;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technocrats.bloodlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technocrats.bloodlife.databinding.FragmentLoginBinding;

public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initUI();
        return view;
    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getActivity(), "Already logged in.", Toast.LENGTH_SHORT).show();
            navigateToMainActivity(getActivity());
            return; // Exit the method to avoid showing the login screen again
        }

        binding.loginBT.setOnClickListener(v -> {
            if (!checkForEmptyFields()) {
                String email = binding.emailET.getText().toString().trim();
                String password = binding.passwordET.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getActivity(), "Authentication Successful.", Toast.LENGTH_SHORT).show();

                                // Save user information to SharedPreferences
                                fetchAndSaveUserInfo(user);

                                navigateToMainActivity(getActivity());
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.signupTV.setOnClickListener(v -> {
            replaceFragment(R.id.frameFL, new SignupFragment());
        });
    }


    private void fetchAndSaveUserInfo(FirebaseUser user) {
        if (user != null) {
            firestore.collection("bloodLife")
                    .whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the document ID
                                String documentId = document.getId();

                                // Map the data to your ProfileData class
                                ProfileData profileData = document.toObject(ProfileData.class);

                                // Save user information and document ID to SharedPreferences
                                saveUserInformationToSharedPreferences(profileData, documentId);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error fetching user information.", Toast.LENGTH_SHORT).show();
                            Log.e("LoginFragment", "Error fetching user information", task.getException());
                        }
                    });
        }
    }


    private boolean checkForEmptyFields() {
        if (binding.emailET.getText().toString().trim().isEmpty()) {
            showToast("Email cannot be empty");
        } else if (binding.passwordET.getText().toString().trim().isEmpty()) {
            showToast("Password cannot be empty");
        } else {
            return false;
        }
        return true;
    }

    private void saveUserInformationToSharedPreferences(ProfileData profileData, String documentId) {
        if (profileData != null) {
            SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", profileData.getName());
            editor.putString("email", profileData.getEmail());
            editor.putString("location", profileData.getLocation());
            editor.putString("phoneNumber", profileData.getPhoneNumber());
            editor.putString("bloodType", profileData.getBloodType());
            editor.apply();
        }
    }
}