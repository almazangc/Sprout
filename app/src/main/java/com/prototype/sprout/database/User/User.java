package com.prototype.sprout.database.User;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "nickname")
    private String nickname;

    @ColumnInfo(name = "identity")
    private String identity;

    @ColumnInfo(name = "wake_hour")
    private int wakeHour;

    @ColumnInfo(name = "wake_minute")
    private int wakeMinute;

    @ColumnInfo(name = "sleep_hour")
    private int sleepHour;

    @ColumnInfo(name = "sleep_minute")
    private int sleepMinute;

    @ColumnInfo(name = "eula")
    private boolean eulaAgreement;

    @ColumnInfo(name = "assessment")
    private boolean assessmentDone;

    @ColumnInfo(name = "onBoarding")
    private boolean onBoardingDone;

    public boolean isOnBoardingDone() {
        return onBoardingDone;
    }

    public void setOnBoardingDone(boolean onBoardingDone) {
        this.onBoardingDone = onBoardingDone;
    }

    public User() {

    }

    public User(String nickname, String identity, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eulaAgreement) {
        this.nickname = nickname;
        this.identity = identity;
        this.wakeHour = wakeHour;
        this.wakeMinute = wakeMinute;
        this.sleepHour = sleepHour;
        this.sleepMinute = sleepMinute;
        this.eulaAgreement = eulaAgreement;
    }

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

    public int getWakeHour() {
        return wakeHour;
    }

    public void setWakeHour(int wakeHour) {
        this.wakeHour = wakeHour;
    }

    public int getWakeMinute() {
        return wakeMinute;
    }

    public void setWakeMinute(int wakeMinute) {
        this.wakeMinute = wakeMinute;
    }

    public int getSleepHour() {
        return sleepHour;
    }

    public void setSleepHour(int sleepHour) {
        this.sleepHour = sleepHour;
    }

    public int getSleepMinute() {
        return sleepMinute;
    }

    public void setSleepMinute(int sleepMinute) {
        this.sleepMinute = sleepMinute;
    }

    public boolean isEulaAgreement() {
        return eulaAgreement;
    }

    public void setEulaAgreement(boolean eulaAgreement) {
        this.eulaAgreement = eulaAgreement;
    }

    public boolean isAssessmentDone() {
        return assessmentDone;
    }

    public void setAssessmentDone(boolean assessmentDone) {
        this.assessmentDone = assessmentDone;
    }
}
