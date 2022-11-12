package com.prototype.sprout.database.sub_routine;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface RoutineDao {

    @Query("SELECT * FROM routine")
    List<Routine> getAllRoutine();

    @Query("SELECT routine from routine")
    List<String> getRoutine();

    @Query("SELECT * FROM routine")
    LiveData<List<Routine>> getAllRoutineLiveData();

    @Query("SELECT * FROM routine WHERE uid =:uid")
    List<Routine> getRoutine(int uid);

    @Insert
    void insert(Routine... routines);

    @Update
    void update(Routine routines);

    @Delete
    void delete(Routine routines);

    @Query("DELETE FROM routine")
    void deleteAllRoutines();
}
