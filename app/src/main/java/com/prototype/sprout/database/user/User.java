package com.prototype.sprout.database.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * User Class Room Entity
 */
@Entity
public class User {
    /**
     * Auto Generated ID
     */
    @PrimaryKey(autoGenerate = true)
    private int uid;

    /**
     * User Nickname
     */
    @ColumnInfo(name = "nickname")
    private String nickname;

    /**
     * User Gender
     */
    @ColumnInfo(name = "identity")
    private String identity;

    /**
     * User App Streak Count
     */
    @ColumnInfo(name = "streakCount")
    private int streakCount;

    /**
     * User Wake Time Hour
     */
    @ColumnInfo(name = "wake_hour")
    private int wakeHour;

    /**
     * User Wake Time Minute
     */
    @ColumnInfo(name = "wake_minute")
    private int wakeMinute;

    /**
     * User Sleep Time Hour
     */
    @ColumnInfo(name = "sleep_hour")
    private int sleepHour;

    /**
     * User Sleep Time Minute
     */
    @ColumnInfo(name = "sleep_minute")
    private int sleepMinute;

    /**
     * User EULA Agreement
     */
    @ColumnInfo(name = "eula")
    private boolean eulaAgreement;

    /**
     * Checker for assessment finish
     */
    @ColumnInfo(name = "assessment")
    private boolean assessmentDone;

    /**
     * Checker for onboarding finish
     */
    @ColumnInfo(name = "onBoarding")
    private boolean onBoardingDone;

    /**
     * Default Empty Constructor
     */
    @Ignore
    public User() {

    }

    /**
     * New User Object Constructor
     * @param nickname user nickname
     * @param identity selected gender identity (male, female, non-binary)
     * @param streakCount total user streak count
     * @param wakeHour morning wakehour
     * @param wakeMinute morning wakeminute
     * @param sleepHour evening sleephour
     * @param sleepMinute evening sleepminute
     * @param eulaAgreement boolean value for user agreement to eula
     * @param assessmentDone boolean value for assessment completion checker
     * @param onBoardingDone boolean value for user onboarding completion
     */
    public User(String nickname, String identity, int streakCount, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eulaAgreement, boolean assessmentDone, boolean onBoardingDone) {
        this.nickname = nickname;
        this.identity = identity;
        this.streakCount = streakCount;
        this.wakeHour = wakeHour;
        this.wakeMinute = wakeMinute;
        this.sleepHour = sleepHour;
        this.sleepMinute = sleepMinute;
        this.eulaAgreement = eulaAgreement;
        this.assessmentDone = assessmentDone;
        this.onBoardingDone = onBoardingDone;
    }

    /**
     * Display User Object to String
     * @return String
     */
    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", nickname='" + nickname + '\'' +
                ", identity='" + identity + '\'' +
                ", streakCount=" + streakCount +
                ", wakeHour=" + wakeHour +
                ", wakeMinute=" + wakeMinute +
                ", sleepHour=" + sleepHour +
                ", sleepMinute=" + sleepMinute +
                ", eulaAgreement=" + eulaAgreement +
                ", assessmentDone=" + assessmentDone +
                ", onBoardingDone=" + onBoardingDone +
                '}';
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

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
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

    public boolean isOnBoardingDone() {
        return onBoardingDone;
    }

    public void setOnBoardingDone(boolean onBoardingDone) {
        this.onBoardingDone = onBoardingDone;
    }
}
