package com.example.sprout.Database.Assestment;

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
    public List<Assestment> getQuestionUID(int uid);

    @Query("UPDATE assestment SET selected = :selected WHERE uid = :uid")
    public void updateSelectedUID(int uid, String selected);

    @Insert
    void insert(Assestment... assessment);

    @Update
    void update(Assestment assestment);

    @Delete
    void delete(Assestment assestment);
}
