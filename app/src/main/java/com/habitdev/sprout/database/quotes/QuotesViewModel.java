package com.habitdev.sprout.database.quotes;

import androidx.lifecycle.ViewModel;

import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.List;

public class QuotesViewModel extends ViewModel {

    final QuotesRepository repository;

    public QuotesViewModel(){
        repository = new QuotesRepository();
    }

    public List<Quotes> getQuotesList() {
        return repository.getQuotesList();
    }
}
