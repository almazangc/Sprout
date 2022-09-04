package com.example.sprout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBManager(Context c) {
        this.context = c;
    }

    public DBManager open() throws SQLException {
        this.dbHelper = new DBHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void insert(long _id, String username, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.USERNAME, username);
        contentValues.put(DBHelper.IDENTITY, identity);
        this.database.insert(DBHelper.TABLE_NAME_USER, null, contentValues);

//        long results = this.database.insert(DBHelper.TABLE_NAME_USER, null, contentValues);
//        if insert successful return true else false
//        return results != -1;
    }

    public Cursor fetch() {
        Cursor cursor = this.database.query(DBHelper.TABLE_NAME_USER, new String[]{DBHelper.USERNAME, DBHelper.IDENTITY}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String username, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.USERNAME, username);
        contentValues.put(DBHelper.IDENTITY, identity);
        return this.database.update(DBHelper.TABLE_NAME_USER, contentValues, "_id = " + _id, null);
    }

    public void delete(long _id) {
        this.database.delete(DBHelper.TABLE_NAME_USER, "_id=" + _id, null);
    }
}
