package com.technocrats.bloodlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.List;

public class BloodAvailaibilityAdapter extends RecyclerView.Adapter<BloodAvailaibilityAdapter.ViewHolder> {

    private List<BloodAvailabilityData> data;

    public BloodAvailaibilityAdapter(List<BloodAvailabilityData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_blood_availability, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodAvailabilityData availabilityData = data.get(position);
        holder.bind(availabilityData);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<BloodAvailabilityData> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bloodTypeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodTypeTextView = itemView.findViewById(R.id.textTV);
        }

        public void bind(BloodAvailabilityData availabilityData) {
            bloodTypeTextView.setText("Blood Type : " + availabilityData.getBloodType() + "\n" + "Available Units : " + String.valueOf(availabilityData.getBloodAvailability()) + "\n" + "Donor : " + availabilityData.getName());
        }
    }
}
