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
        String desName = intent.getStringExtra("destinationName");

        ListView listView = findViewById(R.id.listView_detail_tour);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Destination> arrayList;

        if (item == null) {
            arrayList = dbHelper.getDesForDetail(desName);
        } else {
            arrayList = dbHelper.getDesForDetail(item);
        }

        DesAdapter2 desAdapter = new DesAdapter2(this, R.layout.list_item2, arrayList);
        listView.setAdapter(desAdapter);

        TextView countTextView = findViewById(R.id.Count);
        countTextView.setText(arrayList.size() + " Services");

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Destination destination = arrayList.get(position);
            String destinationName = destination.getDestinations();
            String tourName = destination.getTour_name();
            double tourPrice = destination.getPrice();
            String tourDescription = destination.getDescriptions();
            String tourImage = destination.getPic1();

            Intent fullDetailsIntent = new Intent(Detail_Tour.this, Full_Details_Tour.class);
            fullDetailsIntent.putExtra("destinationName", destinationName);
            fullDetailsIntent.putExtra("tourName", tourName);
            fullDetailsIntent.putExtra("tourPrice", tourPrice);
            fullDetailsIntent.putExtra("tourDescription", tourDescription);
            fullDetailsIntent.putExtra("tourImage", tourImage);
            startActivity(fullDetailsIntent);
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