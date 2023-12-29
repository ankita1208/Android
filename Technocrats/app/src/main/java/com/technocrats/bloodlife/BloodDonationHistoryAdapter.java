package com.technocrats.bloodlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.List;

public class BloodDonationHistoryAdapter extends RecyclerView.Adapter<BloodDonationHistoryAdapter.ViewHolder> {
    private List<BloodDonationHistoryData> historyDataList;
    private OnDonationClickListener listener;

    public BloodDonationHistoryAdapter(List<BloodDonationHistoryData> historyDataList) {
        this.historyDataList = historyDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodDonationHistoryData historyData = historyDataList.get(position);
        holder.bloodTypeTextView.setText("Donor : " + historyData.getDonorName());
        holder.donationButton.setVisibility(historyData.isDonated() ? View.GONE : View.VISIBLE);
        holder.donationButton.setOnClickListener(v -> listener.onDonationClick(historyData));
    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bloodTypeTextView;
        public TextView donationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodTypeTextView = itemView.findViewById(R.id.textTV);
            donationButton = itemView.findViewById(R.id.donatedButton);
        }
    }

    public interface OnDonationClickListener {
        void onDonationClick(BloodDonationHistoryData historyData);
    }
}