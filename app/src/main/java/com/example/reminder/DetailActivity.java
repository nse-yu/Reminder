package com.example.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.contracts.DetailContract;
import com.example.reminder.database.room.LocationCodes;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    //memo's contents id is essential to update specific memo
    private String  topic;
    private String  summary;
    private int     id;
    private int     location;
    private QueryViewModel queryViewModel;
    public  final   static String   EDIT_UPDATE     = "update";

    private final ActivityResultLauncher<String[]> launcher = registerForActivityResult(
            new DetailContract(),
            result -> {

                if(result.length == 3)
                    queryViewModel.update(
                            new Memo(id, result[0], result[1], Integer.parseInt(result[2]))
                    );

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

        //[Intent]
        topic   = getIntent().getStringExtra(MainActivity.TOPIC_STRING);
        summary = getIntent().getStringExtra(MainActivity.SUMMARY_STRING);
        id      = getIntent().getIntExtra(MainActivity.ID_INT,-1);
        location= getIntent().getIntExtra(MainActivity.LOCATION_INT, 1);

        //[TextView]
        binding.showTopic   .setText(topic);
        binding.showSummary .setText(summary);
        binding.selectableLocation.setLocation(LocationCodes.toString(this, location));

        //[Button]
        binding.buttonEdit      .setOnClickListener(this);
        binding.buttonDelete    .setOnClickListener(this);
        binding.buttonComplete  .setOnClickListener(this);
        binding.linerShow       .setOnClickListener(this);

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

    //onclick for update one memo's contents
    @Override
    public void onClick(View view) {

        final int id = view.getId();

        if(id == R.id.button_edit || id == R.id.liner_show) {

            launcher.launch(new String[]{topic, summary, String.valueOf(location)});

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

            queryViewModel.toggleCompletedState(this.id, true);

            returnMain();
        }
    }

    //onclick for alert and delete
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i != DialogInterface.BUTTON_POSITIVE) return;

        //delete a memo
        queryViewModel.delete(
                new Memo(id, topic, summary)
        );

        //return to main activity
        returnMain();
    }

    public void returnMain(){

        //return to MainActivity
        Intent return_intent = new Intent(this, MainActivity.class);
        startActivity(return_intent);
    }
}