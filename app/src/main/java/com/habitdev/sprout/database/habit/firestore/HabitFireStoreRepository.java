package com.habitdev.sprout.database.habit.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;

import java.util.ArrayList;
import java.util.List;

public class HabitFireStoreRepository {
    private FirebaseFirestore firestore; // instance of the FirebaseFirestore class, used to access the Firestore database.
    private CollectionReference habitCollection; // CollectionReference object for the "habit" collection in Firestore.

    public interface FetchCallback {
        void onFetchHabitSuccess(List<HabitFireStore> habitFireStoreList);
        void onFetchHabitFailure(Exception e);
    }

    public interface InsertCallback {
        void onInsertHabitSuccess(DocumentReference documentReference);
        void onInsertHabitFailure(Exception e);
    }

    public interface UpdateCallback {
        void onUpdateHabitSuccess();
        void onUpdateHabitFailure(Exception e);
    }

    public interface DeleteCallback {
        void onDeleteHabitSuccess();
        void onDeleteHabitFailure(Exception e);
    }

    public HabitFireStoreRepository() {
        firestore = FirebaseFirestore.getInstance();
        habitCollection = firestore.collection("habit");
    }

    public void fetchData(final FetchCallback callback) {
        habitCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<HabitFireStore> habits = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            HabitFireStore habit = documentSnapshot.toObject(HabitFireStore.class); //
                            habit.setId(documentSnapshot.getId());
                            habits.add(habit);
                        }
                        callback.onFetchHabitSuccess(habits);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFetchHabitFailure(e);
                    }
                });
    }

    public void insertHabit(HabitFireStore habit, final InsertCallback callback) {
        habitCollection.add(habit.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onInsertHabitSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onInsertHabitFailure(e);
                    }
                });
    }

    public void updateHabit(HabitFireStore habit, final UpdateCallback callback) {
        habitCollection.document(habit.getId()).set(habit.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onUpdateHabitSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onUpdateHabitFailure(e);
                    }
                });
    }

    public void deleteHabit(String id, final DeleteCallback callback) {
        habitCollection.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onDeleteHabitSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onDeleteHabitFailure(e);
                    }
                });
    }
}

