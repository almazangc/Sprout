package com.habitdev.sprout.database.quotes;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.List;

public class QuotesViewModel extends ViewModel {

    QuotesRepository repository;

    public QuotesViewModel(){
        repository = new QuotesRepository();
    }

    public List<Quotes> getQuotesList() {
        return repository.getQuotesList();
    }
}
