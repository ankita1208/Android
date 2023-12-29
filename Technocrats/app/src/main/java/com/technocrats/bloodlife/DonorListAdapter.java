package com.technocrats.bloodlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.List;

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.ViewHolder> {
    private List<DonorModel> donorList;
    private OnDonorClickListener listener;

    public DonorListAdapter(List<DonorModel> donorList, OnDonorClickListener listener) {
        this.donorList = donorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_donor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonorModel donor = donorList.get(position);
        holder.donorTextView.setText(donor.getName());
        holder.itemView.setOnClickListener(v -> listener.onDonorClick(donor));
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView donorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            donorTextView = itemView.findViewById(R.id.donorTextView);
        }
    }

    public interface OnDonorClickListener {
        void onDonorClick(DonorModel donor);
    }
}

