package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Full_Details_Tour extends AppCompatActivity {
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details_tour);


        Intent intent = getIntent();
        String destinationName = intent.getStringExtra("destinationName");
        String tourName = intent.getStringExtra("tourName");
        double tourPrice = intent.getDoubleExtra("tourPrice", 0.0); // 0.0 is the default value if "tourPrice" is not found
        String tourDescription = intent.getStringExtra("tourDescription");
        String tourImage = intent.getStringExtra("tourImage");
        String date = intent.getStringExtra("date");

        TextView Destination = findViewById(R.id.Destination1);
        TextView TourName = findViewById(R.id.tour_name1);
        TextView TourPrice = findViewById(R.id.PriceBook);
        TextView TourDescription = findViewById(R.id.Description1);
        ImageView TourImage = findViewById(R.id.imageView3);
        ImageView TourImage1 = findViewById(R.id.imageView4q);
        ImageView TourImage2 = findViewById(R.id.imageView5q);
        ImageView TourImage3 = findViewById(R.id.imageView6q);
        TextView Rating = findViewById(R.id.Rating);
        TextView Rating_people = findViewById(R.id.Rating_people);
        TextView Booked = findViewById(R.id.Booked);
        Button btn = findViewById(R.id.Booknow);
        EditText people_amount = findViewById(R.id.people_amount1);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
            btn.setOnClickListener(v -> {
                int people = Integer.parseInt(people_amount.getText().toString());
                if(people == 0) {
                    Toast.makeText(this, "Please enter the number of people", Toast.LENGTH_LONG).show();
                }else {
                    if (dbHelper.checkAmount(people, tourName)) {
                        Intent intent1 = new Intent(Full_Details_Tour.this, Checkout.class);
                        intent1.putExtra("tourName", tourName);
                        intent1.putExtra("tourPrice", tourPrice);
                        intent1.putExtra("date", date);
                        intent1.putExtra("people", people);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(this, "Not enough people", Toast.LENGTH_LONG).show();
                    }
                }
            });



        int random = new Random().nextInt(3) + 3; // [0, 2] + 3 => [3, 5]
        double random1 = new Random().nextDouble() * 2 + 3; // Generates a value between [3, 5)
        double random2 = new Random().nextDouble() * 2 + 3; // Generates a value between [3, 5)

        Rating.setText(String.format("%d ★", random));
        Rating_people.setText(String.format("%.1fk+ Ratings", random1));
        Booked.setText(String.format("%.1fk+ Booked", random2));

        ArrayList<Destination> arrayList = dbHelper.getPics(destinationName);
        String pic2 = arrayList.get(0).getPic2();
        String pic3 = arrayList.get(0).getPic3();
        String pic4 = arrayList.get(0).getPic4();


        Destination.setText(destinationName);
        TourName.setText(tourName);
        TourPrice.setText(String.valueOf(tourPrice) + "$");
        TourDescription.setText(tourDescription);

        Glide.with(this) // Use "this" if you are inside an Activity
                .load(MediaManager.get().url().generate(tourImage)) // Corrected Glide syntax
                .into(TourImage);
        Glide.with(this) // Use "this" if you are inside an Activity
                .load(MediaManager.get().url().generate(pic2)) // Corrected Glide syntax
                .into(TourImage1);
        Glide.with(this) // Use "this" if you are inside an Activity
                .load(MediaManager.get().url().generate(pic3)) // Corrected Glide syntax
                .into(TourImage2);
        Glide.with(this) // Use "this" if you are inside an Activity
                .load(MediaManager.get().url().generate(pic4)) // Corrected Glide syntax
                .into(TourImage3);


    }
}
