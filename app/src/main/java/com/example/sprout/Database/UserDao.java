package com.example.sprout.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    public List<User> getAllUser();

    @Insert
    void insertUser(User... users);

    @Delete
    void deleteUser(User user);
}
