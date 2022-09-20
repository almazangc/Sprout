package com.prototype.sprout.database.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> userListLiveData;
    private List<User> userList;
    private int userCount;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        userList = repository.getUserList();
        userListLiveData = repository.getUserListLiveData();
        userCount = repository.getUserCount();
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteAllUser() {
        repository.deleteAll();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public List<User> getUserList(){
        return userList;
    }

    public int getUserCount(){
        return userCount;
    }

    public void setAssessment(){
        repository.setAssessment();
    }

    public boolean getAssessment(){
        return repository.getAssessment();
    }

    public void setOnBoarding(){
        repository.setOnBoarding();
    }

    public boolean getOnBoarding(){
        return repository.getOnBoarding();
    }
}
