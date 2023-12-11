package com.cs407.madcal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        if (wiscId == null) {
            logout();
        }

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
                if (position != 1) {
                    hideToDoTabFragment();
                }
                if (position == 2) { // Assuming the map is at position 2
                    viewPager.setUserInputEnabled(false);
                } else {
                    viewPager.setUserInputEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            navigateToLogin();
            return;
        }
    }

    private void hideToDoTabFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment emptyFragment = new Fragment();
        fragmentTransaction.replace(R.id.fragment_container, emptyFragment);
        fragmentTransaction.commit();
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
            String help_message = "For the Calendar, select a day that is highlighted with a red arrow in order to see the tasks that are due that day.\n\n";
            help_message += "For the To Do list, add, edit, or delete tasks/assignments as you see fit.\n\n";
            help_message += "For the UW Map, search for a building. When you select a building, you can create a pin that assigns the building to a specific class. " +
                    "When you click on the pin, you get the information on which class is running within the building.\n\n";
            help_message += "For the Schedule, add, edit, or delete classes as you see fit.";

            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(help_message)
                    .setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
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
