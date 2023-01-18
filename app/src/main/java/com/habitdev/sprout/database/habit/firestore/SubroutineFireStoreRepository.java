package com.habitdev.sprout.database.habit.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.habitdev.sprout.database.habit.model.firestore.SubroutineFireStore;

import java.util.ArrayList;
import java.util.List;

public class SubroutineFireStoreRepository {
    private FirebaseFirestore firestore; // instance of the FirebaseFirestore class, used to access the Firestore database.
    private CollectionReference subroutineCollection; // CollectionReference object for the "subroutine" collection in Firestore.

    public interface FetchCallback {
        void onFetchSubroutineSuccess(List<SubroutineFireStore> subroutineFireStores);

        void onFetchSubroutineFailure(Exception e);
    }

    public interface InsertCallback {
        void onInsertSubroutineSuccess(DocumentReference documentReference);

        void onInsertSubroutineFailure(Exception e);
    }

    public interface UpdateCallback {
        void onUpdateSubroutineSuccess();

        void onUpdateSubroutineFailure(Exception e);
    }

    public interface DeleteCallback {
        void onDeleteSubroutineSuccess();

        void onDeleteSubroutineFailure(Exception e);
    }

    public SubroutineFireStoreRepository() {
        firestore = FirebaseFirestore.getInstance();
        subroutineCollection = firestore.collection("subroutine");
    }

    public void fetchData(final FetchCallback callback) {
        subroutineCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<SubroutineFireStore> subroutines = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            SubroutineFireStore subroutine = documentSnapshot.toObject(SubroutineFireStore.class); //
                            subroutine.setId(documentSnapshot.getId());
                            subroutines.add(subroutine);
                        }
                        callback.onFetchSubroutineSuccess(subroutines);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFetchSubroutineFailure(e);
                    }
                });
    }

    public void insertSubroutine(SubroutineFireStore subroutine, final InsertCallback callback) {
        subroutineCollection.add(subroutine.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onInsertSubroutineSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onInsertSubroutineFailure(e);
                    }
                });
    }

    public void updateSubroutine(SubroutineFireStore subroutine, final UpdateCallback callback) {
        subroutineCollection.document(subroutine.getId()).set(subroutine.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onUpdateSubroutineSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onUpdateSubroutineFailure(e);
                    }
                });
    }

    public void deleteSubroutine(String id, final DeleteCallback callback) {
        subroutineCollection.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onDeleteSubroutineSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onDeleteSubroutineFailure(e);
                    }
                });
    }
}
