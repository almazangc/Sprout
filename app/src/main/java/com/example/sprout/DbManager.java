package com.example.sprout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DbManager(Context c) {
        this.context = c;
    }

    public DbManager open() throws SQLException {
        this.dbHelper = new DbHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void insert(String username, String identity, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.USERNAME, username);
        contentValues.put(DbHelper.IDENTITY, identity);
        contentValues.put(DbHelper.WAKEHOUR, wakeHour);
        contentValues.put(DbHelper.WAKEMINUTE, wakeMinute);
        contentValues.put(DbHelper.SLEEPHOUR, sleepHour);
        contentValues.put(DbHelper.SLEEPMINUTE, sleepMinute);
        this.database.insert(DbHelper.TABLE_NAME_USER, null, contentValues);
    }

    public Cursor fetch() {
        Cursor cursor = this.database.query(DbHelper.TABLE_NAME_USER, new String[]{DbHelper.USERNAME, DbHelper.IDENTITY}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(int _id, String username, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.USERNAME, username);
        contentValues.put(DbHelper.IDENTITY, identity);
        return this.database.update(DbHelper.TABLE_NAME_USER, contentValues, "id=" + _id, null);
    }

    public void delete(int _id) {
        this.database.delete(DbHelper.TABLE_NAME_USER, "id=" + _id, null);
    }
}
