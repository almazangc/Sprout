package com.habitdev.sprout.database.qoutes;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QoutesRepository {

    FirebaseFirestore firestoreDB;

    public QoutesRepository() {
        this.firestoreDB = FirebaseFirestore.getInstance();
    }

    public List<Qoutes> getQoutesList() {

        List<Qoutes> qoutes = new ArrayList<>();

        firestoreDB.collection("quotes")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documents) {
                    if (documents != null){
                        qoutes.addAll(documents.toObjects(Qoutes.class));
                    } else {
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("tag", "onFailure Exception: " + e);
                }
            });
        return qoutes;
    }
}
