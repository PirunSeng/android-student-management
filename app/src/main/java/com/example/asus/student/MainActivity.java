package com.example.asus.student;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{
    private Button btnAdd, btnView, btnViewAll, btnDelete, btnUpdate;
    private EditText rollOnVal, nameVal, subjectVal, scoresVal;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnView = (Button)findViewById(R.id.btnView);
        btnViewAll = (Button)findViewById(R.id.btnViewAll);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);

        rollOnVal = (EditText)findViewById(R.id.rollOnVal);
        nameVal = (EditText)findViewById(R.id.nameVal);
        subjectVal = (EditText)findViewById(R.id.subjectVal);
        scoresVal = (EditText)findViewById(R.id.scoresVal);

        db=openOrCreateDatabase("StudentInfoDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student (rollon int primary key, stu_name varchar, subject varchar, score double)");

        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAdd){
            if(rollOnVal.getText().toString().equals("0")){
                showMessage("Error", "Zero is invalid for" + " input to database");
            }else{
                if(rollOnVal.getText().toString().equals("") || nameVal.getText().toString().equals("") ||
                        subjectVal.getText().toString().equals("") || scoresVal.getText().toString().equals("")){
                    showMessage("Error", "Please enter all values");
                    return;
                }
                Cursor c = db.rawQuery("Select rollon From student " + "where rollon='" +
                        rollOnVal.getText().toString() + "'", null);

                if (c.moveToFirst()) {
                    showMessage("Error", "Record already exists");
                    clearText();
                }else{
                    db.execSQL("INSERT INTO student VALUES(" + rollOnVal.getText() + ",'" + nameVal.getText() + "','" +
                    subjectVal.getText() + "'," + scoresVal.getText() + ");");
                    showMessage("Successful", "Record added");
                    clearText();
                }
            }
        }

        if(view == btnView){
            if(rollOnVal.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student where rollon = '" + rollOnVal.getText() + "'", null);
            if(c.moveToFirst()){
                nameVal.setText(c.getString(1));
                subjectVal.setText(c.getString(2));
                scoresVal.setText(c.getString(3));
            }else{
                showMessage("Error", "Invalid Rollon");
                clearText();
            }
        }

        if(view == btnUpdate){
            if(rollOnVal.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Rolln for Update");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student WHERE "+ "rollon='" + rollOnVal.getText() + "'", null);
            if(c.moveToFirst()){
                db.execSQL("UPDATE student SET stu_name='" + nameVal.getText() + "'" + ", subject='" + subjectVal.getText() +
                "', score = '" + scoresVal.getText() + "'" +
                "WHERE rollon='"+ rollOnVal.getText() + "'");
                showMessage("Successfully Updated", "Record was updated");
            }else{
                showMessage("Error", "Invalid Rollon");
                clearText();
            }
        }

        if(view == btnViewAll){
            Cursor c = db.rawQuery("SELECT * FROM student", null);
            if(c.getCount() == 0){
                showMessage("Error", "No Records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while((c.moveToNext())){
                buffer.append("Rollon: " + c.getString(0) + "\n");
                buffer.append("StudentName: " + c.getString(1) + "\n");
                buffer.append("Subject: " + c.getString(2) + "\n");
                buffer.append("Marks: " + c.getString(3) + "\n\n\n");
            }
            showMessage("Student Information", buffer.toString());
        }

        if(view == btnDelete){
            if(rollOnVal.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Rolln for Delete");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student WHERE "+ "rollon='" + rollOnVal.getText() + "'", null);
            if(c.moveToFirst()){
                db.execSQL("DELETE FROM student WHERE rollon='"+ rollOnVal.getText() + "'");
                showMessage("Successfully", "Record was deleted");
            }else{
                showMessage("Error", "Invalid Rollon");
                clearText();
            }
        }
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText(){
        rollOnVal.setText("");
        nameVal.setText("");
        subjectVal.setText("");
        scoresVal.setText("");
    }
}
