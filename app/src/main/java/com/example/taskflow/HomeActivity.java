package com.example.taskflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private EditText etTask;
    private Button btnAddTask;
    private Button btnLogout;
    private LinearLayout taskContainer;

    private SharedPreferences preferences;
    private JSONArray tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = getSharedPreferences(
                "TaskFlowPrefs",
                MODE_PRIVATE
        );

        // Verify login status
        if (!preferences.getBoolean("isLoggedIn", false)) {
            openLogin();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        etTask = findViewById(R.id.etTask);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnLogout = findViewById(R.id.btnLogout);
        taskContainer = findViewById(R.id.taskContainer);

        // Show the user's name
        String name = preferences.getString("name", "Student");
        tvWelcome.setText("Welcome, " + name + "!");

        // Load saved tasks
        loadTasks();

        // Add task
        btnAddTask.setOnClickListener(v -> addTask());

        // Logout
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadTasks() {

        String savedTasks = preferences.getString(
                "tasks",
                "[]"
        );

        try {
            tasks = new JSONArray(savedTasks);
        } catch (Exception e) {
            tasks = new JSONArray();
        }

        displayTasks();
    }

    private void addTask() {

        String task = etTask.getText()
                .toString()
                .trim();

        if (task.isEmpty()) {
            etTask.setError("Please enter a task");
            return;
        }

        tasks.put(task);

        saveTasks();

        etTask.setText("");

        displayTasks();

        Toast.makeText(
                this,
                "Task added successfully",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void saveTasks() {

        preferences.edit()
                .putString("tasks", tasks.toString())
                .apply();
    }

    private void displayTasks() {

        taskContainer.removeAllViews();

        for (int i = 0; i < tasks.length(); i++) {

            final int taskIndex = i;
            String task = tasks.optString(i, "");

            TextView taskView = new TextView(this);

            taskView.setText("☐  " + task);
            taskView.setTextSize(17);
            taskView.setTextColor(Color.rgb(31, 35, 64));
            taskView.setGravity(Gravity.CENTER_VERTICAL);
            taskView.setPadding(16, 18, 16, 18);
            taskView.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, 0, 0, 12);

            taskView.setLayoutParams(params);

            // Tap a task to delete it
            taskView.setOnClickListener(v -> {

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Delete Task")
                        .setMessage(
                                "Do you want to delete this task?"
                        )
                        .setPositiveButton(
                                "Delete",
                                (dialog, which) -> {

                                    if (taskIndex < tasks.length()) {
                                        tasks.remove(taskIndex);
                                    }

                                    saveTasks();
                                    displayTasks();
                                }
                        )
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            taskContainer.addView(taskView);
        }
    }

    private void logoutUser() {

        preferences.edit()
                .putBoolean("isLoggedIn", false)
                .apply();

        openLogin();
    }

    private void openLogin() {

        Intent intent = new Intent(
                HomeActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}