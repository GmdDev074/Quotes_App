package com.example.quotes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotes.R;
import com.example.quotes.model.QuoteModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int QUOTE_TYPE = 0;
    private static final int AD_TYPE = 1;
    private static final int AD_POSITION_INTERVAL = 6;

    private final List<Object> items;
    private final Context context;
    private final Random random = new Random();
    private InterstitialAd interstitialAd;
    private int clickCount = 0; // Track clicks for interstitial ads

    public QuoteAdapter(Context context, List<QuoteModel> quotes) {
        this.context = context;
        this.items = new ArrayList<>();

        for (int i = 0; i < quotes.size(); i++) {
            items.add(quotes.get(i));
            if ((i + 1) % AD_POSITION_INTERVAL == 0) {
                items.add("AD");
            }
        }

        loadInterstitialAd(); // Load interstitial ad when adapter is created
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof QuoteModel ? QUOTE_TYPE : AD_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == QUOTE_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false);
            return new QuoteViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_native_ad, parent, false);
            return new AdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == QUOTE_TYPE) {
            QuoteModel quote = (QuoteModel) items.get(position);
            QuoteViewHolder quoteHolder = (QuoteViewHolder) holder;

            if (quote.getBackgroundColor() == 0) {
                quote.setBackgroundColor(getRandomColor());
            }

            quoteHolder.quoteText.setText(quote.getQuoteText());
            quoteHolder.quoteWriter.setText(quote.getQuoteWriter());
            setRoundedBackground(quoteHolder.linearLayout, quote.getBackgroundColor(), true);
            setRoundedBackground(quoteHolder.bottomActionRow, quote.getBackgroundColor(), false);

            // Handle click to change color
            quoteHolder.linearLayout.setOnClickListener(v -> {
                int newColor = getRandomColor();
                quote.setBackgroundColor(newColor);

                setRoundedBackground(quoteHolder.linearLayout, newColor, true);
                setRoundedBackground(quoteHolder.bottomActionRow, newColor, false);

                clickCount++;
                if (clickCount >= 3) {
                    showInterstitialAd();
                    clickCount = 0;
                }
            });

        } else {
            AdViewHolder adHolder = (AdViewHolder) holder;
            loadNativeAd(adHolder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void loadNativeAd(AdViewHolder holder) {
        holder.shimmerLayout.startShimmer();
        holder.shimmerLayout.setVisibility(View.VISIBLE);
        holder.nativeAdView.setVisibility(View.GONE);

        String nativeAdUnitId = context.getString(R.string.native_ad_id);
        AdLoader adLoader = new AdLoader.Builder(context, nativeAdUnitId)
                .forNativeAd(nativeAd -> {
                    populateNativeAdView(nativeAd, holder.nativeAdView);

                    // Hide shimmer effect when ad loads
                    holder.shimmerLayout.stopShimmer();
                    holder.shimmerLayout.setVisibility(View.GONE);
                    holder.nativeAdView.setVisibility(View.VISIBLE);
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        holder.nativeAdView.setVisibility(View.GONE);
                        holder.shimmerLayout.setVisibility(View.GONE);
                        Log.e("TAG", "Failed to load native ad: " + adError.getMessage());
                    }
                })
                .build();
        Log.d("TAG", "Native Ad Unit ID: " + nativeAdUnitId);

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        ShimmerFrameLayout shimmerContainer = adView.findViewById(R.id.shimmer_container);

        if (shimmerContainer != null) {
            shimmerContainer.stopShimmer();
            shimmerContainer.setVisibility(View.GONE);
        } else {
            Log.e("NativeAd", "ShimmerFrameLayout is NULL");
        }

        adView.setVisibility(View.VISIBLE);

        // Populate ad views safely
        TextView headlineView = adView.findViewById(R.id.ad_headline);
        Button callToActionView = adView.findViewById(R.id.ad_call_to_action);

        if (headlineView != null) {
            headlineView.setText(nativeAd.getHeadline());
            adView.setHeadlineView(headlineView);
        }

        if (callToActionView != null && nativeAd.getCallToAction() != null) {
            callToActionView.setText(nativeAd.getCallToAction());
            callToActionView.setVisibility(View.VISIBLE);
            adView.setCallToActionView(callToActionView);
        } else if (callToActionView != null) {
            callToActionView.setVisibility(View.GONE);
        }

        adView.setNativeAd(nativeAd);
    }



    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitialAdUnitId = context.getString(R.string.interstitial_ad_id);
        InterstitialAd.load(context, interstitialAdUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                interstitialAd = ad;
                Log.d("TAG", "Interstitial ad loaded" + interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                interstitialAd = null;
                Log.d("TAG", "Interstitial ad failed to load: " + adError.getMessage());
            }
        });
    }

    private void showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.show(null);
            loadInterstitialAd(); // Reload for next use
        }
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView quoteText, quoteWriter;
        LinearLayout linearLayout, bottomActionRow;

        QuoteViewHolder(View itemView) {
            super(itemView);
            quoteText = itemView.findViewById(R.id.quote_text);
            quoteWriter = itemView.findViewById(R.id.quote_writer);
            linearLayout = itemView.findViewById(R.id.quote_card_container);
            bottomActionRow = itemView.findViewById(R.id.bottom_action_row);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        NativeAdView nativeAdView;
        ShimmerFrameLayout shimmerLayout;

        AdViewHolder(View itemView) {
            super(itemView);
            nativeAdView = itemView.findViewById(R.id.native_ad_view);
            shimmerLayout = itemView.findViewById(R.id.shimmer_container);
        }
    }

    private void setRoundedBackground(View view, int color, boolean isTop) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadii(isTop ? new float[]{20f, 20f, 20f, 20f, 0f, 0f, 0f, 0f} : new float[]{0f, 0f, 0f, 0f, 20f, 20f, 20f, 20f});
        view.setBackground(drawable);
    }

    private int getRandomColor() {
        return Color.rgb(random.nextInt(156) + 100, random.nextInt(156) + 100, random.nextInt(156) + 100);
    }
}
