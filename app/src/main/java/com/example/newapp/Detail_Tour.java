package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Detail_Tour extends AppCompatActivity {
    SearchView searchBar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour);

        Intent intent = getIntent();
        String item = intent.getStringExtra("item");

        ListView listView = findViewById(R.id.listView_detail_tour);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Destination> arrayList = dbHelper.getDesForDetail(item);
        DesAdapter2 desAdapter = new DesAdapter2(this,R.layout.list_item2,arrayList);
        listView.setAdapter(desAdapter);
        TextView countTextView = findViewById(R.id.Count);
        countTextView.setText(String.valueOf(arrayList.size())+" Services");

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Destination destination = (Destination) desAdapter.getItem(position);
            String destinationName = destination.getDestinations(); // Example: Get the destination name
            Toast.makeText(this, destinationName, Toast.LENGTH_SHORT).show();
        });

        searchBar = findViewById(R.id.Start_detail_tour);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_Tour.this,Tour_List.class);
                startActivity(intent);
            }
        });
        Button btnBack = findViewById(R.id.back_arrow1);
        btnBack.setOnClickListener(v -> {
            Intent intent2 = new Intent(Detail_Tour.this,Main.class);
            startActivity(intent2);
        });
    }
}