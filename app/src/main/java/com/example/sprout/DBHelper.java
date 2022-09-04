package com.example.sprout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String USERDB = "user.db";
    private static final String CREATE_TABLE_USER = "create Table users(username TEXT primary key, identity TEXT)";
    public static final String USERNAME = "username";
    public static final String IDENTITY = "identity";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME_USER = "users";

    //    Constructor
    public DBHelper(Context context) {
        super(context, USERDB, null, DB_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//       Creating table for get user
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table  if exists users");
    }
}
