package com.prototype.sprout.database.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAllUser();

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUserLiveData();

    @Query("SELECT COUNT(*) FROM user")
    int countAllSections();

    @Query("UPDATE user SET assessment = 1 WHERE uid = 1")
    void setUserAssessmentTrue();

    @Query("SELECT assessment FROM user WHERE uid = 1")
    boolean isAssessment();

    @Query("UPDATE user SET onBoarding = 1 WHERE uid = 1")
    void setUserOnBoardingTrue();

    @Query("SELECT onBoarding FROM user WHERE uid = 1")
    boolean isOnBoarding();

    @Query("SELECT nickname FROM user WHERE uid = 1")
    LiveData<String> getNickname();

    @Insert
    void insert(User... users);

    @Update
    void update(User users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAllUser();
}
