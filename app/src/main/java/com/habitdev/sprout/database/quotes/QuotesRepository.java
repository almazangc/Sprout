package com.habitdev.sprout.database.quotes;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuotesRepository {

    private final FirebaseFirestore firestore;
    private final QuotesViewModel quotesViewModel;
    private final CollectionReference quotesCollection;

    public QuotesRepository(QuotesViewModel quotesViewModel) {
        this.quotesViewModel = quotesViewModel;
        firestore = FirebaseFirestore.getInstance();
        quotesCollection = firestore.collection("quotes");
    }

    public void fetchData() {
        quotesCollection
                .get(
                        Source.DEFAULT
                )
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Quotes> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Quotes quotes = document.toObject(Quotes.class); // document id
                                quotes.setId(document.getId());
                                dataList.add(quotes);
                            }
                            quotesViewModel.setLiveData(dataList);
                            quotesViewModel.setData(dataList);
                        } else {
                            Log.d("tag", "onComplete: fetchdata failed due to network");
                        }
                    }
                });
    }

    public void updateQuotes(Quotes quotes) {
        quotesCollection.document(quotes.getId())
                .set(quotes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("QuotesRepository", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(" quotesRepository", "Error updating document", e);
                    }
                });
    }

    public void insert_quotes(String docID, Map<String, Object> quotes) {
//        firestore.collection("quotes")
//                .add(quotes)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(" quotesRepository", "DocumentSnapshot written with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(" quotesRepository", "Error adding document", e);
//                    }
//                });

        if (docID.isEmpty()) {
            firestore.collection("quotes").document().set(quotes)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(" quotesRepository", "DocumentSnapshot successfully inserted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(" quotesRepository", "Error inserting new document", e);
                        }
                    });
        } else {
            firestore.collection("quotes").document(docID).set(quotes)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(" quotesRepository", "DocumentSnapshot successfully inserted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(" quotesRepository", "Error inserting new document", e);
                        }
                    });
        }
    }

    public void delete_quotes(String id) {
        firestore.collection("quotes").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(" quotesRepository", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(" quotesRepository", "Error deleting document", e);
                    }
                });
    }
}
