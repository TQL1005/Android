package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tour_List extends AppCompatActivity {
    SearchView searchView;
    ArrayAdapter<String> arrayList1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        getSupportActionBar().hide();

        ListView listView = findViewById(R.id.listView1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<String> arrayList = dbHelper.getDesName();
        arrayList1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayList1);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrayList1.getItem(position);

            Intent intent = new Intent(Tour_List.this,Detail_Tour.class);
            intent.putExtra("item",item);
            Tour_List.this.startActivity(intent);
        });

        searchView = findViewById(R.id.Search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Tour_List.this.arrayList1.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Tour_List.this.arrayList1.getFilter().filter(newText);
                return true;
            }
        });
    }

}