package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyTour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Button btn1 = (Button) findViewById(R.id.Back3);
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