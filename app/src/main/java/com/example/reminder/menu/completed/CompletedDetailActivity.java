package com.example.reminder.menu.completed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.reminder.AllFragment;
import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityCompletedDetailBinding;

public class CompletedDetailActivity extends AppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {
    //memo's contents id is essential to update specific memo
    private String memo_topic;
    private String memo_summary;
    private int memo_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompletedDetailBinding binding = ActivityCompletedDetailBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        memo_topic = getIntent().getStringExtra(CompletedReminderActivity.TOPIC_STRING);
        memo_summary = getIntent().getStringExtra(CompletedReminderActivity.SUMMARY_STRING);
        memo_id = getIntent().getIntExtra(CompletedReminderActivity.ID_INT,-1);

        //set text
        binding.cmpShowTopic.setText(memo_topic);
        binding.cmpShowSummary.setText(memo_summary);

        //listener
        binding.cmpButtonDelete.setOnClickListener(this);
        binding.cmpButtonResetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.cmp_button_delete) {
            //to show alert
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.alert_delete);
            //listener
            alertBuilder.setPositiveButton(R.string.button_delete, this);
            alertBuilder.setNegativeButton(R.string.button_cancel, this);
            //show the alert
            alertBuilder.show();
        }else if(id == R.id.cmp_button_resetting){
            Log.d("RESETTING","R E S E T T I N G");

            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            //delete->同じMemoにcompleted属性をつけて再構成
            Memo completed_memo = new Memo(memo_id, memo_topic, memo_summary);
            viewModel.delete(completed_memo);
            completed_memo.setCompleted(false);
            viewModel.insert(completed_memo);

            //resetting message
            Toast.makeText(this, R.string.message_resetting, Toast.LENGTH_SHORT).show();

            //return to the CompletedReminderActivity
            returnPresentActivity();
        }
    }

    //this is the method for the delete button
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE) {
            //delete memo
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            Memo delete_memo = new Memo(memo_id, memo_topic, memo_summary);
            viewModel.delete(delete_memo);

            returnPresentActivity();
        }
    }

    public void returnPresentActivity(){
        //return to main activity
        Intent return_intent = new Intent(this,CompletedReminderActivity.class);
        startActivity(return_intent);
    }
}