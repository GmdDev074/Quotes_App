package com.example.quotes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quotes.model.QuoteModel;
import com.example.quotes.repository.QuoteRepository;

import java.util.List;

public class QuoteViewModel extends ViewModel {
    private MutableLiveData<List<QuoteModel>> quotes = new MutableLiveData<>();
    private QuoteRepository repository = new QuoteRepository();

    public void loadQuotes() {
        quotes.setValue(repository.getQuotes());
    }

    public LiveData<List<QuoteModel>> getQuotes() {
        return quotes;
    }
}
