package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyTour extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ArrayAdapter<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Button btn1 = (Button) findViewById(R.id.Back3);

        dbHelper = new DatabaseHelper(this);
        ArrayList<String> details = dbHelper.getBooked(username);
        arrayList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        ListView details_booked = findViewById(R.id.details_booked);
        details_booked.setAdapter(arrayList);

        details_booked.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrayList.getItem(position); // Get the selected item from the adapter

            new AlertDialog.Builder(MyTour.this)
                    .setTitle("Do you want to delete this tour?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Extract the tour name from the selected item
                            String[] lines = item.split("\n");
                            String tourName = lines[0];
                            String tourDate = lines[1];
                            String currentTime = LocalDate.now().toString();

                            if(tourDate.equals(currentTime)){
                                // Remove the booked tour from the database and update the adapter
                                dbHelper.deleteBooked(username, tourName);
                                arrayList.remove(item);
                                arrayList.notifyDataSetChanged();
                            }else{
                                Toast.makeText(MyTour.this, "You can only cancel the tour on the day of the booked", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTour.this, Profile.class);
                intent.putExtra("username", username);
                MyTour.this.startActivity(intent);
            }
        });

    }
}