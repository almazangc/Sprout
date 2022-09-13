package com.example.sprout.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    public List<User> getAllUser();

    @Insert
    void insert(User... users);

    @Update
    void update(User users);

    @Delete
    void delete(User user);

}
