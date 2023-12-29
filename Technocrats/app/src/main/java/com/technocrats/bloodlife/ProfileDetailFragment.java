package com.technocrats.bloodlife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.technocrats.bloodlife.R;

public class ProfileDetailFragment extends BaseFragment {

    private TextView nameTextView;
    private TextView bloodGroupTextView;
    private TextView locationTextView;
    // Add other views as needed

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        // Initialize views
        nameTextView = view.findViewById(R.id.nameTextView);
        bloodGroupTextView = view.findViewById(R.id.bloodGroupTextView);
        locationTextView = view.findViewById(R.id.locationTextView);
        // Initialize other views

        // Retrieve user details from arguments
        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            String bloodGroup = args.getString("bloodGroup");
            String location = args.getString("location");
            String number = args.getString("number");
            // Retrieve other details as needed

            // Set details to views
            nameTextView.setText("Name: "+name);
            bloodGroupTextView.setText("Blood Group: "+ bloodGroup);
            locationTextView.setText("Location: "+location+"\n" +"Number : "+ number);
        }

        return view;
    }
}
