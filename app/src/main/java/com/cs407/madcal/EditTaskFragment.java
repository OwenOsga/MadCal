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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class EditTaskFragment extends Fragment {

    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    private String wiscId;
    private int taskId;

    public static boolean isValidDate(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edittask, container, false);

        // Retrieve the TASK ID from the fragment's arguments
        if (getArguments() != null) {
            wiscId = getArguments().getString("WISC_ID");
            taskId = getArguments().getInt("TASK_ID");
        }

        DatabaseHelper db = new DatabaseHelper(getActivity());
        String[] taskDetails = db.getTaskById(taskId);
        Log.e("TaskDetails: ", taskDetails[0] + " , " + taskDetails[1] + " , " +taskDetails[2] + " , " + taskDetails[3]);

        String old_task = taskDetails[0];
        String old_month = taskDetails[1].split("/")[0];
        String old_day = taskDetails[1].split("/")[1];
        String old_year = taskDetails[1].split("/")[2];
        String old_hour = taskDetails[2].split(" ")[0].split(":")[0];
        String old_minute = taskDetails[2].split(" ")[0].split(":")[1];
        String old_meridiem = taskDetails[2].split(" ")[1];

        ((EditText)view.findViewById(R.id.the_new_task)).setText(old_task);
        ((EditText)view.findViewById(R.id.the_new_month)).setText(old_month);
        ((EditText)view.findViewById(R.id.the_new_day)).setText(old_day);
        ((EditText)view.findViewById(R.id.the_new_year)).setText(old_year);
        ((EditText)view.findViewById(R.id.the_new_hour)).setText(old_hour);
        ((EditText)view.findViewById(R.id.the_new_minute)).setText(old_minute);

        if (old_meridiem.equals("AM")) {
            ((RadioButton)view.findViewById(R.id.new_am_dot)).setChecked(true);
        } else if (old_meridiem.equals("PM")){
            ((RadioButton)view.findViewById(R.id.new_pm_dot)).setChecked(true);
        }

        Button saveTaskButton = view.findViewById(R.id.new_save_button);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String task = ((EditText)view.findViewById(R.id.the_new_task)).getText().toString().trim();
                    String month = ((EditText)view.findViewById(R.id.the_new_month)).getText().toString().trim();
                    String day = ((EditText)view.findViewById(R.id.the_new_day)).getText().toString().trim();
                    String year = ((EditText)view.findViewById(R.id.the_new_year)).getText().toString().trim();
                    String hour = ((EditText)view.findViewById(R.id.the_new_hour)).getText().toString().trim();
                    String minute = ((EditText)view.findViewById(R.id.the_new_minute)).getText().toString().trim();


                    RadioGroup radioGroup = view.findViewById(R.id.dotSelectionGroup);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    String meridiem = "";
                    if (selectedId == R.id.new_am_dot) {
                        meridiem = "AM";
                    } else if (selectedId == R.id.new_pm_dot) {
                        meridiem = "PM";
                    }
                    String msg = "You have added the task, which is due on " + month + "/" + day + "/" + year + ", at " + hour + ":" + minute + " " + meridiem + ".";

                    if (Integer.valueOf(month) < 10 && month.length() == 1) {
                        month = "0" + month;
                    }

                    if (Integer.valueOf(day) < 10 && day.length() == 1) {
                        day = "0" + day;
                    }

                    if (Integer.valueOf(hour) < 10 && hour.length() == 1) {
                        hour = "0" + hour;
                    }

                    if (Integer.valueOf(minute) < 10 && minute.length() == 1) {
                        minute = "0" + minute;
                    }

                    if(!isValidDate(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                        //  Start of Dialog Code
                        new AlertDialog.Builder(getActivity())
                                .setMessage("You've entered an invalid date. Try again.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        //  End of Dialog Code
                    } else if ((Integer.valueOf(hour) < 1 || Integer.valueOf(hour) > 12)
                            || (Integer.valueOf(minute) < 1 || Integer.valueOf(minute) > 59)) {
                        //  Start of Dialog Code
                        new AlertDialog.Builder(getActivity())
                                .setMessage("You've entered an invalid (AM/PM) time. Try again.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        //  End of Dialog Code
                    } else if (Integer.valueOf(year) < 2000 || Integer.valueOf(year) > 2200) {
                        //  Start of Dialog Code
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Make sure that the year isn't too far into the future or past.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        //  End of Dialog Code
                    } else {
                        // Construct date and time strings
                        String taskDate = month + "/" + day + "/" + year;
                        String taskTime = hour + ":" + minute + " " + meridiem;

                        // Use DatabaseHelper to insert the task
                        DatabaseHelper db = new DatabaseHelper(getActivity());
                        db.updateTask(taskId, task, taskDate, taskTime);

                        // After successful update
                        Bundle result = new Bundle();
                        result.putBoolean("updated", true);
                        getParentFragmentManager().setFragmentResult("task_update_key", result);
                        getFragmentManager().popBackStack();
                    }
                } catch (Exception e) {
                    //  Start of Dialog Code
                    new AlertDialog.Builder(getActivity())
                            .setMessage("There was an error processing your task. Make sure each box is filled and a valid date is entered.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Dismiss the dialog when "Ok" is clicked
                                    dialog.dismiss();

                                }
                            }).show();
                    //  End of Dialog Code
                }
            }
        });

        return view;
    }
}
