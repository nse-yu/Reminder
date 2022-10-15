package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reminder.databinding.ActivityNewMemoBinding;

public class NewMemoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private ActivityNewMemoBinding binding;
    private EditText edit_topic;
    private EditText edit_summary;
    public static final String EDIT_REPLY = "EDIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ActivityNewMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        edit_topic      = binding.editTopic;
        edit_summary    = binding.editSummary;

        //listener
        binding.save.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
        edit_topic.addTextChangedListener(this);

        //onCreate時にはedit_topicはほぼ空確定なので、ボタンを無効にする
        if(TextUtils.isEmpty(edit_topic.getText().toString())) {

            Button save_button = binding.save;

            save_button.setTextColor(getColor(R.color.mediumslateblue_dark));
            save_button.setClickable(false);
        }

        //edittext
        prepareEditText();
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

            //result canceled
            setResult(RESULT_CANCELED,intent);

        }

        //return to main
        finish();
    }

    @Override
    public void afterTextChanged(Editable editable) {

        Button save_button = binding.save;

        if(TextUtils.isEmpty(editable)) {

            save_button.setTextColor(getColor(R.color.mediumslateblue_dark));
            save_button.setClickable(false);

            return;
        }

        save_button.setTextColor(getColor(R.color.medium_slate_blue));
        save_button.setClickable(true);
    }

    private void prepareEditText(){

        Bundle bundle = getIntent().getExtras();

        //whether to set the text on EditText or not based on the action "update"
        if (bundle != null && bundle.containsKey(DetailActivity.EDIT_UPDATE)) {

            String[] edit_text = bundle.getStringArray(DetailActivity.EDIT_UPDATE);

            edit_topic  .setText(edit_text[0]);
            edit_summary.setText(edit_text[1]);
        }

        //focus on the edit text right end
        edit_topic.setSelection(edit_topic.getText().toString().length());
        edit_topic.requestFocus();
    }


    //before text has been changed
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    //between text has been changing
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

}