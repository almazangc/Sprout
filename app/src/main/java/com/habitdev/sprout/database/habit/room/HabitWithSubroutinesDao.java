package com.habitdev.sprout.database.habit.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;

import java.util.List;

@Dao
public interface HabitWithSubroutinesDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHabit(Habits habits);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubroutines(List<Subroutines> subroutines);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubroutine(Subroutines subroutine);

    @Query("SELECT * FROM HABITS WHERE pk_habit_uid=:uid")
    Habits getHabitByUID(long uid);

    @Query("SELECT * FROM Habits WHERE on_reform = 1")
    List<Habits> getAllHabitOnReform();

    @Query("SELECT * FROM Habits WHERE on_reform = 1")
    LiveData<List<Habits>> getAllHabitOnReformLiveData();

    @Query("SELECT COUNT(*) FROM Habits WHERE on_reform = 1")
    long getAllHabitOnReformCount();

    @Transaction
    @Query("SELECT * FROM Habits INNER JOIN subroutines ON Habits.pk_habit_uid = subroutines.fk_habit_uid WHERE Habits.on_reform = 1")
    List<HabitWithSubroutines> getAllHabitsOnReformWithSubroutines();

    @Transaction
    @Query("SELECT * FROM Habits INNER JOIN subroutines ON Habits.pk_habit_uid = subroutines.fk_habit_uid WHERE Habits.on_reform = 1")
    LiveData<List<HabitWithSubroutines>> getAllHabitsOnReformWithSubroutinesLiveData();

    @Query("SELECT pk_habit_uid FROM Habits WHERE Habits.on_reform = 1")
    List<Long> getAllHabitsOnReformUID();

    @Query("SELECT * FROM subroutines WHERE fk_habit_uid=:uid")
    List<Subroutines> getAllSubroutinesOfHabit(long uid);

    @Query("SELECT * FROM subroutines WHERE fk_habit_uid=:uid")
    LiveData<List<Subroutines>> getAllSubroutinesOfHabitLiveData(long uid);

    @Query("SELECT * FROM Habits")
    LiveData<List<Habits>> getAllHabitListLiveData();

    @Query("SELECT * FROM Habits")
    List<Habits> getAllHabitList();

    @Query("SELECT COUNT(*) FROM Habits WHERE Habits.on_reform = 1")
    LiveData<Long> getHabitOnReformCount();

    @Query("SELECT COUNT(*) FROM Habits WHERE Habits.on_reform = 1 and Habits.is_modifiable = 0")
    LiveData<Long> getPredefinedHabitOnReformCount();

    @Query("SELECT * FROM Habits WHERE Habits.is_modifiable = 1")
    LiveData<List<Habits>> getAllUserDefinedHabitListLiveData();

    @Query("SELECT SUM(completed_subroutines) FROM Habits")
    LiveData<Integer> getTotalCompletedSubroutineCountLiveData();

    @Update
    void updateHabit(Habits habit);

    @Update
    void updateSubroutine(Subroutines subroutine);

    @Delete
    void deleteHabit(Habits habit);

    @Delete
    void deleteSubroutine(Subroutines subroutine);

    @Delete(entity = Subroutines.class)
    void deleteSubroutineList(List<Subroutines> subroutine);
}
