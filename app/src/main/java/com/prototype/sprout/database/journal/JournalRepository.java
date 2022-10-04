package com.prototype.sprout.database.journal;

import android.app.Application;

import com.prototype.sprout.database.AppDatabase;

public class JournalRepository {

    public JournalRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
    }

}
