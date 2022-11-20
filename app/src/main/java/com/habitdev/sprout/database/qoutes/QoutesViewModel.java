package com.habitdev.sprout.database.qoutes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QoutesViewModel extends AndroidViewModel {

    FirebaseFirestore firebaseFirestore;
    QoutesRepository repository;
    List<Qoutes> qoutesList;

    public QoutesViewModel(@NonNull Application application) {
        super(application);
        firebaseFirestore = FirebaseFirestore.getInstance();
        repository = new QoutesRepository();
        qoutesList = repository.getQoutesList();
    }

    public List<Qoutes> getQoutesList() {
        return qoutesList;
    }
}
