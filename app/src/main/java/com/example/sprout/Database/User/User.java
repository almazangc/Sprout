package com.example.sprout.Database.User;

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

    @ColumnInfo(name = "eula")
    public boolean agreed;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getWake_hour() {
        return wake_hour;
    }

    public void setWake_hour(int wake_hour) {
        this.wake_hour = wake_hour;
    }

    public int getWake_minute() {
        return wake_minute;
    }

    public void setWake_minute(int wake_minute) {
        this.wake_minute = wake_minute;
    }

    public int getSleep_hour() {
        return sleep_hour;
    }

    public void setSleep_hour(int sleep_hour) {
        this.sleep_hour = sleep_hour;
    }

    public int getSleep_minute() {
        return sleep_minute;
    }

    public void setSleep_minute(int sleep_minute) {
        this.sleep_minute = sleep_minute;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }
}
