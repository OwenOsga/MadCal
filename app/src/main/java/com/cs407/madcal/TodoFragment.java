package com.cs407.madcal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TodoFragment extends Fragment {

    private EditText taskEditText;
    private ListView taskListView;
    private Button addTaskButton;

    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    private String wiscId; // Variable to store WISC ID

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        // Retrieve the WISC ID from the fragment's arguments
        if (getArguments() != null) {
            wiscId = getArguments().getString("WISC_ID");
        }

        taskEditText = root.findViewById(R.id.taskEditText);
        taskListView = root.findViewById(R.id.taskListView);
        addTaskButton = root.findViewById(R.id.addTaskButton);

        // Initialize the task list and adapter
        // Here, consider loading existing tasks associated with the wiscId
        taskList = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(taskAdapter);

        // Set up the click listener for the "Add Task" button
        addTaskButton.setOnClickListener(v -> addTask());

        return root;
    }

    // Method to add a task to the list
    private void addTask() {
        String task = taskEditText.getText().toString().trim();
        if (!task.isEmpty()) {
            taskList.add(task);
            taskAdapter.notifyDataSetChanged(); // Notify the adapter that the dataset has changed
            taskEditText.getText().clear(); // Clear the input field

            // Here, consider saving the task associated with the wiscId
        }
    }
}
