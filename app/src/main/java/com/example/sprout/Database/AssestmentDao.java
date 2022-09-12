package com.example.sprout.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssestmentDao {
    @Query("SELECT * FROM assestment")
    public List<Assestment> getALLAssestment();

    @Query("SELECT * FROM assestment WHERE uid = :uid")
    public List<Assestment> getQuestionbyUID(int uid);

    @Query("UPDATE assestment SET selected = :selected WHERE uid = :uid")
    public void updateSelectedbyUID(int uid, String selected);

    @Insert
    void insertAssestment(Assestment... assestments);

    @Update
    void update(Assestment assestment);
    @Delete
    void deleteUser(Assestment assestment);
}
