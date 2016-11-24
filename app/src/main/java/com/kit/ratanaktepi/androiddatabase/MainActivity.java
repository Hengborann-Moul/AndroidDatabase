package com.kit.ratanaktepi.androiddatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SearchView.OnClickListener {

    EditText rollno, name, mark;
    Button add, delete, modify, view, viewall, about;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollno = (EditText) findViewById(R.id.rollno);
        name = (EditText) findViewById(R.id.name);
        mark = (EditText) findViewById(R.id.mark);

        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        modify = (Button) findViewById(R.id.modify);
        view = (Button) findViewById(R.id.view);
        viewall = (Button) findViewById(R.id.viewall);
        about = (Button) findViewById(R.id.about);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        modify.setOnClickListener(this);
        view.setOnClickListener(this);
        viewall.setOnClickListener(this);
        about.setOnClickListener(this);

        database = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");


    }

    @Override
    public void onClick(View v) {
        // Adding a record
        if (v == add) {
            // Checking empty fields
            if (rollno.getText().toString().trim().length() == 0 ||
                    name.getText().toString().trim().length() == 0 ||
                    mark.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter all values");
                return;
            }
            // Inserting record
            database.execSQL("INSERT INTO student VALUES('" + rollno.getText() + "','" + name.getText() + "','" + mark.getText() + "');");
            showMessage("Success", "Record added");
            clearText();
        }
        // Deleting a record
        if (v == delete) {
            // Checking empty roll number
            if (rollno.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            // Searching roll number
            Cursor c = database.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                // Deleting record if found
                database.execSQL("DELETE FROM student WHERE rollno='" + rollno.getText() + "'");
                showMessage("Success", "Record Deleted");
            } else {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        // Modifying a record
        if (v == modify) {
            // Checking empty roll number
            if (rollno.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            // Searching roll number
            Cursor c = database.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                // Modifying record if found
                database.execSQL("UPDATE student SET name='" + name.getText() + "',marks='" + mark.getText() +
                        "' WHERE rollno='" + rollno.getText() + "'");
                showMessage("Success", "Record Modified");
            } else {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        // Viewing a record
        if (v == view) {
            // Checking empty roll number
            if (rollno.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            // Searching roll number
            Cursor c = database.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                // Displaying record if found
                Log.d("Main", c.getString(0));
                name.setText(c.getString(1));
                mark.setText(c.getString(2));
            } else {
                showMessage("Error", "Invalid Rollno");
                clearText();
            }
        }
        // Viewing all records
        if (v == viewall) {
            // Retrieving all records
            Cursor c = database.rawQuery("SELECT * FROM student", null);
            // Checking if no records found
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            // Appending records to a string buffer
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("Rollno: " + c.getString(0) + "\n");
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Marks: " + c.getString(2) + "\n\n");
            }
            // Displaying all records
            showMessage("Student Details", buffer.toString());
        }
        // Displaying info
        if (v == about) {
            showMessage("Student Management Application", "Developed By Ratanaktepi Chhor");
        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        rollno.setText("");
        name.setText("");
        modify.setText("");
        rollno.requestFocus();
    }

}
