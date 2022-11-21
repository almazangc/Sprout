package com.habitdev.sprout.database.quotes;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QuotesViewModel extends ViewModel {

    QuotesRepository repository;

    public QuotesViewModel(){
        repository = new QuotesRepository();
    }

    public List<Quotes> getQuotesList() {
        Log.d("tag", "View Model : getQuotesList => " + repository.getQuotesList().toString());
        return repository.getQuotesList();
    }
}
