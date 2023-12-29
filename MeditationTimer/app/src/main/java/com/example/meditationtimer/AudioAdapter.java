package com.example.meditationtimer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private List<AudioItem> audioItems;

    private OnItemClickListener onItemClickListener;


    public AudioAdapter(List<AudioItem> audioItems) {
        this.audioItems = audioItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(AudioItem audioItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setAudioItems(List<AudioItem> audioItems) {
        this.audioItems = audioItems;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioItem audioItem = audioItems.get(position);

        // Bind data to the views
        holder.audioTitle.setText(audioItem.getTitle());
        holder.audioDuration.setText(audioItem.getDuration());
        holder.audioThumbnail.setImageResource(audioItem.getThumbnailResId());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(audioItem);
                Log.d("AudioAdapter", "Item clicked: " + audioItem.getAudioUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView audioTitle;
        TextView audioDuration;
        ImageView audioThumbnail;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioTitle = itemView.findViewById(R.id.audioTitle);
            audioDuration = itemView.findViewById(R.id.audioDuration);
            audioThumbnail = itemView.findViewById(R.id.audioThumbnail);
        }
    }
}
