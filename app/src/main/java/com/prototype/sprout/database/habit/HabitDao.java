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
    List<Habit> getAllHabits();

    @Query("SELECT habits from Habit")
    List<String> getHabits();

    @Query("SELECT * FROM Habit")
    LiveData<List<Habit>> getAllHabitsLiveData();

    @Query("SELECT * FROM Habit WHERE uid =:uid")
    List<Habit> getHabit(int uid);

    @Insert
    void insert(Habit... habits);

    @Update
    void update(Habit habit);

    @Delete
    void delete(Habit habit);

    @Query("DELETE FROM Habit")
    void deleteAllHabits();
}
