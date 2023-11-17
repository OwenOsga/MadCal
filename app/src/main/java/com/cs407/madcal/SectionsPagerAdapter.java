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
                return new CalendarFragment();
            case 1:
                return new TodoFragment();
            case 2:
                return new MapFragment();
            case 3:
                return new ScheduleFragment();
            default:
                return new CalendarFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;  // since you have 4 tabs
    }
}
