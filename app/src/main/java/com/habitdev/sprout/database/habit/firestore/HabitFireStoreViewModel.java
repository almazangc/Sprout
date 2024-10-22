package com.habitdev.sprout.database.habit.firestore;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;

import java.util.List;

/**
 * Unused for now
 */
public class HabitFireStoreViewModel extends AndroidViewModel {

    private final HabitFireStoreRepository repository;
    private final MutableLiveData<List<HabitFireStore>> liveData;
    private List<HabitFireStore> data;
    private final String TAG = "tag";

    public HabitFireStoreViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitFireStoreRepository();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<HabitFireStore>> getLiveData() {
        fetchData();
        return liveData;
    }

    public List<HabitFireStore> getData() {
        fetchData();
        return data;
    }

    public void setData(List<HabitFireStore> data) {
        this.data = data;
    }

    public void fetchData() {
        repository.fetchData(new HabitFireStoreRepository.FetchCallback() {
            @Override
            public void onFetchHabitSuccess(List<HabitFireStore> habit) {
                HabitFireStoreViewModel.this.liveData.setValue(habit);
                HabitFireStoreViewModel.this.setData(habit);
                Log.d(TAG, "onFetchHabitSuccess: ");
            }

            @Override
            public void onFetchHabitFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onFetchHabitFailure: " + e.getMessage());
            }
        });
    }

    public void insertHabit(HabitFireStore habit) {
        repository.insertHabit(habit, new HabitFireStoreRepository.InsertCallback() {
            @Override
            public void onInsertHabitSuccess(DocumentReference documentReference) {
                // Handle success
            }

            @Override
            public void onInsertHabitFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onInsertHabitFailure: :" + e.getMessage());
            }
        });
    }

    public void updateHabit(HabitFireStore habit) {
        repository.updateHabit(habit, new HabitFireStoreRepository.UpdateCallback() {
            @Override
            public void onUpdateHabitSuccess() {
                // Handle success
            }

            @Override
            public void onUpdateHabitFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onUpdateHabitFailure: :" + e.getMessage());
            }
        });
    }

    public void deleteHabit(String id) {
        repository.deleteHabit(id, new HabitFireStoreRepository.DeleteCallback() {
            @Override
            public void onDeleteHabitSuccess() {
                // Handle success
            }

            @Override
            public void onDeleteHabitFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onDeleteHabitFailure: :" + e.getMessage());
            }
        });
    }
}
