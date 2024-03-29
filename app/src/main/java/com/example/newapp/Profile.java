package com.example.newapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    DatabaseHelper dbHelper;
    TextView eUsername;
    static Integer temp = 0;
    float x11, x21, y11, y21;
    String username;
    private Button btn,btn1,btn2,btn3,btn4,btn5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
//        username = intent.getStringExtra("username");
        dbHelper = new DatabaseHelper(this);
        username = dbHelper.getUser();

        eUsername = findViewById(R.id.name);
        eUsername.setText(username);

//        Ket noi den Cloud
//        if (temp == 0){
//            initConfig();
//        }




        ImageView imageView = findViewById(R.id.imageView2);

        // Cloudinary URL of the image you want to retrieve
        String imageResourceId = "v1695711751/samples/man-portrait.jpg";

        /* Load the image into the ImageView using Glide */
        Glide.with(this)
                .load(MediaManager.get().url().generate(imageResourceId))
                .into(imageView);

        //ChangePwd
        btn = (Button) findViewById(R.id.change_password);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePwd();
            }
        });

        //CustomerSupport
        btn1 = (Button) findViewById(R.id.customer_support);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerSupport();
            }
        });
        
        btn3 = (Button) findViewById(R.id.follow_us);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follow();
            }
        });

        btn4 = (Button) findViewById(R.id.terms_amp_conditions);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term();
            }
        });

        btn2 = (Button) findViewById(R.id.my_booking);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTour();
            }
        });

        btn5 = (Button) findViewById(R.id.Logout);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

    }

    private void initConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "drhgfqbzv");
        config.put("api_key", "158819295119423");
        config.put("api_secret", "AoaAmRiOV3QXlVFVDuFA4UI2yfs");
        MediaManager.init(this, config);
        temp = temp +1;
    }

    public void Logout(){
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void ChangePwd(){
        Intent intent = new Intent(this, ChangePwd.class);
        intent.putExtra("username", username);
        Profile.this.startActivity(intent);
    }

    public void CustomerSupport(){
        Intent intent = new Intent(this, CustomerSupport.class);
        intent.putExtra("username", username);
        Profile.this.startActivity(intent);
    }

    public void Follow(){
        Intent intent = new Intent(this,Follow__Us.class);
        intent.putExtra("username", username);
        Profile.this.startActivity(intent);
    }

    public void Term(){
        Intent intent = new Intent(this,Term.class);
        intent.putExtra("username", username);
        Profile.this.startActivity(intent);
    }

    public void MyTour(){
        Intent intent = new Intent(this,MyTour.class);
        intent.putExtra("username", username);
        Profile.this.startActivity(intent);
    }
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x11 = touchEvent.getX();
                y11 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x21 = touchEvent.getX();
                y21 = touchEvent.getY();
                if(x11 > x21){
                    Intent i = new Intent(Profile.this,Main.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

    
}   