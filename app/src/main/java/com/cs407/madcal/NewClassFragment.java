package com.cs407.madcal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class NewClassFragment extends Fragment {

    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    View view;
    private String wiscId; // Variable to store WISC ID
    private int taskId;
    private boolean updating;
    private boolean successfulAdd;

    private int[] weekdayIds = {R.id.monday_dot, R.id.tuesday_dot, R.id.wednesday_dot,
            R.id.thursday_dot, R.id.friday_dot, R.id.saturday_dot, R.id.sunday_dot};

    private ArrayList<Integer> checkedDaysIds;
    private ArrayList<String> checkedDays;

    private ArrayList<String> checkedDaysRange;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newclass, container, false);
        checkedDaysIds = new ArrayList<Integer>();
        checkedDays = new ArrayList<String>();
        checkedDaysRange = new ArrayList<String>();

        if (getArguments() != null) {
            wiscId = getArguments().getString("WISC_ID");
        }

        Button saveClassButton = view.findViewById(R.id.save_class_button);
        saveClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String class_name = ((EditText) view.findViewById(R.id.class_name)).getText().toString().trim();
                    if (class_name.isEmpty() || class_name.length() <= 0) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Your class has to have a name.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        return;
                    }

                    for (int id : weekdayIds) {
                        CheckBox checkBox = view.findViewById(id);
                        if (checkBox.isChecked()) {
                            checkedDaysIds.add(id);
                        }
                    }
                    if (checkedDaysIds.size() < 1) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Your class must run on at least one day.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        return;
                    }

                    for (int id : checkedDaysIds) {
                        checkedDays.add(((CheckBox)view.findViewById(id)).getText().toString().trim());
                        String weekday = ((CheckBox)view.findViewById(id)).getText().toString().trim().toLowerCase();


                        String from_hour = ((EditText)view.findViewById(getResources()
                                .getIdentifier(weekday + "_from_hour", "id", getActivity().getPackageName())))
                                .getText().toString().trim();

                        String from_minute = ((EditText)view.findViewById(getResources()
                                .getIdentifier(weekday + "_from_minute", "id", getActivity().getPackageName())))
                                .getText().toString().trim();

                        String to_hour = ((EditText)view.findViewById(getResources()
                                .getIdentifier(weekday + "_to_hour", "id", getActivity().getPackageName())))
                                .getText().toString().trim();

                        String to_minute = ((EditText)view.findViewById(getResources()
                                .getIdentifier(weekday + "_to_minute", "id", getActivity().getPackageName())))
                                .getText().toString().trim();

                        RadioGroup radioGroup = view.findViewById(getResources()
                                .getIdentifier(weekday + "_from_group", "id", getActivity().getPackageName()));

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        String from_meridiem = "";
                        if (selectedId == getResources().getIdentifier(weekday + "_from_am_dot", "id",
                                getActivity().getPackageName())) {
                            from_meridiem = "AM";
                        } else if (selectedId == getResources().getIdentifier(weekday + "_from_pm_dot", "id",
                                getActivity().getPackageName())) {
                            from_meridiem = "PM";
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("You must select either AM or PM.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            return;
                        }

                        radioGroup = view.findViewById(getResources()
                                .getIdentifier(weekday + "_to_group", "id", getActivity().getPackageName()));
                        selectedId = radioGroup.getCheckedRadioButtonId();
                        String to_meridiem = "";
                        if (selectedId == getResources().getIdentifier(weekday + "_to_am_dot", "id",
                                getActivity().getPackageName())) {
                            to_meridiem = "AM";
                        } else if (selectedId == getResources().getIdentifier(weekday + "_to_pm_dot", "id",
                                getActivity().getPackageName())) {
                            to_meridiem = "PM";
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("You must select either AM or PM.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            return;
                        }

                        System.out.println(class_name + " runs on " + weekday + " from " + from_hour + ":" + from_minute + " " + from_meridiem + " to " + to_hour + ":" + to_minute + " " + to_meridiem);

                        if ((Integer.valueOf(from_hour) < 1 || Integer.valueOf(from_hour) > 12
                                || Integer.valueOf(to_hour) < 1 || Integer.valueOf(to_hour) > 12)
                                || (Integer.valueOf(from_minute) < 0 || Integer.valueOf(from_minute) > 59
                                || Integer.valueOf(to_minute) < 0 || Integer.valueOf(to_minute) > 59)) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("You've entered an invalid (AM/PM) time. Try again.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            return;
                        }

                        if (Integer.valueOf(from_hour) < 10 && from_hour.length() == 1) {
                            from_hour = "0" + from_hour;
                        }

                        if (Integer.valueOf(from_minute) < 10 && from_minute.length() == 1) {
                            from_minute = "0" + from_minute;
                        }

                        if (Integer.valueOf(to_hour) < 10 && to_hour.length() == 1) {
                            to_hour = "0" + to_hour;
                        }

                        if (Integer.valueOf(to_minute) < 10 && to_minute.length() == 1) {
                            to_minute = "0" + to_minute;
                        }

                        String fromToRange = from_hour + ":" + from_minute + " " + from_meridiem +
                                " to " + to_hour + ":" + to_minute + " " + to_meridiem;
                        checkedDaysRange.add(fromToRange);
                    }

                    String class_days = "";
                    String class_range = "";
                    for (int i = 0; i < checkedDaysIds.size(); i++) {
                        if (i != 0) {
                            class_days += ",";
                            class_range += ",";
                        }
                        class_days += checkedDays.get(i);
                        class_range += checkedDaysRange.get(i);
                    }

                    System.out.println("[(" + class_name + ") , (" + class_days + ") , ("+ class_range + ") , (" + wiscId + ")]");
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    db.addClass(class_name, class_days, class_range, wiscId);

                    // After successful add
                    Bundle result = new Bundle();
                    result.putBoolean("new_class", true);
                    getParentFragmentManager().setFragmentResult("class_add_key", result);
                    getFragmentManager().popBackStack();


                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("There was an error processing your class. Make sure that for each weekday selected," +
                                    "each box is filled and a valid time is entered.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Dismiss the dialog when "Ok" is clicked
                                    dialog.dismiss();

                                }
                            }).show();
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
