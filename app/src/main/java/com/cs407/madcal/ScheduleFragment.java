package com.cs407.madcal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ScheduleFragment extends Fragment {

    private String wiscId; // Variable to store WISC ID

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Retrieve the WISC ID from the fragment's arguments
        if (getArguments() != null) {
            wiscId = getArguments().getString("WISC_ID");
        }

        // Use the WISC ID to display or manage the user-specific schedule
        // For now, it shows a placeholder schedule.
        // This is where you will integrate your logic to fetch and display the actual schedule
        // related to the WISC ID.
        ((TextView)view.findViewById(R.id.monday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");
        ((TextView)view.findViewById(R.id.wednesday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");
        ((TextView)view.findViewById(R.id.friday_content)).setText("MATH 150 -- 1:30-3:15 PM -- Birge Hall");

        return view;
    }
}
