package com.technocrats.bloodlife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technocrats.bloodlife.databinding.FragmentRatingBinding;

import java.util.HashMap;
import java.util.Map;

public class RatingFragment extends BaseFragment {
    FragmentRatingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        handleClick();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) baseActivity).setToolbarTitle("Feedback Rating");
    }

    private void handleClick() {
        binding.button2.setOnClickListener(v -> {
            // Get user feedback and rating
            String feedback = binding.tlFeedback.getText().toString();
            float rating = binding.rating.getRating();

            // Send feedback to Firestore
            sendFeedbackToFirestore(feedback, rating);
        });
    }

    private void sendFeedbackToFirestore(String feedback, float rating) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Create a new feedback document
            Map<String, Object> feedbackData = new HashMap<>();
            feedbackData.put("userId", currentUser.getUid());
            feedbackData.put("feedback", feedback);
            feedbackData.put("rating", rating);

            // Add the feedback to Firestore
            firestore.collection("feedback")
                    .add(feedbackData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(requireContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                        // Clear input fields or perform other actions if needed
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to send feedback", Toast.LENGTH_SHORT).show();
                        // Handle the error
                    });
        } else {
            // User is not authenticated, handle accordingly
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
