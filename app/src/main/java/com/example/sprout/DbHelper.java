package com.example.sprout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String USERDB = "user.db";
    private static final String CREATE_TABLE_USER = "create Table users(id  INTEGER primary key AUTOINCREMENT, username TEXT, identity TEXT, wakeHour INTEGER, wakeMinute INTEGER, sleepHour INTEGER, sleepMinute INTEGER)";
    public static final String USERNAME = "username";
    public static final String IDENTITY = "identity";
    public static final String WAKEHOUR = "wakeHour";
    public static final String WAKEMINUTE = "wakeMinute";
    public static final String SLEEPHOUR = "sleepHour";
    public static final String SLEEPMINUTE = "sleepMinute";
    public static final String ISDONE = "isDone";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME_USER = "users";

    //    Constructor
    public DbHelper(Context context) {
        super(context, USERDB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table  if exists users");
    }
}
