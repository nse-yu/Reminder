package com.example.reminder.menu.completed;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;

import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.databinding.ActivityCompletedBinding;

public class CompletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompletedBinding binding = ActivityCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set text of the reminder button in background thread
        MemoViewModel viewModel = MainActivity.getMemoViewModel();
        viewModel.countCompleted(CompletedActivity.this);

        //set the action bar's preferences
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.actionbar_background, getTheme()));
            actionBar.setTitle(R.string.app_completed);
        }

        //listener
        binding.reminderCompletedList.setOnClickListener(v -> {
            Intent intent = new Intent(CompletedActivity.this,CompletedReminderActivity.class);
            startActivity(intent);
        });
    }
}