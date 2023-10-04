package com.cs407.madcal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CalendarFragment();  // Assuming you have a class named CalendarFragment
            case 1:
                return new TodoFragment();     // ...and TodoFragment
            case 2:
                return new Placeholder1Fragment(); // ...and so on
            case 3:
                return new Placeholder2Fragment();
            default:
                return new CalendarFragment();  // Default to the calendar fragment if something goes wrong
        }
    }

    @Override
    public int getItemCount() {
        return 4;  // since you have 4 tabs
    }
}
