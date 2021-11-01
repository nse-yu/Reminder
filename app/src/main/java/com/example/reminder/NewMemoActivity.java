package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reminder.databinding.ActivityNewMemoBinding;

public class NewMemoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText edit_topic;
    private EditText edit_summary;
    public static final String EDIT_REPLY = "EDIT";
    com.example.reminder.databinding.ActivityNewMemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        edit_topic = binding.editTopic;
        edit_summary = binding.editSummary;

        //listener
        binding.save.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
        edit_topic.addTextChangedListener(this);

        //onCreate時にはedit_topicは空確定なので、ボタンを無効にする
        if(TextUtils.isEmpty(edit_topic.getText().toString())) {
            Button save_button = binding.save;
            save_button.setTextColor(getResources().getColor(R.color.mediumslateblue_dark));
            save_button.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        final int id = view.getId();

        if(id == R.id.save) {
            //get inputted values
            String topic = edit_topic.getText().toString();
            String summary = edit_summary.getText().toString();
            //put string array
            intent.putExtra(EDIT_REPLY, new String[]{topic, summary});

            //result completed
            setResult(RESULT_OK, intent);
        }else if(id == R.id.cancel){
            setResult(RESULT_CANCELED,intent);
        }
        //return to main
        finish();
    }

    //before text has been changed
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("BEFORE TEXT CHANGED","B E F O R E T E X T C H A N G E D");
    }

    //between text has been changing
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("ON TEXT CHANGED","O N T E X T C H A N G E D");
    }

    //after text has been changed
    @Override
    public void afterTextChanged(Editable editable) {
        Log.d("AFTER TEXT CHANGED","A F T E R T E X T C H A N G E D");
        Button save_button = binding.save;

        if(TextUtils.isEmpty(editable)) {
            save_button.setTextColor(getResources().getColor(R.color.mediumslateblue_dark));
            save_button.setClickable(false);
        }
        else {
            save_button.setTextColor(getResources().getColor(R.color.mediumslateblue));
            save_button.setClickable(true);
        }
    }
}