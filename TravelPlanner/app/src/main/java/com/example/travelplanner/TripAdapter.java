package com.example.travelplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<ItineraryEntity> tripList;

    public TripAdapter(List<ItineraryEntity> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ItineraryEntity trip = tripList.get(position);
        holder.tripNameTextView.setText(trip.getToLocation());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }


    public void setItineraries(List<ItineraryEntity> itineraries) {
        this.tripList = itineraries;
        notifyDataSetChanged();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tripNameTextView;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTextView = itemView.findViewById(R.id.activityNameTextView);
        }
    }
}

