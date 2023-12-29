package com.example.travelplanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// ItineraryPlanner.java
public class ItinaryPlanner extends AppCompatActivity {

    private AppDatabase appDatabase;
    private EditText fromEditText, toEditText, departureEditText, returnEditText, adultsEditText, childrenEditText;
    private Button editItineraryBtn, addActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinary_planner);

        appDatabase = AppDatabase.getInstance(this);

        fromEditText = findViewById(R.id.fromET);
        toEditText = findViewById(R.id.toET);
        departureEditText = findViewById(R.id.departET);
        returnEditText = findViewById(R.id.returnET);
        adultsEditText = findViewById(R.id.adultsET);
        childrenEditText = findViewById(R.id.childET);
        editItineraryBtn = findViewById(R.id.editItineraryBtn);
        addActivityBtn = findViewById(R.id.addActivityBtn);
        addActivityBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItinaryPlanner.this, AddActivity.class);
                startActivity(intent);
            }
        });

        editItineraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLocation = fromEditText.getText().toString().trim();
                String toLocation = toEditText.getText().toString().trim();
                String departureDate = departureEditText.getText().toString().trim();
                String returnDate = returnEditText.getText().toString().trim();
                String adultsStr = adultsEditText.getText().toString().trim();
                String childrenStr = childrenEditText.getText().toString().trim();

                if (fromLocation.isEmpty() || toLocation.isEmpty() || departureDate.isEmpty() ||
                        returnDate.isEmpty() || adultsStr.isEmpty() || childrenStr.isEmpty()) {
                    // Show a toast or alert indicating that all fields must be filled
                    Toast.makeText(ItinaryPlanner.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                int adults = Integer.parseInt(adultsStr);
                int children = Integer.parseInt(childrenStr);

                ItineraryEntity itinerary = new ItineraryEntity();
                itinerary.setFromLocation(fromLocation);
                itinerary.setToLocation(toLocation);
                itinerary.setDepartureDate(departureDate);
                itinerary.setReturnDate(returnDate);
                itinerary.setAdults(adults);
                itinerary.setChildren(children);

                insertItinerary(itinerary);

                Intent intent = new Intent(ItinaryPlanner.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertItinerary(ItineraryEntity itinerary) {
        new InsertItineraryAsyncTask(appDatabase.itineraryDao()).execute(itinerary);
    }

    private static class InsertItineraryAsyncTask extends AsyncTask<ItineraryEntity, Void, Void> {

        private ItineraryDao itineraryDao;

        private InsertItineraryAsyncTask(ItineraryDao itineraryDao) {
            this.itineraryDao = itineraryDao;
        }

        @Override
        protected Void doInBackground(ItineraryEntity... itineraries) {
            itineraryDao.insert(itineraries[0]);
            return null;
        }
    }
}
