package com.example.sprout.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "nickname")
    public String nickname;

    @ColumnInfo(name = "identity")
    public String identity;

    @ColumnInfo(name = "wake_hour")
    public int wake_hour;

    @ColumnInfo(name = "wake_minute")
    public int wake_minute;

    @ColumnInfo(name = "sleep_hour")
    public int sleep_hour;

    @ColumnInfo(name = "sleep_minute")
    public int sleep_minute;
}
