package com.example.reminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {
    //memo's contents id is essential to update specific memo
    private String memo_topic;
    private String memo_summary;
    private int memo_id;

    //public opened KEY CODE to open the intent
    private final static int REQUEST_UPDATE = 2;
    public final static String EDIT_UPDATE = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.reminder.databinding.ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        memo_topic = getIntent().getStringExtra(AllFragment.TOPIC_STRING);
        memo_summary = getIntent().getStringExtra(AllFragment.SUMMARY_STRING);
        memo_id = getIntent().getIntExtra(AllFragment.ID_INT,-1);

        //set text
        binding.showTopic.setText(memo_topic);
        binding.showSummary.setText(memo_summary);

        //listener
        binding.buttonEdit.setOnClickListener(this);
        binding.buttonDelete.setOnClickListener(this);
        binding.buttonComplete.setOnClickListener(this);
    }

    //onclick for update one memo's contents
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_edit) {
            //appendボタン（+）の使いまわしActivityを使う
            Intent update_intent = new Intent(this, NewMemoActivity.class);
            update_intent.putExtra(EDIT_UPDATE, new String[]{memo_topic, memo_summary});
            startActivityForResult(update_intent, REQUEST_UPDATE);
        }else if(id == R.id.button_delete){
            //to show alert
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.alert_delete);
            //listener
            alertBuilder.setPositiveButton(R.string.button_delete,this);
            alertBuilder.setNegativeButton(R.string.button_cancel,this);
            //show the alert
            alertBuilder.show();
        }else if(id == R.id.button_complete){
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            //delete->同じMemoにcompleted属性をつけて再構成
            Memo completed_memo = new Memo(memo_id, memo_topic, memo_summary);
            viewModel.delete(completed_memo);
            completed_memo.setCompleted(true);
            viewModel.insert(completed_memo);

            returnMain();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_UPDATE && resultCode == RESULT_OK){
            MemoViewModel viewModel = MainActivity.getMemoViewModel();

            if (data != null) {
                String[] memo_txt = data.getStringArrayExtra(NewMemoActivity.EDIT_REPLY);
                Memo memo = new Memo(memo_id,memo_txt[0],memo_txt[1]);
                viewModel.update(memo);

                //return to main activity
                returnMain();
            }
        }else{
            Log.d("RESULT FAILED","R E S U L T F A I L E D");
        }
    }

    //onclick for alert and delete
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE) {
            //delete memo
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            Memo delete_memo = new Memo(memo_id, memo_topic, memo_summary);
            viewModel.delete(delete_memo);

            //return to main activity
            returnMain();
        }
    }

    public void returnMain(){
        //return to MainActivity
        Intent return_intent = new Intent(this,MainActivity.class);
        startActivity(return_intent);
    }
}