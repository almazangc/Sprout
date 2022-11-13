package com.prototype.sprout.database.habit;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {

    @Query("SELECT * FROM Habit")
    List<Habit> getAllHabit();

    @Query("SELECT habit from Habit")
    List<String> getAllHabitTitle();

    @Query("SELECT * FROM Habit")
    LiveData<List<Habit>> getAllHabitLiveData();

    @Query("SELECT * FROM Habit WHERE uid =:uid")
    List<Habit> getAllHabitTitle(int uid);

    @Query("SELECT * FROM Habit WHERE on_reform = 1")
    List<Habit> getAllHabitOnReform();

    @Query("SELECT * FROM Habit WHERE on_reform = 1")
    LiveData<List<Habit>> getAllHabitOnReformLiveData();

    @Insert
    void insert(Habit... habits);

    @Update
    void update(Habit habit);

    @Delete
    void delete(Habit habit);

    @Query("DELETE FROM Habit")
    void deleteAllHabits();
}
