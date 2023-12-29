package com.technocrats.bloodlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private long availableTimes;

    public ScheduleAdapter() {

    }
    public void updateData(long newAvailableTimes) {
        this.availableTimes = newAvailableTimes;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_schedule, parent, false);
        return new ScheduleAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        long donor = availableTimes;
        String dateString = convertTimestampToDate(donor);
        holder.timeTV.setText(dateString);
    }

    private String convertTimestampToDate(long timestamp) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            Date date = new Date(timestamp);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.timeTV);
        }
    }
}
