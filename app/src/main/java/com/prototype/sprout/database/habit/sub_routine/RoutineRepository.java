package com.prototype.sprout.database.habit.sub_routine;

import android.app.Application;

import com.prototype.sprout.database.AppDatabase;

public class RoutineRepository {

    public RoutineRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
    }
}
