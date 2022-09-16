package com.example.sprout.database.User;

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

    @Insert
    void insert(User... users);

    @Update
    void update(User users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAllUser();
}
