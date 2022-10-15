package com.example.reminder.menu.completed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import com.example.reminder.R;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.databinding.ActivityCompletedBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompletedActivity extends AppCompatActivity {

    private ActivityCompletedBinding    binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ActivityCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //[ActionBar]
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.actionbar_background, getTheme()));
            actionBar.setTitle(R.string.app_completed);
        }
        

        //[Button]
        Button btn = binding.reminderCompletedList;
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CompletedReminderActivity.class);
            startActivity(intent);
        });


        //[QueryViewModel]
        QueryViewModel queryViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(new ViewModelInitializer<>(
                        QueryViewModel.class,
                        creationExtras -> new QueryViewModel(getApplication())
                ))
        ).get(QueryViewModel.class);
        queryViewModel.getCurrentCount().observe(this, getListObserver());
    }

    @NotNull
    private Observer<List<Integer>> getListObserver() {
        return count -> binding.reminderCompletedList.setText(
                String.format("%s(%s)", binding.reminderCompletedList.getText().toString(), count.get(0))
        );
    }
}