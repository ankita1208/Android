package com.technocrats.bloodlife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.technocrats.bloodlife.databinding.FragmentNavBinding;

import java.util.ArrayList;

public class NavFragment extends BaseFragment {
    final ArrayList<DrawerData> menuList = new ArrayList<>();
    FragmentNavBinding binding;
    DrawerAdapter navAdapter;
    MainActivity mainActivity;

    public NavFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavBinding.inflate(inflater, container, false);
        getMenuList();
        setUpAdapter();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    public ArrayList<DrawerData> getMenuList() {
        menuList.clear();
        menuList.add(new DrawerData("Home"));
        menuList.add(new DrawerData("Blood Request Management"));
        menuList.add(new DrawerData("Blood Availability"));
        menuList.add(new DrawerData("Appointment Scheduling"));
        menuList.add(new DrawerData("Blood Donation History"));
        menuList.add(new DrawerData("Educational Resources"));
        menuList.add(new DrawerData("Feedback Rating"));
        menuList.add(new DrawerData("Logout"));

        return menuList;
    }

    void setUpAdapter() {
        navAdapter = new DrawerAdapter(getActivity(), getMenuList(), (item, position) -> {
            if (item.getName().equalsIgnoreCase("Home")) {
                mainActivity.changeHomeFragment(new HomeFragment());
            } else if (item.getName().equalsIgnoreCase("Appointment Scheduling")) {
                mainActivity.changeHomeFragment(new AppointmentScheduleFragment());
            } else if (item.getName().equalsIgnoreCase("Blood Availability")) {
                mainActivity.changeHomeFragment(new BloodAvailability());
            } else if (item.getName().equalsIgnoreCase("Blood Request Management")) {
                mainActivity.changeHomeFragment(new BloodRequestManagement());
            } else if (item.getName().equalsIgnoreCase("Educational Resources")) {
                mainActivity.changeHomeFragment(new EducationalResourceFragment());
            } else if (item.getName().equalsIgnoreCase("Feedback Rating")) {
                mainActivity.changeHomeFragment(new RatingFragment());
            } else if (item.getName().equalsIgnoreCase("Blood Donation History")) {
                mainActivity.changeHomeFragment(new BloodDonationHistoryFragment());
            } else if (item.getName().equalsIgnoreCase("Logout")) {
                performLogout();
            }
        });
        binding.rvMenuItem.setAdapter(navAdapter);
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        navigateToLoginActivity(getActivity());
        Toast.makeText(getActivity(), "Logged out successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}