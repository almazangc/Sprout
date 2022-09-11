package com.example.sprout.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssestmentDao {
    @Query("SELECT * FROM assestment")
    public List<Assestment> getALLAssestment();

    @Query("SELECT * FROM assestment WHERE uid = :uid")
    public List<Assestment> getQuestionbyUID(int uid);

    @Insert
    void insertAssestment(Assestment... assestments);

    @Delete
    void deleteUser(Assestment assestment);
}
