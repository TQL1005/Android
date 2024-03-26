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

public class Admin_bill extends AppCompatActivity {

    LinearLayout billForm;
    EditText idEditText, tourIdEditText, userIdEditText, billDateEditText, billMoneyEditText, amountEditText;
    Button addBillButton, addBillSubmitButton, addBillCancelButton;

    DatabaseHelper dbHelper;
    TableLayout billTable;

    LinearLayout editBillDialog;
    EditText editTourIdEditText, editUserIdEditText, editBillDateEditText, editBillMoneyEditText, editAmountEditText;
    Button editBillSubmitButton, editBillCancelButton;
    int editingBillId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bill);

        billForm = findViewById(R.id.billForm);
        idEditText = findViewById(R.id.idEditText);
        tourIdEditText = findViewById(R.id.tourIdEditText);
        userIdEditText = findViewById(R.id.userIdEditText);
        billDateEditText = findViewById(R.id.billDateEditText);
        billMoneyEditText = findViewById(R.id.totalEditText);
        amountEditText = findViewById(R.id.amountEditText);
        addBillButton = findViewById(R.id.addBillButton);
        addBillSubmitButton = findViewById(R.id.addBillSubmitButton);
        addBillCancelButton = findViewById(R.id.addBillCancelButton);

        dbHelper = new DatabaseHelper(this);
        billTable = findViewById(R.id.billTable);

        editBillDialog = findViewById(R.id.editBillDialog);
        editTourIdEditText = findViewById(R.id.editTourIdEditText);
        editUserIdEditText = findViewById(R.id.editUserIdEditText);
        editBillDateEditText = findViewById(R.id.editBillDateEditText);
        editBillMoneyEditText = findViewById(R.id.editTotalEditText);
        editAmountEditText = findViewById(R.id.editAmountEditText);
        editBillSubmitButton = findViewById(R.id.editBillSubmitButton);
        editBillCancelButton = findViewById(R.id.editBillCancelButton);

        populateBillTable();

        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billForm.setVisibility(View.VISIBLE);
            }
        });

        addBillSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBillToDatabase();
            }
        });

        addBillCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idEditText.setText("");
                tourIdEditText.setText("");
                userIdEditText.setText("");
                billDateEditText.setText("");
                billMoneyEditText.setText("");
                amountEditText.setText("");
                billForm.setVisibility(View.GONE);
            }
        });

    }

    private void populateBillTable() {
        Cursor cursor = dbHelper.getAllBills();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                int tourId = cursor.getInt(cursor.getColumnIndex("ID_tours"));
                int userId = cursor.getInt(cursor.getColumnIndex("ID_users"));
                String billDate = cursor.getString(cursor.getColumnIndex("bill_date"));
                String billMoney = cursor.getString(cursor.getColumnIndex("bill_money"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                idTextView.setText(String.valueOf(id));

                TextView tourIdTextView = new TextView(this);
                tourIdTextView.setText(String.valueOf(tourId));

                TextView userIdTextView = new TextView(this);
                userIdTextView.setText(String.valueOf(userId));

                TextView billDateTextView = new TextView(this);
                billDateTextView.setText(billDate);

                TextView billMoneyTextView = new TextView(this);
                billMoneyTextView.setText(billMoney);

                TextView amountTextView = new TextView(this);
                amountTextView.setText(String.valueOf(amount));

                LinearLayout actionLayout = createActionLayout(id);


                row.addView(idTextView);
                row.addView(tourIdTextView);
                row.addView(userIdTextView);
                row.addView(billDateTextView);
                row.addView(billMoneyTextView);
                row.addView(amountTextView);
                row.addView(actionLayout);

                billTable.addView(row);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
    private LinearLayout createActionLayout(final int billId) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton editButton = createImageButton(R.drawable.ic_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingBillId = billId;
                showEditDialog(billId);
            }
        });

        ImageButton deleteButton = createImageButton(R.drawable.ic_bin);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(billId);
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

    private void addBillToDatabase() {
        int tourId = Integer.parseInt(tourIdEditText.getText().toString().trim());
        int userId = Integer.parseInt(userIdEditText.getText().toString().trim());
        String billDate = billDateEditText.getText().toString().trim();
        String billMoney = billMoneyEditText.getText().toString().trim();
        int amount = Integer.parseInt(amountEditText.getText().toString().trim());

        boolean isInserted = dbHelper.addBill(tourId, userId, billDate, billMoney, amount);

        if (isInserted) {
            Toast.makeText(Admin_bill.this, "Bill added successfully", Toast.LENGTH_SHORT).show();
            tourIdEditText.setText("");
            userIdEditText.setText("");
            billDateEditText.setText("");
            billMoneyEditText.setText("");
            amountEditText.setText("");
            refreshBillTable();
        } else {
            Toast.makeText(Admin_bill.this, "Failed to add bill", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final int billId) {
        // Populate edit dialog with bill data
        Cursor billCursor = dbHelper.getBillById(billId);
        if (billCursor != null && billCursor.moveToFirst()) {
            int tourId = billCursor.getInt(billCursor.getColumnIndex("ID_tours"));
            int userId = billCursor.getInt(billCursor.getColumnIndex("ID_users"));
            String billDate = billCursor.getString(billCursor.getColumnIndex("bill_date"));
            String billMoney = billCursor.getString(billCursor.getColumnIndex("bill_money"));
            int amount = billCursor.getInt(billCursor.getColumnIndex("amount"));

            editTourIdEditText.setText(String.valueOf(tourId));
            editUserIdEditText.setText(String.valueOf(userId));
            editBillDateEditText.setText(billDate);
            editBillMoneyEditText.setText(billMoney);
            editAmountEditText.setText(String.valueOf(amount));

            billCursor.close();
        }

        // Show the edit dialog
        editBillDialog.setVisibility(View.VISIBLE);

        // Handle edit dialog button clicks
        editBillSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save edited bill data
                int newTourId = Integer.parseInt(editTourIdEditText.getText().toString());
                int newUserId = Integer.parseInt(editUserIdEditText.getText().toString());
                String newBillDate = editBillDateEditText.getText().toString();
                String newBillMoney = editBillMoneyEditText.getText().toString();
                int newAmount = Integer.parseInt(editAmountEditText.getText().toString());

                dbHelper.updateBill(billId, newTourId, newUserId, newBillDate, newBillMoney, newAmount);
                editBillDialog.setVisibility(View.GONE);
                refreshBillTable();
            }
        });

        editBillCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                editBillDialog.setVisibility(View.GONE);
            }
        });
    }


    private void showDeleteConfirmationDialog(final int billId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this bill?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the bill
                        dbHelper.deleteBill(billId);
                        // Refresh the bill table
                        refreshBillTable();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void refreshBillTable() {
        for (int i = billTable.getChildCount() - 1; i > 0; i--) {
            View child = billTable.getChildAt(i);
            if (child instanceof TableRow) {
                billTable.removeViewAt(i);
            }
        }
        populateBillTable();
    }
}

