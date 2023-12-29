package com.technocrats.bloodlife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technocrats.bloodlife.databinding.FragmentNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class EducationalResourceFragment extends BaseFragment {
    FragmentNotificationBinding binding;
    EducationalResourceAdapter educationalResourceAdapter;

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
        ((MainActivity) baseActivity ).setToolbarTitle("Educational Resources");
    }

    private void setAdapter() {
        List<EducationalResourceData> mockData = createMockData();
        educationalResourceAdapter = new EducationalResourceAdapter(mockData, getActivity());
        binding.recyclerRVd.setAdapter(educationalResourceAdapter);
    }

    private List<EducationalResourceData> createMockData() {
        List<EducationalResourceData> mockData = new ArrayList<>();
        mockData.add(new EducationalResourceData("Blood Donation: A Guide for Donors",
                "Learn about the blood donation process and what to expect when you donate blood.",
                "https://www.redcrossblood.org/donate-blood/how-to-donate/eligibility-requirements.html"));
        mockData.add(new EducationalResourceData("Blood Types and Compatibility",
                "Understand the different blood types and how they are compatible for blood transfusions.",
                "https://www.healthline.com/health/blood-types"));
        mockData.add(new EducationalResourceData("Benefits of Blood Donation",
                "Discover the health benefits of donating blood and how it can save lives.",
                "https://www.mayoclinic.org/tests-procedures/blood-donation/about/pac-20385144"));

        return mockData;
    }

    private void initUI() {
        binding.recyclerRVd.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
