package com.habitdev.sprout.database.quotes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.ArrayList;
import java.util.List;

public class QuotesViewModel extends AndroidViewModel {

    private final QuotesRepository repository;
    private MutableLiveData<List<Quotes>> liveData;
    private List<Quotes> data;

    private final String TAG = "tag";

    public QuotesViewModel(@NonNull Application application) {
        super(application);
        repository = new QuotesRepository(this);
        liveData = new MutableLiveData<>();
        data = new ArrayList<>();
    }

    public LiveData<List< Quotes>> getLiveData() {
        return liveData;
    }

    public List<Quotes> getData() {
        fetchData();
        return data;
    }

    public void setData(List<Quotes> data) {
        this.data = data;
    }

    public void fetchData() {
        repository.fetchData(new QuotesRepository.FetchCallback() {
            @Override
            public void onFetchQuoteSuccess(List<Quotes> quotes) {
                QuotesViewModel.this.liveData.setValue(quotes);
                QuotesViewModel.this.setData(quotes);
            }

            @Override
            public void onFetchQuoteFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onFetchQuoteFailure: " + e.getMessage());
            }
        });
    }

    public void insertQuote(String docID, Quotes quote) {
        repository.insertQuote(docID , quote, new QuotesRepository.InsertCallback() {
            @Override
            public void onInsertQuoteSuccess() {
                // Handle success
            }

            @Override
            public void onInsertQuoteFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onInsertHabitFailure: :" + e.getMessage());
            }
        });
    }

    public void updateQuote(Quotes quote) {
        repository.updateQuote(quote, new QuotesRepository.UpdateCallback() {
            @Override
            public void onUpdateQuoteSuccess() {
                // Handle success
            }

            @Override
            public void onUpdateQuoteFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onUpdateQuoteFailure: :" + e.getMessage());
            }
        });
    }

    public void deleteQuote(String id) {
        repository.deleteQuote(id, new QuotesRepository.DeleteCallback() {
            @Override
            public void onDeleteQuoteSuccess() {
                // Handle success
            }

            @Override
            public void onDeleteQuoteFailure(Exception e) {
                // Handle failure
                Log.e(TAG, "onDeleteQuoteFailure: :" + e.getMessage());
            }
        });
    }
}
