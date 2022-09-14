package com.example.sprout.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sprout.database.Assestment.Assestment;
import com.example.sprout.database.Assestment.AssestmentDao;
import com.example.sprout.database.User.User;
import com.example.sprout.database.User.UserDao;

@Database(entities = {User.class, Assestment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract AssestmentDao assestmentDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
