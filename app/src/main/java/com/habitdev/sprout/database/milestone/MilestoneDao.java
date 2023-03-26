package com.habitdev.sprout.database.milestone;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.habitdev.sprout.database.milestone.model.Milestone;

import java.util.List;

@Dao
public interface MilestoneDao {

    @Query("SELECT * FROM milestone ORDER BY name ASC")
    LiveData<List<Milestone>> getAllMilestonesLiveDataList();

    @Query("SELECT * FROM milestone ORDER BY name ASC")
    List<Milestone> getAllMilestonesList();

    @Query("SELECT * FROM milestone WHERE pk_milestone_uid = :pk_milestone_uid")
    LiveData<Milestone> getMilestoneByUID(long pk_milestone_uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Milestone milestone);

    @Update
    void update(Milestone milestone);

    @Delete
    void delete(Milestone milestone);
}
