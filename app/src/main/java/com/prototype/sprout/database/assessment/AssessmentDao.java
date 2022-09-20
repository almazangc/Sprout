package com.prototype.sprout.database.assessment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM Assessment")
    LiveData<List<Assessment>> getALLAssessmentLiveData();

    @Query("SELECT * FROM Assessment")
    List<Assessment> getALLAssessment();

    @Query("UPDATE Assessment SET selected = :value WHERE uid = :uid")
    void updateSelectedUID(int uid, String value);

    @Query("SELECT selected FROM assessment WHERE uid = :uid")
    String getAssessmentSelected(int uid);

    @Insert
    void insert(Assessment... assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("DELETE FROM assessment")
    void deleteAllAssessment();
}
