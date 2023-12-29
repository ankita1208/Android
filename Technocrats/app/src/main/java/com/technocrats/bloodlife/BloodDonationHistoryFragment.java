package com.technocrats.bloodlife;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technocrats.bloodlife.databinding.FragmentBloodDonationHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BloodDonationHistoryFragment extends BaseFragment {
    private FragmentBloodDonationHistoryBinding binding;
    private BloodDonationHistoryAdapter historyAdapter;
    private List<BloodDonationHistoryData> historyDataList;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBloodDonationHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initUI();
        loadAppointments();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) baseActivity).setToolbarTitle("Blood Donation History");
    }

    private void initUI() {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.recyclerRVd.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyDataList = new ArrayList<>();
    }

    private void loadAppointments() {
        SharedPreferences sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String documentId = sharedPreferences.getString("docId", "");

        if (currentUser != null) {
            firestore.collection("appointments")
                    .whereEqualTo("donorId", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            historyDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Appointment appointment = document.toObject(Appointment.class);
                                BloodDonationHistoryData historyData = new BloodDonationHistoryData();
                                historyData.setAppointment(appointment);
                                historyData.setDonorId(currentUser.getUid());
                                // Load donor name from user id
                                loadDonorName(appointment.getUserId(), historyData);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error fetching appointments", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadDonorName(String userId, BloodDonationHistoryData historyData) {
        firestore.collection("bloodLife")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get information from each matching document
                            String donorName = document.getString("name");
                            // Continue to extract other fields as needed
                            // ...

                            // Set the donorName and other fields in historyData
                            historyData.setDonorName(donorName);

                            historyDataList.add(historyData);
                        }

                        // Update the RecyclerView adapter after all documents are processed
                        historyAdapter = new BloodDonationHistoryAdapter(historyDataList);
                        binding.recyclerRVd.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                        Toast.makeText(requireContext(), "Error fetching donor information", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
