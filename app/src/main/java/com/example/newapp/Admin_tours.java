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

public class Admin_tours extends AppCompatActivity {

    LinearLayout tourForm;
    EditText destinationIdEditText, startDateEditText, endDateEditText, amountEditText;
    Button addTourButton, addTourSubmitButton, addTourCancelButton;

    DatabaseHelper dbHelper;
    TableLayout tourTable;

    LinearLayout editTourDialog;
    EditText editDestinationIdEditText, editStartDateEditText, editEndDateEditText, editAmountEditText;
    Button editTourSubmitButton, editTourCancelButton;
    int editingTourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tours);

        tourForm = findViewById(R.id.tourForm);
        destinationIdEditText = findViewById(R.id.destinationIdEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        amountEditText = findViewById(R.id.amountEditText);
        addTourButton = findViewById(R.id.addTourButton);
        addTourSubmitButton = findViewById(R.id.addTourSubmitButton);
        addTourCancelButton = findViewById(R.id.addTourCancelButton);

        dbHelper = new DatabaseHelper(this);
        tourTable = findViewById(R.id.tourTable);

        editTourDialog = findViewById(R.id.editTourDialog);
        editDestinationIdEditText = findViewById(R.id.editDestinationIdEditText);
        editStartDateEditText = findViewById(R.id.editStartDateEditText);
        editEndDateEditText = findViewById(R.id.editEndDateEditText);
        editAmountEditText = findViewById(R.id.editAmountEditText);
        editTourSubmitButton = findViewById(R.id.editTourSubmitButton);
        editTourCancelButton = findViewById(R.id.editTourCancelButton);

        populateTourTable();

        addTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourForm.setVisibility(View.VISIBLE);
            }
        });

        addTourSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTourToDatabase();
            }
        });

        addTourCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTourForm();
                tourForm.setVisibility(View.GONE);
            }
        });

    }

    private void populateTourTable() {
        Cursor cursor = dbHelper.getAllTours();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                int destinationId = cursor.getInt(cursor.getColumnIndex("ID_destination"));
                String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                String endDate = cursor.getString(cursor.getColumnIndex("end_date"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                idTextView.setText(String.valueOf(id));

                TextView destinationIdTextView = new TextView(this);
                destinationIdTextView.setText(String.valueOf(destinationId));

                TextView startDateTextView = new TextView(this);
                startDateTextView.setText(startDate);

                TextView endDateTextView = new TextView(this);
                endDateTextView.setText(endDate);

                TextView amountTextView = new TextView(this);
                amountTextView.setText(String.valueOf(amount));

                LinearLayout actionLayout = createActionLayout(id);


                row.addView(idTextView);
                row.addView(destinationIdTextView);
                row.addView(startDateTextView);
                row.addView(endDateTextView);
                row.addView(amountTextView);
                row.addView(actionLayout);

                tourTable.addView(row);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private LinearLayout createActionLayout(final int tourId) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton editButton = createImageButton(R.drawable.ic_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingTourId = tourId;
                showEditDialog(tourId);
            }
        });

        ImageButton deleteButton = createImageButton(R.drawable.ic_bin);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(tourId);
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

    private void addTourToDatabase() {
        int destinationId = Integer.parseInt(destinationIdEditText.getText().toString().trim());
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        int amount = Integer.parseInt(amountEditText.getText().toString().trim());

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(Admin_tours.this, "Please fill in missing fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addTour(destinationId, startDate, endDate, amount);

        if (isInserted) {
            Toast.makeText(Admin_tours.this, "Tour added successfully", Toast.LENGTH_SHORT).show();
            clearTourForm();
            refreshTourTable();
        } else {
            Toast.makeText(Admin_tours.this, "Failed to add tour", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final int tourId) {
        // Populate edit dialog with tour data
        Cursor tourCursor = dbHelper.getTourById(tourId);
        if (tourCursor != null && tourCursor.moveToFirst()) {
            int destinationId = tourCursor.getInt(tourCursor.getColumnIndex("ID_destination"));
            String startDate = tourCursor.getString(tourCursor.getColumnIndex("start_date"));
            String endDate = tourCursor.getString(tourCursor.getColumnIndex("end_date"));
            int amount = tourCursor.getInt(tourCursor.getColumnIndex("amount"));

            editDestinationIdEditText.setText(String.valueOf(destinationId));
            editStartDateEditText.setText(startDate);
            editEndDateEditText.setText(endDate);
            editAmountEditText.setText(String.valueOf(amount));

            tourCursor.close();
        }

        // Show the edit dialog
        editTourDialog.setVisibility(View.VISIBLE);

        // Handle edit dialog button clicks
        editTourSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save edited tour data
                int newDestinationId = Integer.parseInt(editDestinationIdEditText.getText().toString());
                String newStartDate = editStartDateEditText.getText().toString();
                String newEndDate = editEndDateEditText.getText().toString();
                int newAmount = Integer.parseInt(editAmountEditText.getText().toString());

                dbHelper.updateTour(tourId, newDestinationId, newStartDate, newEndDate, newAmount);
                editTourDialog.setVisibility(View.GONE);
                refreshTourTable();
            }
        });

        editTourCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                editTourDialog.setVisibility(View.GONE);
            }
        });
    }

    private void showDeleteConfirmationDialog(final int tourId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this tour?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the tour
                        dbHelper.deleteTour(tourId);
                        // Refresh the tour table
                        refreshTourTable();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearTourForm() {
        destinationIdEditText.setText("");
        startDateEditText.setText("");
        endDateEditText.setText("");
        amountEditText.setText("");
    }

    private void refreshTourTable() {
        for (int i = tourTable.getChildCount() - 1; i > 0; i--) {
            View child = tourTable.getChildAt(i);
            if (child instanceof TableRow) {
                tourTable.removeViewAt(i);
            }
        }
        populateTourTable();
    }
}

