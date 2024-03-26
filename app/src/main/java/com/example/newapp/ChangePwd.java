package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.example.newapp.Register.hashPassword;

public class ChangePwd extends AppCompatActivity {
    EditText  ePw;
    EditText  eRpw;
    String username;

    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        setContentView(R.layout.activity_change_pwd);
        dbHelper = new DatabaseHelper(this);
        Button btn = (Button) findViewById(R.id.ChangePwd);
        Button btn1 = (Button) findViewById(R.id.Back);
        ePw = findViewById(R.id.Password);
        eRpw = findViewById(R.id.Password1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePwd1();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePwd.this, Profile.class);
                intent.putExtra("username", username);
                ChangePwd.this.startActivity(intent);
            }
        });
    }

    public void ChangePwd1(){
        String pwd = ePw.getText().toString();
        String rePwd = eRpw.getText().toString();
        if (pwd.equals(rePwd)) {
            // Hash the password
            String hashedPwd = hashPassword(pwd);
            dbHelper.changePwd(hashedPwd);
            Toast.makeText(ChangePwd.this, "Password Changed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ChangePwd.this, "Password does not match", Toast.LENGTH_LONG).show();
        }
    }
}
