package com.technocrats.bloodlife;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocrats.bloodlife.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private final Activity activity;
    private final ArrayList<DrawerData> menuResponseArrayList;
    OnItemClickListener onItemClickListener;


    public DrawerAdapter(Activity activity, List<DrawerData> list, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        menuResponseArrayList = new ArrayList<>(list);
        this.onItemClickListener = onItemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textTV);
            ivImageView = itemView.findViewById(R.id.imageIV);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(activity.getLayoutInflater().inflate(R.layout.list_item_drawer, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvTitle.setText(menuResponseArrayList.get(position).getName());
        Integer drawer_icons[] = {R.drawable.home
                , R.drawable.interview
                , R.drawable.approved
                , R.drawable.appointment
                , R.drawable.heart
                , R.drawable.mortarboard
                , R.drawable.feedback
                , R.drawable.logout};

        int iconIndex = position % drawer_icons.length;
        int iconResId = drawer_icons[iconIndex];
        holder.ivImageView.setImageResource(iconResId);

        holder.tvTitle.setOnClickListener(view -> {
            onItemClickListener.onItemClick(menuResponseArrayList.get(position), position);
            notifyDataSetChanged();

        });
    }


    @Override
    public int getItemCount() {
        return menuResponseArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(DrawerData item, int position);
    }
}
