package com.example.quotes.utils;

import io.paperdb.Paper;

public class SubscriptionManager {
    public static boolean hasSubscription() {
        return Paper.book().read("isSubscribed", false);
    }
}
