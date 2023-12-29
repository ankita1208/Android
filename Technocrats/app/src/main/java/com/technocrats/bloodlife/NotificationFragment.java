package com.technocrats.bloodlife;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technocrats.bloodlife.databinding.FragmentNotificationBinding;

public class NotificationFragment extends BaseFragment {
FragmentNotificationBinding binding;
NotificationAdapter notificationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initUI();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        notificationAdapter = new NotificationAdapter();
        binding.recyclerRVd.setAdapter(notificationAdapter);

    }

    private void initUI() {

    }
}