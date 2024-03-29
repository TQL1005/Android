package com.example.newapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Checkout extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        String tourName = intent.getStringExtra("tourName");
        double tourPrice = intent.getDoubleExtra("tourPrice", 0.0); // 0.0 is the default value if "tourPrice" is not found
        String date = intent.getStringExtra("date");
        int people = intent.getIntExtra("people", 0);

        TextView TourName = findViewById(R.id.tour_name2);
        TextView TourPrice = findViewById(R.id.total);
        TextView Date = findViewById(R.id.dateStart);
        TextView people_amount = findViewById(R.id.people_amount);
        TextView name = findViewById(R.id.name1);
        TextView Email = findViewById(R.id.email1);

        TourName.setText(tourName);
        Date.setText(date);

        // Get the user's name and email from the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String username = dbHelper.getUser();
        String email = dbHelper.getUser();
        name.setText(username);
        Email.setText(email);

        // Get the number of people from the EditText
        people_amount.setText(Integer.toString(people));
        double total = tourPrice * people;
        TourPrice.setText(String.format("Total: $%.2f", total));


        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(v -> {
            dbHelper.saveBooking(username, tourName, people, total);
            Toast.makeText(Checkout.this,"Success",Toast.LENGTH_LONG).show();
        });



    }
}