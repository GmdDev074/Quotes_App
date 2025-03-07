package com.example.quotes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotes.R;
import com.example.quotes.model.QuoteModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.FullScreenContentCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    private List<QuoteModel> quotes;
    private Context context;
    private Random random = new Random();

    private int clickCount = 0;
    private InterstitialAd mInterstitialAd;

    public QuoteAdapter(Context context, List<QuoteModel> quotes) {
        this.context = context;
        this.quotes = new ArrayList<>(quotes); // ✅ To prevent UnsupportedOperationException
        loadInterstitialAd();
        // Assign random colors initially
        for (QuoteModel quote : quotes) {
            quote.setBackgroundColor(getRandomColor());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuoteModel quote = quotes.get(position);
        holder.quoteText.setText(quote.getQuoteText());
        holder.quoteWriter.setText(quote.getQuoteWriter());
        holder.cardView.setCardBackgroundColor(quote.getBackgroundColor());

        holder.itemView.setOnClickListener(v -> {
            int newColor = getRandomColor();
            quote.setBackgroundColor(newColor);
            holder.cardView.setCardBackgroundColor(newColor);

            clickCount++;

            if (clickCount >= 3) {
                showInterstitialAd();
                clickCount = 0; // reset click count after showing the ad
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    private int getRandomColor() {
        // Generate random pastel-like color
        int red = random.nextInt(156) + 100;
        int green = random.nextInt(156) + 100;
        int blue = random.nextInt(156) + 100;
        return Color.rgb(red, green, blue);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitialAdUnitId = context.getString(R.string.interstitial_ad_id);
        InterstitialAd.load(context, interstitialAdUnitId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                loadInterstitialAd(); // Load the next ad when one is closed
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((android.app.Activity) context);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView quoteText;
        TextView quoteWriter;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            quoteText = itemView.findViewById(R.id.quote_text);
            quoteWriter = itemView.findViewById(R.id.quote_writer);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
