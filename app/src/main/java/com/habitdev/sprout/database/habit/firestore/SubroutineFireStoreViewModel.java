package com.habitdev.sprout.database.habit.firestore;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.habitdev.sprout.database.habit.model.firestore.SubroutineFireStore;

import java.util.List;

public class SubroutineFireStoreViewModel extends AndroidViewModel {

    private SubroutineFireStoreRepository repository;
    private MutableLiveData<List<SubroutineFireStore>> liveData;
    private List<SubroutineFireStore> data;
    private final String TAG = "tag";

    public SubroutineFireStoreViewModel(@NonNull Application application) {
        super(application);
        repository = new SubroutineFireStoreRepository();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<SubroutineFireStore>> getLiveData() {
        fetchHabit();
        return liveData;
    }

    public List<SubroutineFireStore> getData() {
        return data;
    }

    public void setData(List<SubroutineFireStore> data) {
        this.data = data;
    }

    public void fetchHabit() {
        repository.fetchData(new SubroutineFireStoreRepository.FetchCallback() {
            @Override
            public void onFetchSubroutineSuccess(List<SubroutineFireStore> subroutine) {
                //Handle Success
                liveData.setValue(subroutine);
                setData(subroutine);
            }

            @Override
            public void onFetchSubroutineFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onFetchHabitFailure: " + e.getMessage());
            }
        });
    }

    public void insertSubroutine(SubroutineFireStore subroutine) {
        repository.insertSubroutine(subroutine, new SubroutineFireStoreRepository.InsertCallback() {
            @Override
            public void onInsertSubroutineSuccess(DocumentReference documentReference) {
                //Handle Success

            }

            @Override
            public void onInsertSubroutineFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onInsertHabitFailure: :" + e.getMessage());
            }
        });
    }

    public void updateSubroutine(SubroutineFireStore subroutine) {
        repository.updateSubroutine(subroutine, new SubroutineFireStoreRepository.UpdateCallback() {
            @Override
            public void onUpdateSubroutineSuccess() {
                // Handle success
            }

            @Override
            public void onUpdateSubroutineFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onUpdateHabitFailure: :" + e.getMessage());
            }
        });
    }

    public void deleteHabit(String id) {
        repository.deleteSubroutine(id, new SubroutineFireStoreRepository.DeleteCallback() {
            @Override
            public void onDeleteSubroutineSuccess() {
                // Handle success
            }

            @Override
            public void onDeleteSubroutineFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onDeleteHabitFailure: :" + e.getMessage());
            }
        });
    }
}
