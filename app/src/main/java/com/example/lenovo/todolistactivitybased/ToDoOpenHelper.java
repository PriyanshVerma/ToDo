package com.example.lenovo.todolistactivitybased;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DB_NAME = "todo_db";

    public ToDoOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String toDoSQL = "CREATE TABLE todo ( id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)";

        db.execSQL(toDoSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
