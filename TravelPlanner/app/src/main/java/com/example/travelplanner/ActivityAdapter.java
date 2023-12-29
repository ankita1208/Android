package com.example.travelplanner;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityItem> activityList;
    private AppDatabase appDatabase;

    public ActivityAdapter(List<ActivityItem> activityList, AppDatabase appDatabase) {
        this.activityList = activityList;
        this.appDatabase = appDatabase;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem activityItem = activityList.get(position);
        holder.activityImageView.setImageResource(activityItem.getImageResource());
        holder.activityNameTextView.setText(activityItem.getActivity());
        holder.addToActivitiesSwitch.setTag(position);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView activityImageView;
        TextView activityNameTextView;
        Switch addToActivitiesSwitch;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityImageView = itemView.findViewById(R.id.activityImageView);
            activityNameTextView = itemView.findViewById(R.id.activityNameTextView);
            addToActivitiesSwitch = itemView.findViewById(R.id.addToActivitiesSwitch);

            addToActivitiesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = (int) addToActivitiesSwitch.getTag();
                    ActivityItem activityItem = activityList.get(position);

                    if (isChecked) {
                        saveActivityToDatabase(activityItem);
                    } else {
                        removeActivityFromDatabase(activityItem);
                    }
                }
            });
        }
    }

    private void saveActivityToDatabase(ActivityItem activityItem) {
        ActivityEntity activityEntity = new ActivityEntity(
                activityItem.getCategory(),
                activityItem.getActivity(),
                activityItem.getImageResource()
        );
        new InsertActivityAsyncTask(appDatabase.activityDao()).execute(activityEntity);

    }

    private void removeActivityFromDatabase(ActivityItem activityItem) {
        new DeleteActivityAsyncTask(appDatabase.activityDao()).execute(activityItem.getActivity());
    }


    private static class InsertActivityAsyncTask extends AsyncTask<ActivityEntity, Void, Void> {
        private ActivityDao activityDao;

        private InsertActivityAsyncTask(ActivityDao activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(ActivityEntity... activityEntities) {
            activityDao.insertActivity(activityEntities[0]);
            return null;
        }
    }

    private static class DeleteActivityAsyncTask extends AsyncTask<String, Void, Void> {
        private ActivityDao activityDao;

        private DeleteActivityAsyncTask(ActivityDao activityDao) {
            this.activityDao = activityDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            activityDao.deleteActivity(strings[0]);
            return null;
        }
    }
}
