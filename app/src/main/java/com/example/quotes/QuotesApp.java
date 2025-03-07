package com.example.quotes;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import io.paperdb.Paper;

public class QuotesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        MobileAds.initialize(this);
    }
}

