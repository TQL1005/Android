package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class Admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button btnBack = findViewById(R.id.exitButton);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_home.this,Main.class);
            startActivity(intent);
        });

    }

    public void onIcon1Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User();
            }
        });
    }
    public void User(){
        Intent intent = new Intent(this, Admin_user.class);
        startActivity(intent);
    }

    public void onIcon2Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Destination();
            }
        });
    }
    public void Destination(){
        Intent intent = new Intent(this, Admin_destination.class);
        startActivity(intent);
    }

    public void onIcon3Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tour();
            }
        });
    }
    public void Tour(){
        Intent intent = new Intent(this, Admin_tours.class);
        startActivity(intent);
    }

    public void onIcon4Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill();
            }
        });
    }
    public void Bill(){
        Intent intent = new Intent(this, Admin_bill.class);
        startActivity(intent);
    }

    public void onIcon5Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback();
            }
        });
    }
    public void Feedback(){
        Intent intent = new Intent(this, Admin_feedback.class);
        startActivity(intent);
    }

    public void onIcon6Clicked(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.icon6);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statistics();
            }
        });
    }
    public void Statistics(){
        Intent intent = new Intent(this, Admin_statistics.class);
        startActivity(intent);
    }


//    public void onExitClicked(View view) {
//        Button btn = (Button) findViewById(R.id.exitButton);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Main();
//            }
//        });
//    }
//    public void Main(){
//        Intent intent = new Intent(this, Admin_home.class);
//        startActivity(intent);
//    }
}