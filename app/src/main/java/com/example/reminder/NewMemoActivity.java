package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reminder.database.room.LocationCodes;
import com.example.reminder.databinding.ActivityNewMemoBinding;
import com.example.reminder.view.LocationContextButton;

public class NewMemoActivity extends AppCompatActivity implements
        View.OnClickListener, TextWatcher {
    private ActivityNewMemoBinding binding;
    private EditText edit_topic;
    private EditText edit_summary;

    private LocationContextButton locationContextButton;

    private LocationCodes location = LocationCodes.REMINDER;

    public static final String EDIT_REPLY = "EDIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ActivityNewMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //[EditText]
        edit_topic              = binding.editTopic;
        edit_summary            = binding.editSummary;
        locationContextButton   = binding.selectableLocation;

        //[Button]
        binding.save    .setOnClickListener(this);
        binding.cancel  .setOnClickListener(this);
        edit_topic      .addTextChangedListener(this);
        registerForContextMenu(locationContextButton);

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

            //put string array
            intent.putExtra(EDIT_REPLY, new String[]{
                    edit_topic.getText().toString(),
                    edit_summary.getText().toString(),
                    String.valueOf(location.code)
            });

            //result completed
            setResult(RESULT_OK, intent);

        }else if(id == R.id.cancel){

            //result canceled
            setResult(RESULT_CANCELED, intent);

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

            String[] data = bundle.getStringArray(DetailActivity.EDIT_UPDATE);

            edit_topic  .setText(data[0]);
            edit_summary.setText(data[1]);
            location = LocationCodes.toLocationCodes(Integer.parseInt(data[2]));
            if (location != null) {
                locationContextButton.setLocation(LocationCodes.toString(this, location.code));
            }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.floating, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        final int itemId = item.getItemId();

        if(itemId == R.id.menu_reminder){

            locationContextButton.setLocation(getString(R.string.tab_reminder));
            location = LocationCodes.REMINDER;

        }else if(itemId == R.id.menu_todo){

            locationContextButton.setLocation(getString(R.string.tab_todo));
            location = LocationCodes.TODO;

        }else{
            return super.onContextItemSelected(item);
        }

        return true;
    }
}