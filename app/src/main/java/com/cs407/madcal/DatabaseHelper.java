package com.cs407.madcal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 7;

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

    // New table for classes
    private static final String TABLE_CLASSES = "classes";
    private static final String COLUMN_CLASS_NAME = "class_name";
    private static final String COLUMN_CLASS_DAYS = "class_days";
    private static final String COLUMN_CLASS_RANGE = "class_range";
    private static final String COLUMN_CLASS_WISC_ID = "wisc_id";
    private static final String COLUMN_CLASS_ID = "class_id";


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

    // SQL query to create the classes table
    private static final String CREATE_CLASSES_TABLE = "CREATE TABLE " + TABLE_CLASSES + "("
            + COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CLASS_NAME + " TEXT,"
            + COLUMN_CLASS_DAYS + " TEXT,"
            + COLUMN_CLASS_RANGE + " TEXT,"
            + COLUMN_CLASS_WISC_ID + " TEXT" + ")";

    // SQL query to drop the classes table
    private static final String DROP_CLASSES_TABLE = "DROP TABLE IF EXISTS " + TABLE_CLASSES;

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
        db.execSQL(CREATE_CLASSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TASKS_TABLE);
        db.execSQL(DROP_CLASSES_TABLE);
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

    public void addClass(String className, String classDays, String classRange, String wiscId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, className);
        values.put(COLUMN_CLASS_DAYS, classDays);
        values.put(COLUMN_CLASS_RANGE, classRange);
        values.put(COLUMN_CLASS_WISC_ID, wiscId);

        db.insert(TABLE_CLASSES, null, values);
        db.close();
    }
    public List<String[]> getTasksByWiscId(String wiscId) {
        List<String[]> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Adjusted SQL query for ordering by date and time
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_WISC_ID + " = ? ORDER BY " + COLUMN_TASK_DATE + " ASC, " + COLUMN_TASK_TIME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { wiscId });

        if (cursor.moveToFirst()) {
            do {
                String taskId = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TITLE));
                String taskDate = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE));
                String taskTime = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TIME));
                taskList.add(new String[]{taskTitle + "\nDue on: " +
                        LocalDate.parse(taskDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                        + " at " + taskTime, taskId});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public List<String> getClassesByDay(String day, String wiscId) {
        List<ClassDetail> classDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        Cursor cursor = db.query(TABLE_CLASSES, new String[]{COLUMN_CLASS_NAME, COLUMN_CLASS_DAYS, COLUMN_CLASS_RANGE},
                COLUMN_CLASS_DAYS + " LIKE ? AND " + COLUMN_CLASS_WISC_ID + " = ?", new String[]{"%" + day + "%", wiscId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String className = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_NAME));
                String classDays = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_DAYS));
                String classRange = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_RANGE));

                String[] daysArray = classDays.split(",");
                String[] rangesArray = classRange.split(",");
                for (int i = 0; i < daysArray.length; i++) {
                    if (day.equals(daysArray[i])) {
                        String dayRange = rangesArray[i];
                        String startTimeStr = dayRange.split(" to ")[0];
                        try {
                            Date startTime = timeFormat.parse(startTimeStr);
                            classDetails.add(new ClassDetail(className, dayRange, startTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Collections.sort(classDetails, new Comparator<ClassDetail>() {
            @Override
            public int compare(ClassDetail c1, ClassDetail c2) {
                return c1.startTime.compareTo(c2.startTime);
            }
        });

        List<String> sortedClasses = new ArrayList<>();
        for (ClassDetail classDetail : classDetails) {
            sortedClasses.add(classDetail.className + ", From " + classDetail.dayRange);
        }

        return sortedClasses;
    }

    private static class ClassDetail {
        String className;
        String dayRange;
        Date startTime;

        public ClassDetail(String className, String dayRange, Date startTime) {
            this.className = className;
            this.dayRange = dayRange;
            this.startTime = startTime;
        }
    }
    public List<String[]> getClassesByWiscId(String wiscId) {
        List<String[]> classData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_CLASS_ID + ", " + COLUMN_CLASS_NAME +
                " FROM " + TABLE_CLASSES +
                " WHERE " + COLUMN_CLASS_WISC_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{wiscId});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_NAME));
                classData.add(new String[]{String.valueOf(id), name});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return classData;
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

    public void deleteClass(int classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLASSES, COLUMN_CLASS_ID + " = ?", new String[]{String.valueOf(classId)});
        db.close();
    }


}
