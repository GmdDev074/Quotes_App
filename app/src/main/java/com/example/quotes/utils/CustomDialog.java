package com.example.quotes.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.example.quotes.R;

public class CustomDialog extends Dialog {

    public interface AdWatchCallback {
        void onAdWatch();
    }

    public CustomDialog(Context context, AdWatchCallback callback) {
        super(context);
        setContentView(R.layout.dialog_watch_ad);
        setCancelable(true);

        Button btnWatchAd = findViewById(R.id.btn_watch_ad);
        Button btnCancel = findViewById(R.id.btn_cancel);

        btnWatchAd.setOnClickListener(v -> {
            dismiss();
            callback.onAdWatch();
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }
}