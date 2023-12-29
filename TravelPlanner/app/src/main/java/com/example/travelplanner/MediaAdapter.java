package com.example.travelplanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<MediaEntity> mediaList;
    private Context context;

    public MediaAdapter(List<MediaEntity> mediaList, Context context) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MediaEntity mediaEntity = mediaList.get(position);

        // Construct the content URI
        String filePath = mediaEntity.getMediaUrl();
        String mediaId = filePath.substring(filePath.lastIndexOf("/") + 1);

        // Construct content URI
        Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaId);

        Picasso.get().invalidate(contentUri);

// Load image using Picasso with error handling
        try {
            // Open an InputStream for the content URI
            InputStream inputStream = context.getContentResolver().openInputStream(contentUri);

            // Load image using Picasso with InputStream
            Picasso.get()
                    .load(contentUri)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("exception", e.getMessage());
                            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.ViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the id of the clicked media entity
                int clickedMediaId = mediaEntity.getId();

                // Pass the id to the ViewMedia activity
                Intent viewMediaIntent = new Intent(view.getContext(), ViewMedia.class);
                viewMediaIntent.putExtra("MEDIA_ID", clickedMediaId);
                viewMediaIntent.putExtra("MEDIA_TITLE", mediaEntity.getTitle());
                viewMediaIntent.putExtra("MEDIA_DESCRIPTION", mediaEntity.getDescription());
                viewMediaIntent.putExtra("MEDIA_IMAGE_URL", mediaEntity.getMediaUrl());
                view.getContext().startActivity(viewMediaIntent);

            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the id of the clicked media entity
                int clickedMediaId = mediaEntity.getId();

                // Pass the id to the EditMediaDetails activity
                Intent editMediaIntent = new Intent(view.getContext(), EditMediaDetails.class);
                editMediaIntent.putExtra("MEDIA_ID", clickedMediaId);
                editMediaIntent.putExtra("MEDIA_TITLE", mediaEntity.getTitle());
                editMediaIntent.putExtra("MEDIA_DESCRIPTION", mediaEntity.getDescription());
                editMediaIntent.putExtra("MEDIA_IMAGE_URL", mediaEntity.getMediaUrl());
                view.getContext().startActivity(editMediaIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setMediaList(List<MediaEntity> updatedList) {
        mediaList = updatedList;
        notifyDataSetChanged();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button editBtn, ViewBtn;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
            editBtn = itemView.findViewById(R.id.editButton);
            ViewBtn = itemView.findViewById(R.id.viewButton);
        }
    }
}