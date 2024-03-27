package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main extends AppCompatActivity {
    SearchView searchBar;
    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ListView listView = findViewById(R.id.listView);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Destination> arrayList = dbHelper.getDes();
        DesAdapter desAdapter = new DesAdapter(this, R.layout.list_item, arrayList);
        listView.setAdapter(desAdapter);

        searchBar = findViewById(R.id.Start);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Tour_List.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Destination destination = (Destination) desAdapter.getItem(position);
            String destinationName = destination.getDestinations(); // Example: Get the destination name
            Intent it = new Intent(Main.this, Detail_Tour.class);
            it.putExtra("destinationName", destinationName);
            startActivity(it); // Start activity with the intent containing data
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 > x2){
                    Intent i = new Intent(Main.this,Profile.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
