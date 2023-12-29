package com.technocrats.bloodlife;

import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.technocrats.bloodlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technocrats.bloodlife.databinding.DialogDonorListBinding;
import com.technocrats.bloodlife.databinding.FragmentAppointmentScheduleBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentScheduleFragment extends BaseFragment {
    FragmentAppointmentScheduleBinding binding;
    ScheduleAdapter scheduleAdapter;
    private Dialog customDialog;
    private FirebaseFirestore firestore;
    private String selectedDonorId;
    private List<DonorModel> donorList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppointmentScheduleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        firestore = FirebaseFirestore.getInstance();
        binding.calendarCV.setVisibility(View.GONE);
        setAdapter();
        handleClick();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) baseActivity ).setToolbarTitle("Appointment Scheduling");
    }

    private void setAdapter() {
        scheduleAdapter = new ScheduleAdapter();
        binding.recyclerRV.setAdapter(scheduleAdapter);
    }

    private void handleClick() {
        binding.btnSubmit.setOnClickListener(v -> {
            if (selectedDonorId != null) {
                saveAppointmentToFirestore(selectedDonorId);
                Toast.makeText(requireContext(), "Appointment booked successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Please select a blood donor", Toast.LENGTH_SHORT).show();
            }
        });
        binding.selectDonorButton.setOnClickListener(v -> showDonorSelectionDialog());

        binding.parentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
    }

    private void showDonorSelectionDialog() {
        firestore.collection("bloodLife")
                .whereEqualTo("isBloodDonor", true)
                .whereNotEqualTo("userId", getCurrentUserId()) // Exclude the current user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donorList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DonorModel donor = document.toObject(DonorModel.class);
                            donorList.add(donor);
                        }
                        displayDonorSelectionDialog(donorList);
                    } else {
                        Toast.makeText(requireContext(), "Error fetching blood donors", Toast.LENGTH_SHORT).show();
                        Log.e("AppointmentScheduleFragment", "Error fetching blood donors", task.getException());
                    }
                });
    }

    private String getCurrentUserId() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : "";
    }

    private void displayDonorSelectionDialog(List<DonorModel> donorList) {
        customDialog = new Dialog(requireActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogDonorListBinding dialogBinding = (DialogDonorListBinding) DataBindingUtil
                .inflate(LayoutInflater.from(getActivity())
                        , R.layout.dialog_donor_list, null, false);

        customDialog.setContentView(dialogBinding.getRoot());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        customDialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        customDialog.getWindow().setGravity(Gravity.CENTER);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DonorListAdapter donorListAdapter = new DonorListAdapter(donorList, new DonorListAdapter.OnDonorClickListener() {
            @Override
            public void onDonorClick(DonorModel donor) {
                selectedDonorId = donor.getUserId();
                customDialog.dismiss();
                binding.selectDonorButton.setVisibility(View.GONE);
                binding.calendarCV.setVisibility(View.VISIBLE);
                binding.recyclerRV.setVisibility(View.VISIBLE);
                binding.btnSubmit.setVisibility(View.VISIBLE);
                updateScheduleAdapterWithDonor(donor);
            }
        });

        dialogBinding.donorRecyclerView.setAdapter(donorListAdapter);
        dialogBinding.closeButton.setOnClickListener(view -> customDialog.dismiss());

        customDialog.show();
    }

    private void updateScheduleAdapterWithDonor(DonorModel donor) {
        scheduleAdapter.updateData(donor.getAvailabilityTime());
    }


    private void saveAppointmentToFirestore(String donorId) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // Create a new appointment document
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("userId", currentUser.getUid());
            appointmentData.put("donorId", donorId);
            // Add other appointment details as needed

            // Add the appointment to Firestore
            firestore.collection("appointments")
                    .add(appointmentData)
                    .addOnSuccessListener(documentReference -> {
                        replaceFragment(R.id.flMain, new HomeFragment());
                        Toast.makeText(requireContext(), "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
                        // Handle the error
                    });
        } else {
            // User is not authenticated, handle accordingly
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}