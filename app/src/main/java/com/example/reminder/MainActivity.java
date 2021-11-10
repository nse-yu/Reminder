package com.example.reminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.adapter.TabAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityMainBinding;
import com.example.reminder.menu.completed.CompletedActivity;
import com.example.reminder.menu.SettingsActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private static MemoViewModel viewModel;
    public static final int REQUEST_INSERT = 1;
    public static final String COMPLETED_COUNT = "COMPLETED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle(R.string.app_all);
        setSupportActionBar(binding.toolbar);

        //initialize
        viewPager = binding.viewpager;
        TabLayout tabLayout = setTabLayout();
        
        //initialize adapter
        TabAdapter tab_adapter = new TabAdapter(getSupportFragmentManager(),getLifecycle(),
                tabLayout.getTabCount());

        //restrict swiping
        viewPager.setUserInputEnabled(false);

        //Set a new adapter to provide page views on demand.
        viewPager.setAdapter(tab_adapter);

        //listener
        tabLayout.addOnTabSelectedListener(new TabListenerClass());
        
        //create a ViewModel
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MemoViewModel.class);

        //set the observer
        viewModel.selectAll().observe(this, memos -> {
            Log.d("OBSERVE CHANGED", "MAIN O B S E R V E C H A N G E D count = " + memos.size());

            MemoAdapter memoAdapter = AllFragment.getMemoAdapter();
            List<Memo> new_list = new ArrayList<>();

            /*isCompletedによって、完了リストへ移動するものとそうでないものをわける
            * データベース上はどちらも存在するが、recyclerview上ではnot completedのみ表示*/
            for(int i = 0;i < memos.size();i++){
                if(!(memos.get(i).isCompleted()))
                    new_list.add(memos.get(i));
            }
            //adapt the new list of memo that excludes the completed memo
            memoAdapter.setMemos(new_list);
        });
    }

    private TabLayout setTabLayout(){
        TabLayout tabLayout = binding.tabLayout;
        //tab数はここで決まる
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_all)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_reminder)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_todo)));

        return tabLayout;
    }

    public static MemoViewModel getMemoViewModel(){
        return viewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int itemID = item.getItemId();

        if(itemID == R.id.menu_options){
            Intent options_intent = new Intent(this, SettingsActivity.class);
            startActivity(options_intent);
        }else if(itemID == R.id.menu_append){
            Intent append_intent = new Intent(this,NewMemoActivity.class);
            startActivityForResult(append_intent,REQUEST_INSERT);
        }else if(itemID == R.id.menu_complete){
            Intent completed_intent = new Intent(this, CompletedActivity.class);
            startActivity(completed_intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //only insert query and if the action is "insert", should put third argument "false"
        if(requestCode == REQUEST_INSERT && resultCode == RESULT_OK){
            //data is not null
            if (data != null) {
                String[] content = data.getStringArrayExtra(NewMemoActivity.EDIT_REPLY);
                Memo memo = new Memo(content[0], content[1],false);
                viewModel.insert(memo);
            }
        }else{
            Log.d("RESULT CANCELED","R E S U L T C A N C E L E D");
        }
    }

    private class TabListenerClass implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d("ON TAB SELECTED","O N T A B S E L E C T E D"+tab.getPosition());

            int position = tab.getPosition();
            if(position == 0)
                binding.toolbar.setTitle(R.string.app_all);
            else if(position == 1)
                binding.toolbar.setTitle(R.string.app_reminder);
            else
                binding.toolbar.setTitle(R.string.app_todo);
            //多分この後にcreateFragmentがadapterで呼び出される
            viewPager.setCurrentItem(position);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
