package com.example.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.contracts.DetailContract;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityDetailBinding;
import com.example.reminder.fragment.AllFragment;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    //memo's contents id is essential to update specific memo
    private String  memo_topic;
    private String  memo_summary;
    private int     memo_id;

    public  final   static String   EDIT_UPDATE     = "update";

    private final ActivityResultLauncher<String[]> launcher = registerForActivityResult(
            new DetailContract(),
            result -> {
                QueryViewModel viewModel = MainActivity.getMemoViewModel();

                Memo memo = new Memo(memo_id, result[0], result[1]);
                viewModel.update(memo);

                //return to main activity
                returnMain();
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize
        memo_topic      = getIntent().getStringExtra(AllFragment.TOPIC_STRING);
        memo_summary    = getIntent().getStringExtra(AllFragment.SUMMARY_STRING);
        memo_id         = getIntent().getIntExtra(AllFragment.ID_INT,-1);

        //set text
        binding.showTopic   .setText(memo_topic);
        binding.showSummary .setText(memo_summary);

        //listener
        binding.buttonEdit      .setOnClickListener(this);
        binding.buttonDelete    .setOnClickListener(this);
        binding.buttonComplete  .setOnClickListener(this);
    }

    //onclick for update one memo's contents
    @Override
    public void onClick(View view) {

        final int id = view.getId();

        if(id == R.id.button_edit) {

            launcher.launch(new String[]{memo_topic, memo_summary});

            //startActivityForResult(update_intent, REQUEST_UPDATE);

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

            QueryViewModel viewModel = MainActivity.getMemoViewModel();

            //delete->同じMemoにcompleted属性をつけて再構成
            Memo completed_memo = new Memo(memo_id, memo_topic, memo_summary);
            viewModel.delete(completed_memo);
            completed_memo.setCompleted(true);
            viewModel.insert(completed_memo);

            returnMain();
        }
    }

    //onclick for alert and delete
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE) {
            //delete memo
            QueryViewModel viewModel = MainActivity.getMemoViewModel();
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