package com.habitdev.sprout.database.user;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.user.model.User;

import java.util.List;

public class UserRepository {
    private final UserDao userDao;
    private final List<User> userList;
    private final LiveData<List<User>> userListLiveData;
    private final int userCount;
    private final boolean isAssessment;
    private final boolean isOnBoarding;
    private final LiveData<String> userNickname;

    public UserRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        userDao = appDatabase.userDao();
        userList = userDao.getAllUser();
        userListLiveData = userDao.getAllUserLiveData();
        userCount = userDao.countAllSections();
        isAssessment = userDao.isAssessment();
        isOnBoarding = userDao.isOnBoarding();
        userNickname = userDao.getNickname();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public void deleteAll() {
        new DeleteAllUserAsyncTask(userDao).execute();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public User getUserByUID(long num){
        return userDao.getUserByUID(num);
    }

    public List<User> getUserList() {
        return userList;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setAssessment() {
        userDao.setUserAssessmentTrue();
    }

    public boolean getAssessment() {
        return isAssessment;
    }

    public void setOnBoarding() {
        userDao.setUserOnBoardingTrue();
    }

    public boolean getOnBoarding() {
        return isOnBoarding;
    }

    public LiveData<String> getUserNickname() {
        return userNickname;
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private final UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private final UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {

        private final UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

    private static class DeleteAllUserAsyncTask extends AsyncTask<Void, Void, Void> {

        private final UserDao userDao;

        private DeleteAllUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUser();
            return null;
        }
    }
}
