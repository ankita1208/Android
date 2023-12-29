package com.technocrats.bloodlife;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technocrats.bloodlife.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private HomeAdapter homeAdapter;
    private List<BloodRequest> bloodRequests;
    private List<BloodRequest> originalData;
    private List<BloodRequest> filteredData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recyclerRVd;
        bloodRequests = new ArrayList<>();
        originalData = new ArrayList<>();
        filteredData = new ArrayList<>();

        // Fetch blood requests from Firestore
        fetchBloodRequests();

        // Setup the search EditText
        binding.searchET.addTextChangedListener(new SearchTextWatcher());

        return view;
    }

    private void fetchBloodRequests() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bloodRequestsRef = db.collection("bloodRequests");

        bloodRequestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                originalData.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    BloodRequest bloodRequest = document.toObject(BloodRequest.class);
                    if (currentUser != null && !currentUser.getUid().equals(bloodRequest.getUserId())) {
                        originalData.add(bloodRequest);
                    }
                }
                fetchBloodLifeData();
            } else {
                Log.e("HomeFragment", "Error fetching blood request", task.getException());
            }
        });
    }

    private void fetchBloodLifeData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bloodLifeRef = db.collection("bloodLife");

        bloodLifeRef.whereEqualTo("isBloodDonor", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BloodRequest bloodLifeData = document.toObject(BloodRequest.class);
                            if (currentUser != null && !currentUser.getUid().equals(bloodLifeData.getUserId())) {
                                originalData.add(bloodLifeData);
                            }
                        }
                        // After fetching bloodLifeData, filter data based on search query
                        filterData(binding.searchET.getText().toString());
                    } else {
                        Log.e("HomeFragment", "Error fetching blood life data", task.getException());
                    }
                });
    }

    private void filterData(String query) {
        filteredData.clear();
        if (query.isEmpty()) {
            filteredData.addAll(originalData);
        } else {
            for (BloodRequest request : originalData) {
                if (request.getRequiredType().toLowerCase().contains(query.toLowerCase())
                        || request.getRequesterName().toLowerCase().contains(query.toLowerCase())
                        || request.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    filteredData.add(request);
                }
            }
        }
        updateAdapter();
    }

    private void updateAdapter() {
        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(filteredData, this);
            binding.recyclerRVd.setAdapter(homeAdapter);
        } else {
            homeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(BloodRequest request) {
        openProfileDetailFragment(request);
    }

    private void openProfileDetailFragment(BloodRequest request) {
        ProfileDetailFragment profileDetailFragment = new ProfileDetailFragment();

        // Pass user details to the new fragment
        Bundle args = new Bundle();
        args.putString("name", request.getRequesterName());
        args.putString("bloodGroup", request.getRequiredType());
        args.putString("location", request.getLocation());
        args.putString("number", request.getPhoneNumber());
        // Pass other details as needed
        profileDetailFragment.setArguments(args);

        // Open the new fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain, profileDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            filterData(editable.toString());
        }
    }
}
