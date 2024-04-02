package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Tour.db";
    private static String user ;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the "users" table
        sqLiteDatabase.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, role INTEGER, phone INTEGER,isDeleted boolean DEFAULT 0 )");

        // Create the "destinations" table
        sqLiteDatabase.execSQL("CREATE TABLE destinations (ID INTEGER PRIMARY KEY AUTOINCREMENT, destination TEXT, description TEXT, price DOUBLE,pic1 TEXT, pic2 TEXT, pic3 TEXT, pic4 TEXT,isDeleted boolean DEFAULT 0)");

        // Create the "tours" table with foreign key constraints
        sqLiteDatabase.execSQL("CREATE TABLE tours (ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_destination INTEGER, tour_name TEXT, start_date DATE, end_date DATE, amount INTEGER, isDeleted boolean DEFAULT 0 , FOREIGN KEY (ID_destination) REFERENCES destinations(ID))");

        // Create the "bill" table with foreign key constraints
        sqLiteDatabase.execSQL("CREATE TABLE bill (ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_tours INTEGER, ID_users INTEGER, bill_date DATE, bill_money DATE, amount INTEGER,isDeleted boolean DEFAULT 0, FOREIGN KEY (ID_tours) REFERENCES tours(ID), FOREIGN KEY (ID_users) REFERENCES users(ID))");

        // Create the "feedback" table with foreign key constraints
        sqLiteDatabase.execSQL("CREATE TABLE feedback (ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_users INTEGER, ID_tours INTEGER, feedback TEXT, date_feedback DATE,isDeleted boolean DEFAULT 0 , FOREIGN KEY (ID_users) REFERENCES users(ID), FOREIGN KEY (ID_tours) REFERENCES tours(ID))");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS destinations");
        db.execSQL("DROP TABLE IF EXISTS tours");
        db.execSQL("DROP TABLE IF EXISTS bill");
        db.execSQL("DROP TABLE IF EXISTS feedback");
        // Recreate the tables by calling onCreate
        onCreate(db);
    }




    // SQL code
    public boolean insertDataUser(String username,String password){
        SQLiteDatabase Databasename = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("role",1);
        long result = Databasename.insert("users",null,contentValues);
        if (result == -1) {return false;}
        else return true;
    }

    public boolean changePwd(String password){
        SQLiteDatabase Databasename = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);
        long result = Databasename.update("users",contentValues,"username = ?",new String[]{getUser()});
        if (result == -1) {return false;}
        else return true;
    }

    public boolean checkUsername(String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username = ?",new String[]{username});
        if (cursor.getCount() > 0){
            cursor.close();
            myDB.close();
            return true;
        }else return false;
    }



    public boolean checkUser(String username, String pwd) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to retrieve the hashed password based on the username
        String query = "SELECT password FROM users WHERE username = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            // Extract the hashed password from the cursor
            @SuppressLint("Range") String hashedPassword = cursor.getString(cursor.getColumnIndex("password"));

            // Verify if the entered password matches the hashed password
            boolean isPasswordCorrect = BCrypt.checkpw(pwd, hashedPassword);
            setUser(username.toString());

            // Close the cursor and database
            cursor.close();
            db.close();

            return isPasswordCorrect;
        } else {
            // Username not found
            cursor.close();
            db.close();
            return false;
        }
    }

    // retrive data
    public ArrayList<Destination> getDes(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Destination> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from destinations",null);
        while(cursor.moveToNext()){
            String destination = cursor.getString(cursor.getColumnIndex("destination"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String img = cursor.getString(cursor.getColumnIndex("pic1"));
            Destination destinationItem = new Destination(destination, description, price,img);
            list.add(destinationItem);

        }
        cursor.close();
        return list;
    }

    // retrive picture
    public ArrayList<Destination> getPics(String destinationName){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Destination> list = new ArrayList<>();
        String[] selectionArgs = {destinationName};
        Cursor cursor = db.rawQuery("select * from destinations where destination = ?",selectionArgs);
        while(cursor.moveToNext()){
            String img1 = cursor.getString(cursor.getColumnIndex("pic2"));
            String img2 = cursor.getString(cursor.getColumnIndex("pic3"));
            String img3 = cursor.getString(cursor.getColumnIndex("pic4"));
            Destination destinationItem = new Destination(img1, img2,img3);
            list.add(destinationItem);
        }
        cursor.close();
        return list;
    }



    public ArrayList<Destination> getDesForDetail(String item) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Destination> list = new ArrayList<>();
        String[] selectionArgs = {item};
        Cursor cursor = db.rawQuery("SELECT destinations.destination, destinations.description, destinations.price, destinations.pic1, " +
                "tours.start_date, tours.end_date, tours.amount,tours.tour_name " +
                "FROM destinations " +
                "JOIN tours ON destinations.ID = tours.ID_destination " +
                "WHERE destinations.destination = ? ", selectionArgs);
        while(cursor.moveToNext()) {
            String destination = cursor.getString(cursor.getColumnIndex("destination"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String img = cursor.getString(cursor.getColumnIndex("pic1"));
            String start_date = cursor.getString(cursor.getColumnIndex("start_date"));
            String end_date = cursor.getString(cursor.getColumnIndex("end_date"));
            String tour_name = cursor.getString(cursor.getColumnIndex("tour_name"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            Destination destinationItem = new Destination(destination, description, price, img,start_date,end_date,amount,tour_name);
            list.add(destinationItem);
        }

        cursor.close(); // Close the cursor when finished
        return list;
    }




    public ArrayList<String> getDesName(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select destination from destinations",null);
        while(cursor.moveToNext()){
            String destination = cursor.getString(cursor.getColumnIndex("destination"));
            String destinationItem = new String(destination);
            list.add(destinationItem);
        }
        cursor.close();
        return list;
    }


    public String getUser() {
        return user;
    }


    @SuppressLint("Range")
    public boolean checkAmount(Integer amount, String tour_name) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = myDB.rawQuery("SELECT amount FROM tours WHERE tour_name = ?", new String[]{tour_name});
            if (cursor != null && cursor.moveToFirst()) {
                int availableAmount = cursor.getInt(cursor.getColumnIndex("amount"));
                return availableAmount >= amount;
            }
        } catch (Exception e) {
            // Handle any exceptions (e.g., database query failure)
            Log.e("Database", "Error checking amount", e);
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            myDB.close();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public void saveBooking(String username, String tourName, int people, double total) {
        SQLiteDatabase Databasename = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = Databasename.rawQuery("SELECT users.ID AS user_id, tours.ID AS tour_id FROM users JOIN tours ON users.username = ? AND tours.tour_name = ?", new String[]{username, tourName});
        while (cursor.moveToNext()) {
            String ID_userName = cursor.getString(cursor.getColumnIndex("user_id"));
            String ID_tourName = cursor.getString(cursor.getColumnIndex("tour_id"));
            contentValues.put("ID_tours", ID_tourName);
            contentValues.put("ID_users", ID_userName);
            contentValues.put("bill_date", java.time.LocalDate.now().toString());
            contentValues.put("bill_money", total);
            contentValues.put("amount", people);
            Databasename.insert("bill", null, contentValues);
            Databasename.execSQL("UPDATE tours SET amount = amount - ? WHERE ID = ?", new Object[]{people, ID_tourName});
        }
    }


    public void deleteBooked(String username, String tourName) {
        SQLiteDatabase Databasename = this.getWritableDatabase();
        Databasename.execSQL("DELETE FROM bill WHERE ID_users = (SELECT ID FROM users WHERE username = ?) AND ID_tours = (SELECT ID FROM tours WHERE tour_name = ?)", new String[]{username, tourName});
    }


    @SuppressLint("Range")
    public ArrayList<String> getBooked(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursorBill = db.rawQuery("SELECT * FROM bill WHERE ID_users = (SELECT ID FROM users WHERE username = ?)", new String[]{username});
        while (cursorBill.moveToNext()) {
            String billDate = cursorBill.getString(cursorBill.getColumnIndex("bill_date"));
            String billMoney = cursorBill.getString(cursorBill.getColumnIndex("bill_money"));
            String billAmount = cursorBill.getString(cursorBill.getColumnIndex("amount"));
            String tourName = null,tourStart = null,tourId = null;
            Cursor cursorTour = db.rawQuery("SELECT * FROM tours WHERE ID = ?", new String[]{cursorBill.getString(cursorBill.getColumnIndex("ID_tours"))});
            if (cursorTour.moveToFirst()) {
                tourName = cursorTour.getString(cursorTour.getColumnIndex("tour_name"));
                tourStart = cursorTour.getString(cursorTour.getColumnIndex("start_date"));
            }
            String tourItem =
                     tourName + "\n"
                    + billDate + "\n"
                    + "- Amount: " + billAmount + "people" +"\n"
                    + "- Total: " + billMoney + "$" + "\n"
                    + "- Start Date: " + tourStart + "\n";

            list.add(tourItem);
            cursorTour.close();
        }
        cursorBill.close();
        return list;
    }


    public void WriteFeedBack(String username, String tourName, String feedback){
        SQLiteDatabase Databasename = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = Databasename.rawQuery("SELECT users.ID AS user_id, tours.ID AS tour_id FROM users JOIN tours ON users.username = ? AND tours.tour_name = ?", new String[]{username, tourName});
        while (cursor.moveToNext()){
            String ID_userName = cursor.getString(cursor.getColumnIndex("user_id"));
            String ID_tourName = cursor.getString(cursor.getColumnIndex("tour_id"));
            contentValues.put("ID_tours", ID_tourName);
            contentValues.put("ID_users", ID_userName);
            contentValues.put("date_feedback", java.time.LocalDate.now().toString());
            contentValues.put("feedback", feedback);
            Databasename.insert("feedback", null, contentValues);
        }
    }


    public void setUser(String user) {
        DatabaseHelper.user = user;
        System.out.println("User set to: " + this.user);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE ID = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateUser(int userId, String username, String password, String role, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", hashedPassword);
        values.put("role", role);
        values.put("phone", phone);
        int rowsAffected = db.update("users", values, "ID = ?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }

    public boolean addUser(String username, String password, int role, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", hashedPassword);
        values.put("role", role);
        values.put("phone", phone);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            int affectedRows = db.delete("users", "ID = ?", new String[]{String.valueOf(id)});
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
            }

        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllDestinations() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM destinations", null);
    }

    public Cursor getDestinationById(int destinationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM destinations WHERE ID = ?", new String[]{String.valueOf(destinationId)});
    }

    public boolean updateDestination(int destinationId, String destination, String description, double price, String pic1, String pic2, String pic3, String pic4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination", destination);
        values.put("description", description);
        values.put("price", price);
        values.put("pic1", pic1);
        values.put("pic2", pic2);
        values.put("pic3", pic3);
        values.put("pic4", pic4);
        int rowsAffected = db.update("destinations", values, "ID = ?", new String[]{String.valueOf(destinationId)});
        return rowsAffected > 0;
    }

    public boolean addDestination(String destination, String description, double price, String pic1, String pic2, String pic3, String pic4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination", destination);
        values.put("description", description);
        values.put("price", price);
        values.put("pic1", pic1);
        values.put("pic2", pic2);
        values.put("pic3", pic3);
        values.put("pic4", pic4);
        long result = db.insert("destinations", null, values);
        return result != -1;
    }

    public void deleteDestination(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int affectedRows = db.delete("destinations", "ID = ?", new String[]{String.valueOf(id)});
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllTours() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM tours", null);
    }

    public Cursor getTourById(int tourId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tours WHERE ID = ?", new String[]{String.valueOf(tourId)});
    }

    public boolean updateTour(int tourId, int destinationId, String startDate, String endDate, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_destination", destinationId);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("amount", amount);
        int rowsAffected = db.update("tours", values, "ID = ?", new String[]{String.valueOf(tourId)});
        return rowsAffected > 0;
    }

    public boolean addTour(int destinationId, String startDate, String endDate, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_destination", destinationId);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("amount", amount);
        long result = db.insert("tours", null, values);
        return result != -1;
    }

    public void deleteTour(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int affectedRows = db.delete("tours", "ID = ?", new String[]{String.valueOf(id)});
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllBills() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM bill", null);
    }

    public Cursor getBillById(int billId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM bill WHERE ID = ?", new String[]{String.valueOf(billId)});
    }

    public boolean updateBill(int billId, int tourId, int userId, String billDate, int billMoney, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_tours", tourId);
        values.put("ID_users", userId);
        values.put("bill_date", billDate);
        values.put("bill_money", billMoney);
        values.put("amount", amount);
        int rowsAffected = db.update("bill", values, "ID = ?", new String[]{String.valueOf(billId)});
        return rowsAffected > 0;
    }

    public boolean addBill(int tourId, int userId, String billDate, int billMoney, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_tours", tourId);
        values.put("ID_users", userId);
        values.put("bill_date", billDate);
        values.put("bill_money", billMoney);
        values.put("amount", amount);
        long result = db.insert("bill", null, values);
        return result != -1;
    }

    public void deleteBill(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int affectedRows = db.delete("bill", "ID = ?", new String[]{String.valueOf(id)});
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllFeedbacks() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM feedback", null);
    }

    public Cursor getFeedbackById(int feedbackId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM feedback WHERE ID = ?", new String[]{String.valueOf(feedbackId)});
    }

    public boolean updateFeedback(int feedbackId, int userId, int destinationId, String feedback, String dateFeedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_users", userId);
        values.put("ID_destination", destinationId);
        values.put("feedback", feedback);
        values.put("date_feedback", dateFeedback);
        int rowsAffected = db.update("feedback", values, "ID = ?", new String[]{String.valueOf(feedbackId)});
        return rowsAffected > 0;
    }

    public boolean addFeedback(int userId, int destinationId, String feedback, String dateFeedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_users", userId);
        values.put("ID_destination", destinationId);
        values.put("feedback", feedback);
        values.put("date_feedback", dateFeedback);
        long result = db.insert("feedback", null, values);
        return result != -1;
    }

    public void deleteFeedback(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int affectedRows = db.delete("feedback", "ID = ?", new String[]{String.valueOf(id)});
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Method to get all bills sorted by User ID
    public Cursor getBillsSortedByUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bill WHERE isDeleted = 0 ORDER BY ID_users ASC";
        return db.rawQuery(query, null);
    }

//    // Method to get all bills sorted by Date
//    public Cursor getBillsSortedByDate() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM bill WHERE isDeleted = 0 ORDER BY bill_date ASC";
//        return db.rawQuery(query, null);
//    }
    public Cursor getBillsSortedByDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bill WHERE isDeleted = 0 ORDER BY CAST(SUBSTR(bill_date, INSTR(bill_date, '/') + 1) AS INTEGER), CAST(SUBSTR(bill_date, 1, INSTR(bill_date, '/') - 1) AS INTEGER)";
        return db.rawQuery(query, null);
    }


    // Method to get all bills sorted by Total Cost (assuming 'amount' represents this)
    public Cursor getBillsSortedByTotalCost() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bill WHERE isDeleted = 0 ORDER BY bill_money DESC";
        return db.rawQuery(query, null);
    }

}
