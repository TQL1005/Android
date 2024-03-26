package com.example.newapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class Admin_feedback extends AppCompatActivity {

    LinearLayout feedbackForm;
    EditText idEditText, userIdEditText, destinationIdEditText, feedbackEditText, dateFeedbackEditText;
    Button addFeedbackButton, addFeedbackSubmitButton, addFeedbackCancelButton;

    DatabaseHelper dbHelper;
    TableLayout feedbackTable;

    LinearLayout editFeedbackDialog;
    EditText editIdEditText, editUserIdEditText, editDestinationIdEditText, editFeedbackEditText, editDateFeedbackEditText;
    Button editFeedbackSubmitButton, editFeedbackCancelButton;
    int editingFeedbackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        feedbackForm = findViewById(R.id.feedbackForm);
        idEditText = findViewById(R.id.idEditText);
        userIdEditText = findViewById(R.id.userIdEditText);
        destinationIdEditText = findViewById(R.id.desIdEditText);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        dateFeedbackEditText = findViewById(R.id.dateEditText);
        addFeedbackButton = findViewById(R.id.addFeedbackButton);
        addFeedbackSubmitButton = findViewById(R.id.addFeedbackSubmitButton);
        addFeedbackCancelButton = findViewById(R.id.addFeedbackCancelButton);

        dbHelper = new DatabaseHelper(this);
        feedbackTable = findViewById(R.id.feedbackTable);

        editFeedbackDialog = findViewById(R.id.editFeedbackDialog);
        editIdEditText = findViewById(R.id.editIdEditText);
        editUserIdEditText = findViewById(R.id.editUserIdEditText);
        editDestinationIdEditText = findViewById(R.id.editDestinationIdEditText);
        editFeedbackEditText = findViewById(R.id.editFeedbackEditText);
        editDateFeedbackEditText = findViewById(R.id.editDateEditText);
        editFeedbackSubmitButton = findViewById(R.id.editFeedbackSubmitButton);
        editFeedbackCancelButton = findViewById(R.id.editFeedbackCancelButton);

        populateFeedbackTable();

        addFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackForm.setVisibility(View.VISIBLE);
            }
        });

        addFeedbackSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeedbackToDatabase();
            }
        });

        addFeedbackCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idEditText.setText("");
                userIdEditText.setText("");
                destinationIdEditText.setText("");
                feedbackEditText.setText("");
                dateFeedbackEditText.setText("");
                feedbackForm.setVisibility(View.GONE);
            }
        });

    }

    private void populateFeedbackTable() {
        Cursor cursor = dbHelper.getAllFeedbacks();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                int userId = cursor.getInt(cursor.getColumnIndex("ID_users"));
                int destinationId = cursor.getInt(cursor.getColumnIndex("ID_destination"));
                String feedback = cursor.getString(cursor.getColumnIndex("feedback"));
                String dateFeedback = cursor.getString(cursor.getColumnIndex("date_feedback"));

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                idTextView.setText(String.valueOf(id));

                TextView userIdTextView = new TextView(this);
                userIdTextView.setText(String.valueOf(userId));

                TextView destinationIdTextView = new TextView(this);
                destinationIdTextView.setText(String.valueOf(destinationId));

                TextView feedbackTextView = new TextView(this);
                feedbackTextView.setText(feedback.substring(0,7)+"...");

                TextView dateFeedbackTextView = new TextView(this);
                dateFeedbackTextView.setText(dateFeedback);

                LinearLayout actionLayout = createActionLayout(id);


                row.addView(idTextView);
                row.addView(userIdTextView);
                row.addView(destinationIdTextView);
                row.addView(feedbackTextView);
                row.addView(dateFeedbackTextView);
                row.addView(actionLayout);

                feedbackTable.addView(row);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private LinearLayout createActionLayout(final int feedbackId) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton editButton = createImageButton(R.drawable.ic_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingFeedbackId = feedbackId;
                showEditDialog(feedbackId);
            }
        });

        ImageButton deleteButton = createImageButton(R.drawable.ic_bin);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(feedbackId);
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

    private void addFeedbackToDatabase() {
        String userId = userIdEditText.getText().toString().trim();
        String destinationId = destinationIdEditText.getText().toString().trim();
        String feedback = feedbackEditText.getText().toString().trim();
        String dateFeedback = dateFeedbackEditText.getText().toString().trim();

        if (userId.isEmpty() || destinationId.isEmpty() || feedback.isEmpty() || dateFeedback.isEmpty()) {
            Toast.makeText(Admin_feedback.this, "Please fill in missing field", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addFeedback(Integer.parseInt(userId), Integer.parseInt(destinationId), feedback, dateFeedback);

        if (isInserted) {
            Toast.makeText(Admin_feedback.this, "Feedback added successfully", Toast.LENGTH_SHORT).show();
            userIdEditText.setText("");
            destinationIdEditText.setText("");
            feedbackEditText.setText("");
            dateFeedbackEditText.setText("");
            refreshFeedbackTable();
        } else {
            Toast.makeText(Admin_feedback.this, "Failed to add feedback", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final int feedbackId) {
        // Populate edit dialog with feedback data
        Cursor feedbackCursor = dbHelper.getFeedbackById(feedbackId);
        if (feedbackCursor != null && feedbackCursor.moveToFirst()) {
            int userId = feedbackCursor.getInt(feedbackCursor.getColumnIndex("ID_users"));
            int destinationId = feedbackCursor.getInt(feedbackCursor.getColumnIndex("ID_destination"));
            String feedback = feedbackCursor.getString(feedbackCursor.getColumnIndex("feedback"));
            String dateFeedback = feedbackCursor.getString(feedbackCursor.getColumnIndex("date_feedback"));

//            editIdEditText.setText(String.valueOf(feedbackId));
            editUserIdEditText.setText(String.valueOf(userId));
            editDestinationIdEditText.setText(String.valueOf(destinationId));
            editFeedbackEditText.setText(feedback);
            editDateFeedbackEditText.setText(dateFeedback);

            feedbackCursor.close();
        }

        // Show the edit dialog
        editFeedbackDialog.setVisibility(View.VISIBLE);

        // Handle edit dialog button clicks
        editFeedbackSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save edited feedback data
                int newUserId = Integer.parseInt(editUserIdEditText.getText().toString());
                int newDestinationId = Integer.parseInt(editDestinationIdEditText.getText().toString());
                String newFeedback = editFeedbackEditText.getText().toString();
                String newDateFeedback = editDateFeedbackEditText.getText().toString();

                dbHelper.updateFeedback(feedbackId, newUserId, newDestinationId, newFeedback, newDateFeedback);
                editFeedbackDialog.setVisibility(View.GONE);
                refreshFeedbackTable();
            }
        });

        editFeedbackCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                editFeedbackDialog.setVisibility(View.GONE);
            }
        });
    }

    private void showDeleteConfirmationDialog(final int feedbackId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this feedback?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the feedback
                        dbHelper.deleteFeedback(feedbackId);
                        // Refresh the feedback table
                        refreshFeedbackTable();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void refreshFeedbackTable() {
        for (int i = feedbackTable.getChildCount() - 1; i > 0; i--) {
            View child = feedbackTable.getChildAt(i);
            if (child instanceof TableRow) {
                feedbackTable.removeViewAt(i);
            }
        }
        populateFeedbackTable();
    }
}

