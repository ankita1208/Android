package com.technocrats.bloodlife;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.List;

public class EducationalResourceAdapter extends RecyclerView.Adapter<EducationalResourceAdapter.ViewHolder> {

    private List<EducationalResourceData> data;
    private Context context;

    public EducationalResourceAdapter(List<EducationalResourceData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EducationalResourceData resourceData = data.get(position);
        holder.titleTextView.setText(resourceData.getTitle() +"\n"+resourceData.getDescription() + "\n" + resourceData.getLink() );
        holder.titleTextView.setOnClickListener(v -> openWebPage(resourceData.getLink()));
    }

    private void openWebPage(String link) {
        Uri webpage = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView linkTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTV);
        }
    }
}
