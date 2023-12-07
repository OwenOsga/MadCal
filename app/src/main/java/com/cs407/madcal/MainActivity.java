package com.cs407.madcal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            navigateToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        String wiscId = sharedPreferences.getString("WISC_ID", ""); // Retrieve the WISC ID from SharedPreferences

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, wiscId); // Pass the WISC ID
        viewPager.setAdapter(sectionsPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Calendar");
                    break;
                case 1:
                    tab.setText("To Do");
                    break;
                case 2:
                    tab.setText("UW Map");
                    break;
                case 3:
                    tab.setText("Schedule");
                    break;
            }
        }).attach();

        // Disable swipe for the map tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2) { // Assuming the map is at position 2
                    viewPager.setUserInputEnabled(false);
                } else {
                    viewPager.setUserInputEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.help_menu) {
            Toast.makeText(this, ":)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
