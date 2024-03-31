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
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class Feedback extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ArrayAdapter<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");


        dbHelper = new DatabaseHelper(this);
        ArrayList<String> details = dbHelper.getBooked(username);
        arrayList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        ListView details_booked = findViewById(R.id.details_booked);
        details_booked.setAdapter(arrayList);

        details_booked.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrayList.getItem(position); // Get the selected item from the adapter

            new AlertDialog.Builder(Feedback.this)
                    .setTitle("Do you want to write a feedback ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Extract the tour name from the selected item
                            String[] lines = item.split("\n");
                            String tourName = lines[0];
                            String tourDate = lines[1];
                            String currentTime = LocalDate.now().toString();

                            if (tourDate.compareTo(currentTime) < 0) {
                                // Remove the booked tour from the database and update the adapter
//                                dbHelper.deleteBooked(username, tourName);
//                                arrayList.remove(item);
//                                arrayList.notifyDataSetChanged();
                                Intent intent = new Intent(Feedback.this, Write_feedback.class);
                                intent.putExtra("username", username);
                                intent.putExtra("tourName", tourName);
                                Feedback.this.startActivity(intent);
                            } else {
                                Toast.makeText(Feedback.this, "You can only write feedback the tour when you finish the tour ", Toast.LENGTH_SHORT).show();
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


        Button btn1 = (Button) findViewById(R.id.Back5);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Feedback.this, Profile.class);
                intent.putExtra("username", username);
                Feedback.this.startActivity(intent);
            }
        });
    }
}