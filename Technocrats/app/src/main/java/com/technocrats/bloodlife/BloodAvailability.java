package com.technocrats.bloodlife;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technocrats.bloodlife.databinding.FragmentNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class BloodAvailability extends BaseFragment {
    FragmentNotificationBinding binding;
    BloodAvailaibilityAdapter bloodAvailabilityAdapter;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initUI();
        setAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) baseActivity).setToolbarTitle("Blood Availability");
        fetchBloodAvailabilityData();
    }

    private void fetchBloodAvailabilityData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // Query to fetch documents where isBloodDonor is true
            firestore.collection("bloodLife")
                    .whereEqualTo("isBloodDonor", true)
                    .get()
                    .addOnCompleteListener(fetchTask -> {
                        if (fetchTask.isSuccessful()) {
                            List<BloodAvailabilityData> bloodAvailabilityDataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : fetchTask.getResult()) {
                                // Convert Firestore document to BloodAvailabilityData
                                BloodAvailabilityData availabilityData = document.toObject(BloodAvailabilityData.class);
                                bloodAvailabilityDataList.add(availabilityData);
                            }

                            // Update the adapter with the fetched data
                            bloodAvailabilityAdapter.setData(bloodAvailabilityDataList);
                        } else {
                            Log.e("BloodAvailability", "Error fetching availability", fetchTask.getException());
                        }
                    });
        } else {
            Log.e("BloodAvailability", "Current user null");
        }
    }

    private void setAdapter() {
        bloodAvailabilityAdapter = new BloodAvailaibilityAdapter(new ArrayList<>());
        binding.recyclerRVd.setAdapter(bloodAvailabilityAdapter);
    }

    private void initUI() {
        binding.recyclerRVd.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
