package com.prototype.sprout.database.habit.sub_routine;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prototype.sprout.database.AppDatabase;

import java.util.List;

public class RoutineRepository {

    private RoutineDao routineDao;
    private List<Routine> allRoutineList;
    private List<String> getRoutines;
    private LiveData<List<Routine>> allRoutineListLiveData;

    public RoutineRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        routineDao = appDatabase.routineDao();
        allRoutineList = routineDao.getAllRoutine();
        getRoutines = routineDao.getRoutine();
        allRoutineListLiveData = routineDao.getAllRoutineLiveData();
    }

    public void insert(Routine routine) {
        new RoutineRepository.InsertRoutinesAsyncTask(routineDao).execute(routine);
    }

    public void update(Routine routine) {
        new RoutineRepository.UpdateRoutinesAsyncTask(routineDao).execute(routine);
    }

    public void delete(Routine routine) {
        new RoutineRepository.DeleteRoutinessAsyncTask(routineDao).execute(routine);
    }

    public void deleteAll() {
        new RoutineRepository.DeleteAllRoutinesAsyncTask(routineDao).execute();
    }

    public RoutineDao getRoutineDao() {
        return routineDao;
    }

    public List<Routine> getAllRoutineList() {
        return allRoutineList;
    }

    public List<String> getGetRoutines() {
        return getRoutines;
    }

    public LiveData<List<Routine>> getAllRoutineListLiveData() {
        return allRoutineListLiveData;
    }

    public List<Routine> getRoutine(int uid){
        return routineDao.getRoutine(uid);
    }

    private static class InsertRoutinesAsyncTask extends AsyncTask<Routine, Void, Void> {

        private RoutineDao routineDao;

        private InsertRoutinesAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.insert(routines[0]);
            return null;
        }
    }

    private static class UpdateRoutinesAsyncTask extends AsyncTask<Routine, Void, Void> {

        private RoutineDao routineDao;

        private UpdateRoutinesAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.update(routines[0]);
            return null;
        }
    }

    private static class DeleteRoutinessAsyncTask extends AsyncTask<Routine, Void, Void> {

        private RoutineDao routineDao;

        private DeleteRoutinessAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.delete(routines[0]);
            return null;
        }
    }

    private static class DeleteAllRoutinesAsyncTask extends AsyncTask<Void, Void, Void> {

        private RoutineDao routineDao;

        private DeleteAllRoutinesAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            routineDao.deleteAllRoutines();
            return null;
        }
    }
}
