package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reminder.databinding.ActivityNewMemoBinding;

public class NewMemoActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityNewMemoBinding binding;
    private EditText edit_topic;
    private EditText edit_summary;
    private Bundle bundle;
    public static final String EDIT_REPLY = "EDIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        edit_topic = binding.editTopic;
        edit_summary = binding.editSummary;

        //listener
        binding.save.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        //入力テキストがどちらかでも空
        if(TextUtils.isEmpty(edit_topic.getText()) || TextUtils.isEmpty(edit_summary.getText())){
            if(TextUtils.isEmpty(edit_topic.getText()))
                Toast.makeText(this, R.string.hint_topic+R.string.toast_please_input, Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(edit_summary.getText()))
                Toast.makeText(this, R.string.hint_topic+R.string.toast_please_input, Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }else{
            //get inputted values
            String topic = edit_topic.getText().toString();
            String summary = edit_summary.getText().toString();
            //put string array
            intent.putExtra(EDIT_REPLY, new String[]{topic, summary});

            //result completed
            setResult(RESULT_OK,intent);
            }
        //return to main
        finish();
        }
    }