package com.habitdev.sprout.database.quotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuotesViewModel extends AndroidViewModel {

    private final QuotesRepository repository;
    private MutableLiveData<List<Quotes>> liveData;
    private List<Quotes> data;

    public QuotesViewModel(@NonNull Application application) {
        super(application);
        repository = new QuotesRepository(this);
        liveData = new MutableLiveData<>();
        data = new ArrayList<>();
    }

    public LiveData<List< Quotes>> getLiveData() {
        return liveData;
    }

    public void setLiveData(List< Quotes> dataList) {
        liveData.setValue(dataList);
    }

    public List<Quotes> getData() {
        repository.fetchData();
        return data;
    }

    public void setData(List<Quotes> data) {
        this.data = data;
    }

    public void fetchData() {
        repository.fetchData();
    }

    public void update_quotes(Quotes quotes) {
        repository.updateQuotes(quotes);
    }

    public void insert_quotes(String docID, Map<String, Object> quotes) {
        repository.insert_quotes(docID, quotes);
    }

    public void delete_quotes(String id) {
        repository.delete_quotes(id);
    }
}
