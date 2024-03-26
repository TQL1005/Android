package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Destination> arrayList = dbHelper.getDes();
        DesAdapter desAdapter = new DesAdapter(this,R.layout.list_item,arrayList);
        listView.setAdapter(desAdapter);


        searchBar = findViewById(R.id.Start);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this,Tour_List.class);
                startActivity(intent);
            }
        });

       }
}