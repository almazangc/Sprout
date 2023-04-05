package com.habitdev.sprout.database.user.model;

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
    @ColumnInfo(name = "streak_count")
    private int streakCount; //unused right now, can be used for keeping track of how many continous days the app was used.

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
    @ColumnInfo(name = "eula_agreement")
    private boolean eulaAgreement;

    /**
     * Checker for assessment finish
     */
    @ColumnInfo(name = "assessment_done")
    private boolean assessmentDone;

    /**
     * Checker for onboarding finish
     */
    @ColumnInfo(name = "onboarding_done")
    private boolean onBoardingDone;

    @ColumnInfo(name = "date_installed")
    private String dateInstalled;

    /**
     * Default Empty Constructor
     */
    @Ignore
    public User() {

    }

    public User(int uid, String nickname, String identity, int streakCount, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eulaAgreement, boolean assessmentDone, boolean onBoardingDone, String dateInstalled) {
        this.uid = uid;
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
        this.dateInstalled = dateInstalled;
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
     * @param dateInstalled installed date
     */
    @Ignore
    public User(String nickname, String identity, int streakCount, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eulaAgreement, boolean assessmentDone, boolean onBoardingDone, String dateInstalled) {
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
        this.dateInstalled = dateInstalled;
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
                ", dateInstalled='" + dateInstalled + '\'' +
                '}';
    }

    public int getUid() {
        return uid;
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

    public int getStreakCount() {
        return streakCount;
    }

    public int getWakeHour() {
        return wakeHour;
    }

    public int getWakeMinute() {
        return wakeMinute;
    }

    public int getSleepHour() {
        return sleepHour;
    }

    public int getSleepMinute() {
        return sleepMinute;
    }

    public boolean isEulaAgreement() {
        return eulaAgreement;
    }

    public boolean isAssessmentDone() {
        return assessmentDone;
    }

    public boolean isOnBoardingDone() {
        return onBoardingDone;
    }

    public String getDateInstalled() {
        return dateInstalled;
    }
}
