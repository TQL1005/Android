package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class Detail_Tour extends AppCompatActivity {
    SearchView searchBar;
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
        DesAdapter desAdapter = new DesAdapter(this,R.layout.list_item,arrayList);
        listView.setAdapter(desAdapter);


        searchBar = findViewById(R.id.Start_detail_tour);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_Tour.this,Tour_List.class);
                startActivity(intent);
            }
        });
    }
}