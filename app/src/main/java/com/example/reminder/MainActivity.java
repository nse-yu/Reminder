package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.viewpager2.widget.ViewPager2;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.adapter.TabAdapter;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.contracts.NewMemoContract;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.ActivityMainBinding;
import com.example.reminder.menu.SettingsActivity;
import com.example.reminder.menu.completed.CompletedActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    //view binding
    private ActivityMainBinding     binding;

    //enables to use RecyclerView
    private ViewPager2              viewPager;

    private MemoAdapter             memoAdapter;

    //
    private static QueryViewModel   viewModel;

    public String[]                 tabNames;

    private final ActivityResultLauncher<Integer> launcher = registerForActivityResult(
            new NewMemoContract(),
            result -> {
                Memo memo = new Memo(result[0], result[1], false);
                viewModel.insert(memo);
            }
    );

    //
    public static final int REQUEST_INSERT = 1;



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


        //[ViewPager2]
        viewPager = binding.viewpager;
        viewPager.setUserInputEnabled(false); //restrict swiping
        viewPager.setAdapter(
                new TabAdapter(getSupportFragmentManager(), getLifecycle(), tabNames.length, memoAdapter)
        );

        //[TabLayout] links TabLayout to ViewPager2
        binding.tabLayout.addOnTabSelectedListener(new TabListenerClass());
        new TabLayoutMediator(binding.tabLayout, viewPager, (tab, position) -> tab.setText(tabNames[position])).attach();


        //[ViewModel] TODO:用途を記述
        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(new ViewModelInitializer<>(
                        QueryViewModel.class,
                        creationExtras -> new QueryViewModel(getApplication())
                ))
        ).get(QueryViewModel.class);

        viewModel.getMemos().observe(this, getListObserver());
    }

    /**Observes all memos and add memos without "isCompleted" status to the RecyclerView using memoAdapter. */
    @NotNull
    private Observer<List<Memo>> getListObserver() {

        return all -> {

            //adapt the new list of memo that excludes the completed memo
            memoAdapter.setMemos(
                    all.stream().filter(memo -> !memo.isCompleted()).collect(Collectors.toList())
            );

        };
    }

    public static QueryViewModel getMemoViewModel(){
        return viewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflates the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final int itemID = item.getItemId();
        Intent intent = null;

        if(itemID == R.id.menu_options){

            intent = new Intent(this, SettingsActivity.class);

        }else if(itemID == R.id.menu_append){

            launcher.launch(REQUEST_INSERT);
            return super.onOptionsItemSelected(item);

        }else if(itemID == R.id.menu_complete){

            intent = new Intent(this, CompletedActivity.class);

        }

        startActivity(intent);

        return super.onOptionsItemSelected(item);
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
