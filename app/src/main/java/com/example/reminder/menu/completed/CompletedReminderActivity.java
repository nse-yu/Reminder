package com.example.reminder.menu.completed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reminder.R;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityCompletedReminderBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class CompletedReminderActivity extends AppCompatActivity implements MemoAdapter.ClickListener {

    private QueryViewModel  queryViewModel;

    /**This MemoAdapter is different from ones in the MainActivity because this is responsible for memos completed*/
    private MemoAdapter     completedMemoAdapter;

    public static final String TOPIC_STRING     = "TOPIC";
    public static final String SUMMARY_STRING   = "SUMMARY";
    public static final String ID_INT           = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        ActivityCompletedReminderBinding binding = ActivityCompletedReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //[ActionBar]
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.actionbar_background, getTheme()));
            actionBar.setTitle(R.string.app_reminder);
        }

        //[MemoAdapter]
        completedMemoAdapter = new MemoAdapter();
        completedMemoAdapter.setOnCheckedListener(this);

        //[RecyclerView]
        RecyclerView recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(completedMemoAdapter);


        //[QueryViewModel]
        queryViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(new ViewModelInitializer<>(
                        QueryViewModel.class,
                        creationExtras -> new QueryViewModel(getApplication())
                ))
        ).get(QueryViewModel.class);

        queryViewModel.getMemos().observe(this, getListObserver());
    }

    @NotNull
    private Observer<List<Memo>> getListObserver() {

        return all -> {

            //adapt the new list of memo that excludes the completed memo
            completedMemoAdapter.setMemos(
                    all.stream().filter(Memo::isCompleted).collect(Collectors.toList())
            );

        };
    }

    @Override
    public void onItemClick(View view, int position) {

        //special version of the DetailActivity
        Intent toCompletedDetail = new Intent(this, CompletedDetailActivity.class);

        //get a clicked memo
        Memo clickedMemo = completedMemoAdapter.getMemoAtPosition(position);

        //put some arguments
        toCompletedDetail.putExtra(TOPIC_STRING,clickedMemo.getTopic());
        toCompletedDetail.putExtra(SUMMARY_STRING,clickedMemo.getSummary());
        toCompletedDetail.putExtra(ID_INT,clickedMemo.getId());

        //start
        startActivity(toCompletedDetail);
    }

    @Override
    public void onChecked(View view, int position, boolean isChecked) {

        if(isChecked) return;

        //get a clicked memo
        Memo checkedMemo = completedMemoAdapter.getMemoAtPosition(position);

        //changes "completed" state of memos to false( not completed, default state )
        queryViewModel.toggleCompletedState(checkedMemo.getId(), false);

        //notify that a checked memo is reassigned
        Toast.makeText(this, R.string.message_resetting, Toast.LENGTH_SHORT).show();
    }
}