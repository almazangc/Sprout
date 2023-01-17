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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.database.habit.HabitFireStoreRepository;
import com.habitdev.sprout.database.habit.model.HabitFireStore;
import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuotesRepository {

    private final FirebaseFirestore firestore; // instance of the FirebaseFirestore class, used to access the Firestore database.
    private final CollectionReference quotesCollection; // CollectionReference object for the "habit" collection in Firestore.

    public QuotesRepository(QuotesViewModel quotesViewModel) {
        firestore = FirebaseFirestore.getInstance();
        quotesCollection = firestore.collection("quotes");
    }

    public interface FetchCallback {
        void onFetchQuoteSuccess(List<Quotes> quotesList);
        void onFetchQuoteFailure(Exception e);
    }

    public interface InsertCallback {
        void onInsertQuoteSuccess();
        void onInsertQuoteFailure(Exception e);
    }

    public interface UpdateCallback {
        void onUpdateQuoteSuccess();
        void onUpdateQuoteFailure(Exception e);
    }

    public interface DeleteCallback {
        void onDeleteQuoteSuccess();
        void onDeleteQuoteFailure(Exception e);
    }

    public void fetchData(final FetchCallback callback) {
        quotesCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Quotes> quotes = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            Quotes quote = documentSnapshot.toObject(Quotes.class);
                            quote.setId(documentSnapshot.getId());
                            quotes.add(quote);
                        }
                        callback.onFetchQuoteSuccess(quotes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFetchQuoteFailure(e);
                    }
                });
    }

    public void insertQuote(String docID, Quotes quote, final InsertCallback callback) {
        if (quote.getId().trim().isEmpty()) {
            quotesCollection.document().set(quote.toMap())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onInsertQuoteSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onInsertQuoteFailure(e);
                        }
                    });
        } else {
            quotesCollection.document(docID.trim()).set(quote.toMap())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onInsertQuoteSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onInsertQuoteFailure(e);
                        }
                    });
        }
    }

    public void updateQuote(Quotes quotes) {
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

    public void deleteQuote(String id) {
        quotesCollection.document(id)
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
