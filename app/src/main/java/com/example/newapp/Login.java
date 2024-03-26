package com.example.newapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Button btn;
    DatabaseHelper dbHelper;
    Button buttonLogin;
    EditText eUsername,ePwd;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        //Register
        btn = (Button) findViewById(R.id.register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        dbHelper = new DatabaseHelper(this);
        eUsername = findViewById(R.id.Email);
        ePwd = findViewById(R.id.Password);
        buttonLogin = findViewById(R.id.Login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLoggin = dbHelper.checkUser(eUsername.getText().toString(),ePwd.getText().toString());
                if (isLoggin){
//                    Intent intent = new Intent(Login.this,Profile.class);
//                    intent.putExtra("username",eUsername.getText().toString());
//                    Login.this.startActivity(intent);

                    Intent intent = new Intent(Login.this,Main.class);
                    Login.this.startActivity(intent);
                }else{
                    Toast.makeText(Login.this,"Username or Password does not match",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void Register(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}