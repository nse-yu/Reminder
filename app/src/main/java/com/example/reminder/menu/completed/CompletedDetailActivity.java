package com.example.reminder.menu.completed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import com.example.reminder.R;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityCompletedDetailBinding;

public class CompletedDetailActivity extends AppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {

    private int     id;
    private String  topic;
    private String  summary;

    private QueryViewModel queryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        ActivityCompletedDetailBinding binding = ActivityCompletedDetailBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //[Intent] initializes using intents from the CompletedReminderActivity
        this.id         = getIntent().getIntExtra(CompletedReminderActivity.ID_INT, -1);
        this.topic      = getIntent().getStringExtra(CompletedReminderActivity.TOPIC_STRING);
        this.summary    = getIntent().getStringExtra(CompletedReminderActivity.SUMMARY_STRING);


        //[TextView]
        binding.cmpShowTopic    .setText(topic);
        binding.cmpShowSummary  .setText(summary);

        //[Button]
        binding.cmpButtonDelete     .setOnClickListener(this);
        binding.cmpButtonResetting  .setOnClickListener(this);

        //[QueryViewModel]
        queryViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(
                        new ViewModelInitializer<>(
                                QueryViewModel.class,
                                creationExtras -> new QueryViewModel(getApplication())
                        )
                )
        ).get(QueryViewModel.class);
    }

    @Override
    public void onClick(View view) {

        int btnId = view.getId();

        if(btnId == R.id.cmp_button_delete) {

            //to show alert
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.alert_delete);

            //listener
            alertBuilder.setPositiveButton(R.string.button_delete, this);
            alertBuilder.setNegativeButton(R.string.button_cancel, this);

            //show the alert
            alertBuilder.show();

        }else if(btnId == R.id.cmp_button_resetting){

            //changes "completed" state of memos to false( not completed, default state )
            queryViewModel.toggleCompletedState(id, false);

            //notify that a memo is reassigned
            Toast.makeText(this, R.string.message_resetting, Toast.LENGTH_SHORT).show();

            //return to the CompletedReminderActivity
            returnPresentActivity();

        }
    }

    //this is the method for the delete button
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        if(i != DialogInterface.BUTTON_POSITIVE)
            return;

        //delete a memo
        Memo delete_memo = new Memo(id, topic, summary);
        queryViewModel.delete(delete_memo);

        returnPresentActivity();
    }

    public void returnPresentActivity(){

        //return to main activity
        Intent return_intent = new Intent(this, CompletedReminderActivity.class);
        startActivity(return_intent);
    }
}