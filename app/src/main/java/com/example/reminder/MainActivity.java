package com.example.reminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.adapter.TabAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MemoAdapter.ClickListener{
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private MemoViewModel viewModel;
    public static final String UPDATE = "update";
    public static final int REQUEST_UPDATE = 10;
    public static final int REQUEST_INSERT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //initialize
        viewPager = binding.viewpager;
        TabLayout tabLayout = setTabLayout();
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MemoViewModel.class);
        //initialize two adapters
        TabAdapter tab_adapter = new TabAdapter(getSupportFragmentManager(),getLifecycle(),
                tabLayout.getTabCount());
        //Set a new adapter to provide page views on demand.
        viewPager.setAdapter(tab_adapter);

        //listener
        tabLayout.addOnTabSelectedListener(new TabListenerClass());

        //set the observer
        viewModel.selectAll().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(List<Memo> memos) {
                MemoAdapter memoAdapter = AllFragment.getMemoAdapter();
                if(memos != null)
                    memoAdapter.setMemos(memos);
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int itemID = item.getItemId();

        if(itemID == R.id.options){
            Intent options_intent = new Intent(this,SettingsActivity.class);
            startActivity(options_intent);
        }else if(itemID == R.id.append){
            Intent append_intent = new Intent(this,NewMemoActivity.class);
            startActivityForResult(append_intent,REQUEST_INSERT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_INSERT && resultCode == RESULT_OK){

            if (data != null) {
                String[] content = data.getStringArrayExtra(NewMemoActivity.EDIT_REPLY);
                Memo memo = new Memo(content[0], content[1]);
                viewModel.insert(memo);
            }
        }else if(requestCode == REQUEST_UPDATE && resultCode == RESULT_OK){
            //updateには主キーの断定が必須
        }else{
            Log.d("ERROR_RESULT","result bad");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //start new activity to show a memo
    }

    private class TabListenerClass implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
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