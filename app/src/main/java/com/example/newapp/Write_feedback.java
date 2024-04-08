package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Write_feedback extends AppCompatActivity {
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feedback);
        getSupportActionBar().hide();
        
        
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String tourName = intent.getStringExtra("tourName");

        EditText feedback = findViewById(R.id.feedback);

        dbHelper = new DatabaseHelper(this);
        Button btn1 = (Button) findViewById(R.id.submit);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackText = feedback.getText().toString();
                if(feedbackText.isEmpty()){
                    Toast.makeText(Write_feedback.this, "Please write your feedback", Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.WriteFeedBack(username, tourName,feedbackText);
                    Toast.makeText(Write_feedback.this, "Feedback submitted", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Write_feedback.this, Profile.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
    }
}