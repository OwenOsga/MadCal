package com.cs407.madcal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "wisc_id";

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_TITLE = "title";
    private static final String COLUMN_TASK_DATE = "date";
    private static final String COLUMN_TASK_TIME = "time";
    private static final String COLUMN_TASK_WISC_ID = "wisc_id";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // SQL query to create the tasks table
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TASK_TITLE + " TEXT,"
            + COLUMN_TASK_DATE + " TEXT,"
            + COLUMN_TASK_TIME + " TEXT,"
            + COLUMN_TASK_WISC_ID + " TEXT" + ")";

    // SQL query to drop the tasks table
    private static final String DROP_TASKS_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASKS;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TASKS_TABLE); // New table drop
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param wiscId
     */
    public void addUser(String wiscId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, wiscId);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addTask(String title, String date, String time, String wiscId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DATE, date);
        values.put(COLUMN_TASK_TIME, time);
        values.put(COLUMN_TASK_WISC_ID, wiscId);

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public List<String[]> getTasksByWiscId(String wiscId) {
        List<String[]> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_WISC_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { wiscId });

        if (cursor.moveToFirst()) {
            do {
                String taskId = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TITLE));
                String taskDate = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE));
                String taskTime = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TIME));
                taskList.add(new String[]{taskTitle+ "\nDue on: " + taskDate + " at " + taskTime, taskId});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public String[] getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] taskDetails = new String[4];

        Cursor cursor = db.query(TABLE_TASKS, new String[] {COLUMN_TASK_TITLE, COLUMN_TASK_DATE, COLUMN_TASK_TIME},
                COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TIME));

            taskDetails[0] = title;
            taskDetails[1] = date;
            taskDetails[2] = time;
            // You can add more details if needed

            cursor.close();
        }

        db.close();
        return taskDetails;
    }
    public void updateTask(int taskId, String title, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DATE, date);
        values.put(COLUMN_TASK_TIME, time);

        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }


    /**
     * This method is to check user exist or not
     *
     * @param wiscId
     * @return true/false
     */
    public boolean checkUser(String wiscId) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_ID + " = ?";

        // selection argument
        String[] selectionArgs = {wiscId};

        // query user table with condition
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method is to delete user record
     *
     * @param wiscId
     */
    public void deleteUser(String wiscId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{wiscId});
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }


}
