package com.example.todolist;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd, buttonDeleteAll;
    private ListView listViewTasks;
    private TextView textViewTitle;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDeleteAll = findViewById(R.id.buttonDeleteAll);
        listViewTasks = findViewById(R.id.listViewTasks);
        textViewTitle = findViewById(R.id.textViewTitle);

        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDeleteDialog(position);
            }
        });

        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllTasks();
            }
        });

        // Check if the list is empty to hide the title
        updateTitleVisibility();

        // Add an OnLayoutChangeListener to update the title visibility whenever the layout changes
        listViewTasks.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateTitleVisibility();
            }
        });
    }

    private void addTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        // Set up the input layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the task input
        final EditText taskInput = new EditText(this);
        taskInput.setInputType(InputType.TYPE_CLASS_TEXT);
        taskInput.setHint("Task");
        layout.addView(taskInput);

        // Set up the time picker
        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);
        layout.addView(timePicker);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    String taskWithTime = task + " (at " + time + ")";
                    taskList.add(taskWithTime);
                    adapter.notifyDataSetChanged();

                    // Update title visibility after adding a task
                    updateTitleVisibility();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void showEditDeleteDialog(final int position) {
        if (position >= 0 && position < taskList.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Action")
                    .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    editTask(position);
                                    break;
                                case 1:
                                    deleteSpecificTask(position);
                                    break;
                            }
                        }
                    })
                    .show();
        }
    }

    private void editTask(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(taskList.get(position));

        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedTask = input.getText().toString().trim();
                if (!editedTask.isEmpty()) {
                    taskList.set(position, editedTask);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteSpecificTask(int position) {
        if (position >= 0 && position < taskList.size()) {
            taskList.remove(position);
            adapter.notifyDataSetChanged();

            // Update title visibility after deleting a task
            updateTitleVisibility();
        }
    }

    private void deleteAllTasks() {
        taskList.clear();
        adapter.notifyDataSetChanged();

        // Update title visibility after deleting all tasks
        updateTitleVisibility();
    }

    private void updateTitleVisibility() {
        if (taskList.isEmpty()) {
            // If the list is empty, hide the title
            textViewTitle.setVisibility(View.GONE);
        } else {
            // If the list is not empty, show the title
            textViewTitle.setVisibility(View.VISIBLE);
        }
    }
    public void goBackToLandingPage(View view) {
        openLandingActivity();
    }

    private void openLandingActivity() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the MainActivity to prevent going back to it
    }
}
