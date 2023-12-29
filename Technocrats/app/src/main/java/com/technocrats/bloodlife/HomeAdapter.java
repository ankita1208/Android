package com.technocrats.bloodlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<BloodRequest> bloodRequests;
    private OnItemClickListener listener;

    public HomeAdapter(List<BloodRequest> bloodRequests, OnItemClickListener listener) {
        this.bloodRequests = bloodRequests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodRequest request = bloodRequests.get(position);
        holder.bind(request);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView bloodGroupTextView;
        TextView urgencyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTV);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTV);
            urgencyTextView = itemView.findViewById(R.id.urgencyTV);
        }

        public void bind(BloodRequest request) {
            String displayName;
            if (request.getIsRequester() != null && request.getIsRequester().equalsIgnoreCase("true")) {
                displayName = "Requester: " + request.getRequesterName();
                bloodGroupTextView.setText(request.getRequiredType());
                urgencyTextView.setText(request.getUrgencyLevel());
            } else {
                displayName = "Donor: " + request.getName();
                bloodGroupTextView.setText(request.getBloodType());
            }

            nameTextView.setText(displayName);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(BloodRequest request);
    }
}