package com.habitdev.sprout.database.quotes;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.ArrayList;
import java.util.List;

public class QuotesRepository {

    final FirebaseFirestore firestoreDB;
    final List<Quotes> quotesList;
    private final String QUOTES_COLLECTION;

    public QuotesRepository() {
        this.firestoreDB = FirebaseFirestore.getInstance();
        quotesList = new ArrayList<>();
        QUOTES_COLLECTION = "quotes";
    }

    public List<Quotes> getQuotesList() {
       return getQuotesListFromFireStore();
    }

    public List<Quotes> getQuotesListFromFireStore() {

        List<Quotes> qoutes = new ArrayList<>();

        firestoreDB.collection(QUOTES_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot != null) qoutes.add(documentSnapshot.toObject(Quotes.class));
                }
            }
        });
        return qoutes;
    }
}
