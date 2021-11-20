package com.example.reminder.menu.completed;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.reminder.AllFragment;
import com.example.reminder.DetailActivity;
import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityCompletedBinding;
import com.example.reminder.databinding.ActivityCompletedReminderBinding;

import java.util.ArrayList;
import java.util.List;

public class CompletedReminderActivity extends AppCompatActivity implements
        MemoAdapter.ClickListener {
    private MemoViewModel viewModel;
    private MemoAdapter adapter;
    public static final String TOPIC_STRING = "TOPIC";
    public static final String SUMMARY_STRING = "SUMMARY";
    public static final String ID_INT = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompletedReminderBinding binding =
                ActivityCompletedReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //actionbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.actionbar_background, getTheme()));
            actionBar.setTitle(R.string.app_reminder);
        }

        //initialize
        RecyclerView recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MemoViewModel.class);
        adapter = new MemoAdapter(this);
        recyclerView.setAdapter(adapter);

        //listener
        adapter.setOnCheckedListener(this);

        viewModel.selectAll().observe(this, memos -> {
            Log.d("OBSERVE CHANGED", "COMPLETED O B S E R V E C H A N G E D sum = " + memos.size());

            List<Memo> new_list = new ArrayList<>();

            /*isCompletedによって、完了リストへ移動するものとそうでないものをわける
             * データベース上はどちらも存在するが、recyclerview上ではnot completedのみ表示*/
            for(int i = 0;i < memos.size();i++){
                if(memos.get(i).isCompleted()) {
                    Log.d("CHECKED","C H E C K E D : "+i);
                    new_list.add(memos.get(i));
                }
            }
            //adapt the new list of memo that excludes the completed memo
            adapter.setMemos(new_list);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("CMP: ON ITEM CLICK", "O N I T E M C L I C K : position = "+position);

        Intent det_intent = new Intent(this, CompletedDetailActivity.class);
        //get a clicked memo
        Memo clicked_memo = adapter.getMemoAtPosition(position);
        //put some arguments
        det_intent.putExtra(TOPIC_STRING,clicked_memo.getTopic());
        det_intent.putExtra(SUMMARY_STRING,clicked_memo.getSummary());
        det_intent.putExtra(ID_INT,clicked_memo.getId());
        //start
        startActivity(det_intent);
    }

    @Override
    public void onChecked(View view, int position, boolean isChecked) {
        if(!isChecked) {
            Log.d("IS CHECKED == FALSE", "I S C H E C K E D = = T R U E");
            Log.d("RESETTING","R E S E T T I N G");

            //get a clicked memo
            Memo checked_memo = adapter.getMemoAtPosition(position);
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            //delete->同じMemoにcompleted属性をつけて再構成
            Memo completed_memo = new Memo(
                    checked_memo.getId(), checked_memo.getTopic(), checked_memo.getSummary());
            viewModel.delete(completed_memo);
            completed_memo.setCompleted(false);
            viewModel.insert(completed_memo);

            //resetting message
            Toast.makeText(this, R.string.message_resetting, Toast.LENGTH_SHORT).show();
        }
    }
}