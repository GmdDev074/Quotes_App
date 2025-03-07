package com.example.quotes.repository;

import com.example.quotes.model.QuoteModel;
import com.example.quotes.staticdata.QuoteData;

import java.util.List;

public class QuoteRepository {
    public List<QuoteModel> getQuotes() {
        return QuoteData.getQuotes();
    }
}
