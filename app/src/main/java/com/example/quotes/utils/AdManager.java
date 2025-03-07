package com.example.quotes.utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdManager {
    public static void loadBanner(Context context, AdView adView) {
        adView.loadAd(new AdRequest.Builder().build());
    }
}
