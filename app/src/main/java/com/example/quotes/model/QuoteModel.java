package com.example.quotes.model;

public class QuoteModel {
    private String quoteText;
    private String quoteWriter;
    private int backgroundColor;
    private boolean isFavorite;

    public QuoteModel(String quoteText, String quoteWriter, boolean isFavorite) {
        this.quoteText = quoteText;
        this.quoteWriter = quoteWriter;
        this.isFavorite = isFavorite;
    }

    public int getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(int backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getQuoteWriter() {
        return quoteWriter;
    }

    public void setQuoteWriter(String quoteWriter) {
        this.quoteWriter = quoteWriter;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
