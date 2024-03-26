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

public class Admin_destination extends AppCompatActivity {

    LinearLayout destinationForm;
    EditText idEditText, destinationEditText, descriptionEditText, priceEditText, pic1EditText, pic2EditText, pic3EditText, pic4EditText;
    Button addDestinationButton, addDestinationSubmitButton, addDestinationCancelButton;

    DatabaseHelper dbHelper;
    TableLayout destinationTable;

    LinearLayout editDestinationDialog;
    EditText editIdEditText, editDestinationEditText, editDescriptionEditText, editPriceEditText, editPic1EditText, editPic2EditText, editPic3EditText, editPic4EditText;
    Button editDestinationSubmitButton, editDestinationCancelButton;
    int editingDestinationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_destination);

        destinationForm = findViewById(R.id.destinationForm);
        idEditText = findViewById(R.id.idEditText);
        destinationEditText = findViewById(R.id.destinationNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        pic1EditText = findViewById(R.id.pic1EditText);
        pic2EditText = findViewById(R.id.pic2EditText);
        pic3EditText = findViewById(R.id.pic3EditText);
        pic4EditText = findViewById(R.id.pic4EditText);
        addDestinationButton = findViewById(R.id.addDestinationButton);
        addDestinationSubmitButton = findViewById(R.id.addDestinationSubmitButton);
        addDestinationCancelButton = findViewById(R.id.addDestinationCancelButton);

        dbHelper = new DatabaseHelper(this);
        destinationTable = findViewById(R.id.destinationTable);

        editDestinationDialog = findViewById(R.id.editDestinationDialog);
        editIdEditText = findViewById(R.id.editIdEditText);
        editDestinationEditText = findViewById(R.id.editDestinationNameEditText);
        editDescriptionEditText = findViewById(R.id.editDescriptionEditText);
        editPriceEditText = findViewById(R.id.editPriceEditText);
        editPic1EditText = findViewById(R.id.editPic1EditText);
        editPic2EditText = findViewById(R.id.editPic2EditText);
        editPic3EditText = findViewById(R.id.editPic3EditText);
        editPic4EditText = findViewById(R.id.editPic4EditText);
        editDestinationSubmitButton = findViewById(R.id.editDestinationSubmitButton);
        editDestinationCancelButton = findViewById(R.id.editDestinationCancelButton);

        populateDestinationTable();

        addDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationForm.setVisibility(View.VISIBLE);
            }
        });

        addDestinationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDestinationToDatabase();
            }
        });

        addDestinationCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDestinationForm();
                destinationForm.setVisibility(View.GONE);
            }
        });
    }

    private void populateDestinationTable() {
        Cursor cursor = dbHelper.getAllDestinations();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String destination = cursor.getString(cursor.getColumnIndex("destination"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
//                String pic1 = cursor.getString(cursor.getColumnIndex("pic1"));
//                String pic2 = cursor.getString(cursor.getColumnIndex("pic2"));
//                String pic3 = cursor.getString(cursor.getColumnIndex("pic3"));
//                String pic4 = cursor.getString(cursor.getColumnIndex("pic4"));

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                idTextView.setText(String.valueOf(id));

                TextView destinationTextView = new TextView(this);
                destinationTextView.setText(destination);

                TextView descriptionTextView = new TextView(this);
                descriptionTextView.setText(description);

                TextView priceTextView = new TextView(this);
                priceTextView.setText(String.valueOf(price));

//                TextView pic1TextView = new TextView(this);
//                pic1TextView.setText(pic1);
//
//                TextView pic2TextView = new TextView(this);
//                pic2TextView.setText(pic2);
//
//                TextView pic3TextView = new TextView(this);
//                pic3TextView.setText(pic3);
//
//                TextView pic4TextView = new TextView(this);
//                pic4TextView.setText(pic4);

                LinearLayout actionLayout = createActionLayout(id);

                row.addView(idTextView);
                row.addView(destinationTextView);
                row.addView(descriptionTextView);
                row.addView(priceTextView);
//                row.addView(pic1TextView);
//                row.addView(pic2TextView);
//                row.addView(pic3TextView);
//                row.addView(pic4TextView);
                row.addView(actionLayout);

                destinationTable.addView(row);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private LinearLayout createActionLayout(final int destinationId) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton editButton = createImageButton(R.drawable.ic_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingDestinationId = destinationId;
                showEditDialog(editingDestinationId);
            }
        });

        ImageButton deleteButton = createImageButton(R.drawable.ic_bin);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(destinationId);
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

    private void addDestinationToDatabase() {
        String destination = destinationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String pic1 = pic1EditText.getText().toString().trim();
        String pic2 = pic2EditText.getText().toString().trim();
        String pic3 = pic3EditText.getText().toString().trim();
        String pic4 = pic4EditText.getText().toString().trim();

        if (destination.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(Admin_destination.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        boolean isInserted = dbHelper.addDestination(destination, description, price, pic1, pic2, pic3, pic4);

        if (isInserted) {
            Toast.makeText(Admin_destination.this, "Destination added successfully", Toast.LENGTH_SHORT).show();
            clearDestinationForm();
            refreshDestinationTable();
        } else {
            Toast.makeText(Admin_destination.this, "Failed to add destination", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final int destinationId) {
        // Populate edit dialog with destination data
        Cursor destinationCursor = dbHelper.getDestinationById(destinationId);
        if (destinationCursor != null && destinationCursor.moveToFirst()) {
            String destination = destinationCursor.getString(destinationCursor.getColumnIndex("destination"));
            String description = destinationCursor.getString(destinationCursor.getColumnIndex("description"));
            double price = destinationCursor.getDouble(destinationCursor.getColumnIndex("price"));
            String pic1 = destinationCursor.getString(destinationCursor.getColumnIndex("pic1"));
            String pic2 = destinationCursor.getString(destinationCursor.getColumnIndex("pic2"));
            String pic3 = destinationCursor.getString(destinationCursor.getColumnIndex("pic3"));
            String pic4 = destinationCursor.getString(destinationCursor.getColumnIndex("pic4"));

//            editIdEditText.setText(String.valueOf(destinationId));
            editDestinationEditText.setText(destination);
            editDescriptionEditText.setText(description);
            editPriceEditText.setText(String.valueOf(price));
            editPic1EditText.setText(pic1);
            editPic2EditText.setText(pic2);
            editPic3EditText.setText(pic3);
            editPic4EditText.setText(pic4);

            destinationCursor.close();
        }

        // Show the edit dialog
        editDestinationDialog.setVisibility(View.VISIBLE);

        // Handle edit dialog button clicks
        editDestinationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save edited destination data
                String newDestination = editDestinationEditText.getText().toString();
                String newDescription = editDescriptionEditText.getText().toString();
                double newPrice = Double.parseDouble(editPriceEditText.getText().toString());
                String newPic1 = editPic1EditText.getText().toString();
                String newPic2 = editPic2EditText.getText().toString();
                String newPic3 = editPic3EditText.getText().toString();
                String newPic4 = editPic4EditText.getText().toString();

                dbHelper.updateDestination(destinationId, newDestination, newDescription, newPrice, newPic1, newPic2, newPic3, newPic4);
                editDestinationDialog.setVisibility(View.GONE);
                refreshDestinationTable();
            }
        });

        editDestinationCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                editDestinationDialog.setVisibility(View.GONE);
            }
        });
    }

    private void showDeleteConfirmationDialog(final int destinationId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this destination?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the destination
                        dbHelper.deleteDestination(destinationId);
                        // Refresh the destination table
                        refreshDestinationTable();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearDestinationForm() {
        destinationEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        pic1EditText.setText("");
        pic2EditText.setText("");
        pic3EditText.setText("");
        pic4EditText.setText("");
    }

    private void refreshDestinationTable() {
        for (int i = destinationTable.getChildCount() - 1; i > 0; i--) {
            View child = destinationTable.getChildAt(i);
            if (child instanceof TableRow) {
                destinationTable.removeViewAt(i);
            }
        }
        populateDestinationTable();
    }
}

