package com.example.newapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_statistics extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerStatisticsType;
    private ListView listViewStatistics;
    private TextView totalCountTextView;
    private TextView totalSumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_statistics);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Bind UI elements
        spinnerStatisticsType = findViewById(R.id.spinnerStatisticsType);
        listViewStatistics = findViewById(R.id.lvStatistics);
        totalCountTextView = findViewById(R.id.totalCountTextView);
        totalSumTextView = findViewById(R.id.totalSumTextView);

        // Set up spinner with options and selection listener
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.statistics_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatisticsType.setAdapter(spinnerAdapter);
        spinnerStatisticsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Refresh data based on selected sorting criteria
                refreshDataSortedBy(parent.getItemAtPosition(position).toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Default: sort by user
        refreshDataSortedBy("user");
    }

    private void refreshDataSortedBy(String sortCriteria) {
        Cursor cursor = null;
        switch (sortCriteria) {
            case "user":
                cursor = dbHelper.getBillsSortedByUser();
                break;
            case "date":
                cursor = dbHelper.getBillsSortedByDate();
                break;
            case "total cost":
                cursor = dbHelper.getBillsSortedByTotalCost();
                break;
        }

        if (cursor != null) {
            // Calculate total count and sum of bills
            int totalCount = cursor.getCount();
            int totalSum = 0;
            while (cursor.moveToNext()) {
                totalSum += cursor.getInt(cursor.getColumnIndex("bill_money"));
            }

            // Display total count and sum
            totalCountTextView.setText("Total Bills: " + totalCount);
            totalSumTextView.setText("Total Income: " + totalSum + "\n");

            // Create an array to store the statistics data
            String[] statisticsArray = new String[cursor.getCount()];
            int i = 0;
            cursor.moveToFirst();
            do {
                // Format data as per your requirement
                String data = "\n ID: " + cursor.getString(cursor.getColumnIndex("ID")) +
                        ", User ID: " + cursor.getString(cursor.getColumnIndex("ID_users")) +
                        ", Date: " + cursor.getString(cursor.getColumnIndex("bill_date")) +
                        ", Amount: " + cursor.getString(cursor.getColumnIndex("bill_money"));
                statisticsArray[i++] = data;
            } while (cursor.moveToNext());

            // Use ArrayAdapter to bind the data to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statisticsArray);
            listViewStatistics.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database to avoid memory leaks
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
