package com.cs407.madcal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        Button scheduleButton = view.findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newClassFragment = new NewClassFragment();
                Bundle bundle = new Bundle();
                bundle.putString("WISC_ID", wiscId);
                newClassFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, newClassFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
