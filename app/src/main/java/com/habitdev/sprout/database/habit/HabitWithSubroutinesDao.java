package com.habitdev.sprout.database.habit;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

@Dao
public interface HabitWithSubroutinesDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHabit(Habits habits);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubroutines(List<Subroutines> subroutines);

    @Query("SELECT * FROM Habits WHERE on_reform = 1")
    List<Habits> getAllHabitOnReform();

    @Query("SELECT * FROM Habits WHERE on_reform = 1")
    LiveData<List<Habits>> getAllHabitOnReformLiveData();

    @Transaction
    @Query("SELECT * FROM Habits INNER JOIN Subroutine ON Habits.pk_habit_uid = Subroutine.fk_habit_uid WHERE Habits.on_reform = 1")
    List<HabitWithSubroutines> getAllHabitsOnReformWithSubroutines();

    @Transaction
    @Query("SELECT * FROM Habits INNER JOIN Subroutine ON Habits.pk_habit_uid = Subroutine.fk_habit_uid WHERE Habits.on_reform = 1")
    LiveData<List<HabitWithSubroutines>> getAllHabitsOnReformWithSubroutinesLiveData();

    @Query("SELECT pk_habit_uid FROM Habits WHERE Habits.on_reform = 1")
    List<Long> getAllHabitsOnReformUID();

    @Query("SELECT * FROM Subroutine WHERE fk_habit_uid=:uid")
    List<Subroutines> getAllSubroutinesOfHabit(long uid);

    @Query("SELECT * FROM Subroutine WHERE fk_habit_uid=:uid")
    LiveData<List<Subroutines>> getAllSubroutinesOfHabitLiveData(long uid);

    @Query("SELECT habit FROM Habits")
    List<String> getAllHabitTitle();

    @Query("SELECT * FROM Habits")
    List<Habits> getAllHabit();

    @Query("SELECT COUNT(*) FROM Habits WHERE Habits.on_reform = 1")
    LiveData<Long> getHabitOnReformCount();

    @Query("UPDATE Habits SET on_reform=:bool WHERE pk_habit_uid=:uid")
    void updateOnReformStatus(boolean bool, long uid);

//    @Query("SELECT h.pk_habit_uid as pk_habitUID, s.fk_habit_uid as fk_habitUID, h.on_reform, h.habit, h.`desc`, s.pk_subroutine_uid as subroutineUID ,s.routine as routineTitle, s.description as routineDescription, s.total_streak, s.isMarkedDone FROM Habits AS h INNER JOIN Subroutine AS s ON h.pk_habit_uid = s.fk_habit_uid WHERE h.on_reform = 1 ORDER BY h.pk_habit_uid")
//    LiveData<List<HabitWithSubroutines>> getAllHabitsOnReformWithSubroutinesLiveData();

    @Update
    void updateHabit(Habits habit);
//
//    @Update
//    void updateSubroutine(Subroutines subroutine);
//
//    @Delete
//    long deleteHabit(Habits habit);

//    @Delete
//    void deleteSubroutine(Subroutines subroutine);
}
