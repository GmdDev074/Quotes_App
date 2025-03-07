package com.example.quotes.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotes.R;
import com.example.quotes.adapters.QuoteAdapter;
import com.example.quotes.utils.AdManager;
import com.example.quotes.viewmodel.QuoteViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private QuoteViewModel viewModel;
    private QuoteAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Change status bar icons to black (dark mode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Set the status bar background to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.toolbar_gradient)); // Use any light color
        }

      /*  // Change status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.actionBar)); // Replace 'your_custom_color' with your color resource

        //Change navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.actionBar)); // Replace 'your_custom_color' with your color resource*/


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        viewModel.getQuotes().observe(this, quotes -> {
            adapter = new QuoteAdapter(this, quotes);
            recyclerView.setAdapter(adapter);
        });

        viewModel.loadQuotes();
        AdManager.loadBanner(this, findViewById(R.id.adView));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Stay on Home

            } else if (id == R.id.nav_category) {
                // TODO: Start Category Activity

            } else if (id == R.id.nav_images) {
                // TODO: Start Images Activity

            } else if (id == R.id.nav_twitter) {
                openLink("https://twitter.com/YourProfile");

            } else if (id == R.id.nav_facebook) {
                openLink("https://facebook.com/YourPage");

            } else if (id == R.id.nav_instagram) {
                openLink("https://instagram.com/YourProfile");

            } else if (id == R.id.nav_share) {
                shareApp();

            } else if (id == R.id.nav_give_star) {
                rateApp();

            } else if (id == R.id.nav_about) {
                // TODO: Start About Activity

            } else if (id == R.id.nav_privacy) {
                openLink("https://yourwebsite.com/privacy-policy");
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quotes App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing Quotes app: https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
