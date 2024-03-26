package com.example.newapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.mindrot.jbcrypt.BCrypt;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private Button btnLogin;
    EditText eUser, ePw, eRpw;
    Button btnRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eUser = findViewById(R.id.Email);
        ePw = findViewById(R.id.Password);
        eRpw = findViewById(R.id.Password1);
        btnRegister = findViewById(R.id.Register);
        dbHelper = new DatabaseHelper(this);
        //Login
        btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user;
                String pwd;
                String rePwd;
                user = eUser.getText().toString();
                pwd = ePw.getText().toString();
                rePwd = eRpw.getText().toString();
                if (user.equals("") || pwd.equals("") || rePwd.equals("")) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    if (pwd.equals(rePwd)) {
                        if (dbHelper.checkUsername(user)) {
                            Toast.makeText(Register.this, "User already exits", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Hash the password
                        String hashedPwd = hashPassword(pwd);
                        //Proceed insert data
                        boolean registerSuccess = dbHelper.insertDataUser(user, hashedPwd);
                        if (registerSuccess)
                            Toast.makeText(Register.this, "Successfully", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Register.this, "Failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Register.this, "Password do not match", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
    }


    //Login
    public void Login () {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        }

    public static String hashPassword(String password) {
        // Define a work factor (controls the computational cost)
        int workFactor = 12; // Recommended value

        // Hash the password using bcrypt
        return BCrypt.hashpw(password, BCrypt.gensalt(workFactor));
    }
    }
