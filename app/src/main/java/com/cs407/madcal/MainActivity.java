package com.cs407.madcal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize ViewPager2 and TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        viewPager.setAdapter(sectionsPagerAdapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Calendar");
                    break;
                case 1:
                    tab.setText("To Do");
                    break;
                case 2:
                    tab.setText("Placeholder 1");
                    break;
                case 3:
                    tab.setText("Placeholder 2");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu to use in the action bar
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_calendar) {
            // Handle calendar action
            return true;
        } else if (itemId == R.id.nav_todo) {
            // Handle to do list action
            return true;
        } else if (itemId == R.id.nav_placeholder1) {
            // Handle placeholder 1 action
            return true;
        } else if (itemId == R.id.nav_placeholder2) {
            // Handle placeholder 2 action
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
