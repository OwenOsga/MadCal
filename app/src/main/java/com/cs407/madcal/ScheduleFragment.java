package com.cs407.madcal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // I've added a placeholder schedule. The plan is to implement full functionalities in the
        // coming weeks.
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ((TextView)view.findViewById(R.id.monday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");
        ((TextView)view.findViewById(R.id.wednesday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");
        ((TextView)view.findViewById(R.id.friday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");

        return view;
    }
}
