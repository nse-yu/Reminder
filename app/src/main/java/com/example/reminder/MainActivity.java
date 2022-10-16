package com.example.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.viewpager2.widget.ViewPager2;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.adapter.TabAdapter;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.contracts.NewMemoContract;
import com.example.reminder.database.room.LocationCodes;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityMainBinding;
import com.example.reminder.menu.SettingsActivity;
import com.example.reminder.menu.completed.CompletedActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements
        MemoAdapter.LongClickListener,
        View.OnClickListener,
        MemoAdapter.ClickListener,
        DialogInterface.OnClickListener
{

    private ActivityMainBinding     binding;
    private ViewPager2              viewPager;
    private MemoAdapter             memoAdapter;
    private static QueryViewModel   queryViewModel;
    public String[]                 tabNames;

    public static Boolean           isLongTapped = false;
    private final ActivityResultLauncher<Integer> launcher = registerForActivityResult(
            new NewMemoContract(),
            result -> {
                if(result.length != 3)
                    return;

                queryViewModel.insert(
                        new Memo(result[0], result[1], false, Integer.parseInt(result[2]))
                );
                Toast.makeText(this, "リマインダーを保存しました", Toast.LENGTH_SHORT).show();
            }
    );

    private final List<Memo>        tmpMemos = new ArrayList<>();

    private final List<View>        tmpViews = new ArrayList<>();

    public static final int     REQUEST_INSERT  = 1;
    public static final String  TOPIC_STRING    = "TOPIC";
    public static final String  SUMMARY_STRING  = "SUMMARY";
    public static final String  ID_INT          = "ID";
    public static final String  LOCATION_INT    = "LOCATION";

    public static int tabPosition = LocationCodes.All.code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //===============Initialization===============//
        tabNames = new String[]{
                getString(R.string.tab_all),
                getString(R.string.tab_reminder),
                getString(R.string.tab_todo)
        };

        //================View Settings================//

        //[ToolBar] set the toolbar default strings
        binding.toolbar.setTitle(R.string.app_all);
        setSupportActionBar(binding.toolbar);


        //[MemoAdapter]
        memoAdapter = new MemoAdapter();
        memoAdapter.setOnLongClickListener(this);
        memoAdapter.setOnCheckedListener(this);


        //[ViewPager2]
        viewPager = binding.viewpager;
        viewPager.setUserInputEnabled(false); //restrict swiping
        viewPager.setAdapter(
                new TabAdapter(getSupportFragmentManager(), getLifecycle(), tabNames.length, memoAdapter)
        );

        //[TabLayout] links TabLayout to ViewPager2
        binding.tabLayout.addOnTabSelectedListener(new TabListenerClass());
        new TabLayoutMediator(binding.tabLayout, viewPager, (tab, position) -> tab.setText(tabNames[position])).attach();


        //[ViewModel]
        queryViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(new ViewModelInitializer<>(
                        QueryViewModel.class,
                        creationExtras -> new QueryViewModel(getApplication())
                ))
        ).get(QueryViewModel.class);

        queryViewModel.getMemos().observe(this, getListObserver());
    }

    /**Observes all memos and add memos without "isCompleted" status to the RecyclerView using memoAdapter. */
    @NotNull
    private Observer<List<Memo>> getListObserver() {

        return all -> {

            //adapt the new list of memo that excludes the completed memo
            memoAdapter.setMemos(
                    all.stream()
                            .filter(memo -> !memo.isCompleted())
                            .collect(Collectors.toList())
            );

            memoAdapter.setFilteredMemos(
                    all.stream()
                            .filter(memo -> !memo.isCompleted())
                            .filter(memo -> memo.getLocation() == tabPosition)
                            .collect(Collectors.toList())
            );

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflates the menu
        getMenuInflater().inflate(R.menu.initial, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final int itemID = item.getItemId();
        Intent intent;

        if(itemID == R.id.menu_append) {
            launcher.launch(REQUEST_INSERT);
            return true;
        }

        if(itemID == R.id.menu_delete){

            //to show alert
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.alert_delete);

            //listener
            alertBuilder.setPositiveButton(R.string.button_delete,this);
            alertBuilder.setNegativeButton(R.string.button_cancel,this);

            //show the alert
            alertBuilder.show();

            return true;
        }

        if(itemID == R.id.menu_options){

            intent = new Intent(this, SettingsActivity.class);

        }
        else if(itemID == R.id.menu_complete) {

            intent = new Intent(this, CompletedActivity.class);

        }
        else{

            return super.onOptionsItemSelected(item);

        }
        startActivity(intent);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(!isLongTapped) return super.onPrepareOptionsMenu(menu);

        menu.clear();
        getMenuInflater().inflate(R.menu.contextual, menu);

        return true;
    }

    /**Implementations for LongClickListener of the MemoAdapter. This is responsible for the click. */
    @Override
    public void onItemLongClick(View view, int adapterPosition) {

        //indicate that certain items are clicked for a long time [long-tapped mode]
        isLongTapped = true;

        //set close button to cancel this operation
        binding.toolbar.setNavigationIcon(R.drawable.ic_close);
        binding.toolbar.setNavigationOnClickListener(this);

        //notify the system that the menu will be re-drawn, then onPreparedOptionsMenu is called
        invalidateOptionsMenu();

    }

    /**This onClick() is the implementation of ToolBar's setNavigationOnClickListener. */
    @Override
    public void onClick(View view) {

        exitLongTappedMode();

    }

    /**Implementations for ClickListener of the MemoAdapter. This is responsible for the click. */
    @Override
    public void onItemClick(View view, int position) {

        //with long-tapped mode
        if(isLongTapped) {
            onItemClickInLongTappedMode(view, position);
            return;
        }

        Intent toDetail = new Intent(this, DetailActivity.class);

        //get a clicked memo
        Memo clickedMemo = memoAdapter.getMemoAtPosition(position);

        //put some arguments
        toDetail.putExtra(TOPIC_STRING,     clickedMemo.getTopic());
        toDetail.putExtra(SUMMARY_STRING,   clickedMemo.getSummary());
        toDetail.putExtra(ID_INT,           clickedMemo.getId());
        toDetail.putExtra(LOCATION_INT,     clickedMemo.getLocation());

        //start
        startActivity(toDetail);
    }

    /**Implementations for ClickListener of the MemoAdapter. This is responsible for the check. */
    @Override
    public void onChecked(View view, int position, boolean isChecked) {

        if(isLongTapped) {
            ((CheckBox) view).setChecked(false);
            return;
        }

        if(!isChecked) return;

        //notify that a memo is completed
        Toast.makeText(this, "リマインダーを完了しました。", Toast.LENGTH_SHORT).show();

        //retrieve a memo
        Memo memo = memoAdapter.getMemoAtPosition(position);


        //when checked the box, it is updated to add new value of completed
        memo.setCompleted(true);
        queryViewModel.update(memo);
    }

    /**Implementations for DialogInterface.OnClickListener. This is responsible for the check. */
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        //cancellation
        if(i != DialogInterface.BUTTON_POSITIVE) return;

        //delete selected memos
        tmpMemos.forEach(queryViewModel::delete);

        Toast.makeText(this, String.format("%s件のリマインダーを削除しました", tmpMemos.size()), Toast.LENGTH_SHORT).show();

        exitLongTappedMode();
    }

    public void onItemClickInLongTappedMode(View view, int position){

        Memo memo;
        int boxId;

        if(tmpMemos.contains(memo = (memoAdapter.getMemoAtPosition(position)))){

            //retrieve a clicked memo identified by position and remove it from the temporary list of memos
            tmpMemos.remove(memo);
            tmpViews.remove(view);

            boxId = R.drawable.box_normal;

        }else{

            //retrieve a clicked memo identified by position and add it to the temporary list of memos
            tmpMemos.add(memo);
            tmpViews.add(view);

            boxId = R.drawable.box_selected;

        }

        view.setBackground(AppCompatResources.getDrawable(this, boxId));
        binding.toolbar.setTitle(String.valueOf(tmpMemos.size()));
    }

    private void exitLongTappedMode(){

        //finish long-tapped mode
        isLongTapped = false;
        tmpMemos.clear();
        tmpViews.forEach(v -> v.setBackground(AppCompatResources.getDrawable(this, R.drawable.box_normal)));
        tmpViews.clear();
        binding.toolbar.setTitle(tabNames[tabPosition]);
        binding.toolbar.setNavigationIcon(null);

        invalidateOptionsMenu();
    }

    private class TabListenerClass implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            //set ToolBar the name of pages
            binding.toolbar.setTitle(tabNames[tab.getPosition()]);

            //多分この後にcreateFragmentがadapterで呼び出される
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }
        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
