package com.example.newapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Admin_user extends AppCompatActivity {

    LinearLayout userForm;
    EditText idEditText, usernameEditText, passwordEditText, roleEditText, phoneEditText;
    Button addUserButton, addUserSubmitButton, addUserCancelButton;

    DatabaseHelper dbHelper;
    TableLayout userTable;

    LinearLayout editUserDialog;
    EditText editIdEditText, editUsernameEditText, editPasswordEditText, editRoleEditText, editPhoneEditText;
    Button editUserSubmitButton, editUserCancelButton;
    int editingUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        userForm = findViewById(R.id.userForm);
        idEditText = findViewById(R.id.idEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleEditText = findViewById(R.id.roleEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addUserButton = findViewById(R.id.addUserButton);
        addUserSubmitButton = findViewById(R.id.addUserSubmitButton);
        addUserCancelButton = findViewById(R.id.addUserCancelButton);

        dbHelper = new DatabaseHelper(this);
        userTable = findViewById(R.id.userTable);

        editUserDialog = findViewById(R.id.editUserDialog);
        editIdEditText = findViewById(R.id.editIdEditText);
        editUsernameEditText = findViewById(R.id.editUsernameEditText);
        editPasswordEditText = findViewById(R.id.editPasswordEditText);
        editRoleEditText = findViewById(R.id.editRoleEditText);
        editPhoneEditText = findViewById(R.id.editPhoneEditText);
        editUserSubmitButton = findViewById(R.id.editUserSubmitButton);
        editUserCancelButton = findViewById(R.id.editUserCancelButton);

        populateUserTable();

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userForm.setVisibility(View.VISIBLE);
            }
        });

        addUserSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToDatabase();
            }
        });

        addUserCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idEditText.setText("");
                usernameEditText.setText("");
                passwordEditText.setText("");
                roleEditText.setText("");
                phoneEditText.setText("");
                userForm.setVisibility(View.GONE);
            }
        });

        Button btnBack = findViewById(R.id.exitButton);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_user.this,Admin_home.class);
            startActivity(intent);
        });

    }

    private void populateUserTable() {
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                int role = cursor.getInt(cursor.getColumnIndex("role"));
                int phone = cursor.getInt(cursor.getColumnIndex("phone"));

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                idTextView.setText(String.valueOf(id));

                TextView usernameTextView = new TextView(this);
                usernameTextView.setText(username);

                TextView passwordTextView = new TextView(this);
                passwordTextView.setText(password.substring(0, 6)+"...");

                TextView roleTextView = new TextView(this);
                roleTextView.setText(String.valueOf(role));

                TextView phoneTextView = new TextView(this);
                phoneTextView.setText(String.valueOf(phone));

                LinearLayout actionLayout = createActionLayout(id);


                row.addView(idTextView);
                row.addView(usernameTextView);
                row.addView(passwordTextView);
                row.addView(roleTextView);
                row.addView(phoneTextView);
                row.addView(actionLayout);

                userTable.addView(row);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
    private LinearLayout createActionLayout(final int userId) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton editButton = createImageButton(R.drawable.ic_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingUserId = userId;
                showEditDialog(userId);
            }
        });

        ImageButton deleteButton = createImageButton(R.drawable.ic_bin);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(userId);
            }
        });

        layout.addView(editButton);
        layout.addView(deleteButton);

        return layout;
    }

    private ImageButton createImageButton(int iconResource) {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(iconResource);
        return imageButton;
    }

    private void addUserToDatabase() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(Admin_user.this, "Please fill in missing field", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addUser(username, password, Integer.parseInt(role), phone);

        if (isInserted) {
            Toast.makeText(Admin_user.this, "User added successfully", Toast.LENGTH_SHORT).show();
            usernameEditText.setText("");
            passwordEditText.setText("");
            roleEditText.setText("");
            phoneEditText.setText("");
            refreshUserTable();
        } else {
            Toast.makeText(Admin_user.this, "Failed to add user", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final int userId) {
        // Populate edit dialog with user data
        Cursor userCursor = dbHelper.getUserById(userId);
        if (userCursor != null && userCursor.moveToFirst()) {
            String username = userCursor.getString(userCursor.getColumnIndex("username"));
            String password = userCursor.getString(userCursor.getColumnIndex("password"));
            String role = userCursor.getString(userCursor.getColumnIndex("role"));
            String phone = userCursor.getString(userCursor.getColumnIndex("phone"));

            editIdEditText.setText(String.valueOf(userId));
            editUsernameEditText.setText(username);
            editPasswordEditText.setText(password);
            editRoleEditText.setText(role);
            editPhoneEditText.setText(phone);

            userCursor.close();
        }

        // Show the edit dialog
        editUserDialog.setVisibility(View.VISIBLE);

        // Handle edit dialog button clicks
        editUserSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save edited user data
                String newUsername = editUsernameEditText.getText().toString();
                String newPassword = editPasswordEditText.getText().toString();
                String newRole = editRoleEditText.getText().toString();
                String newPhone = editPhoneEditText.getText().toString();

                dbHelper.updateUser(userId, newUsername, newPassword, newRole, newPhone);
                editUserDialog.setVisibility(View.GONE);
                refreshUserTable();
            }
        });

        editUserCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                editUserDialog.setVisibility(View.GONE);
            }
        });
    }


    private void showDeleteConfirmationDialog(final int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the user
                        dbHelper.deleteUser(userId);
                        // Refresh the user table
                        refreshUserTable();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void refreshUserTable() {
        for (int i = userTable.getChildCount() - 1; i > 0; i--) {
            View child = userTable.getChildAt(i);
            if (child instanceof TableRow) {
                userTable.removeViewAt(i);
            }
        }
        populateUserTable();
    }
}