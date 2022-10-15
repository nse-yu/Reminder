package com.example.reminder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.reminder.fragment.AllFragment;
import com.example.reminder.fragment.ReminderFragment;
import com.example.reminder.fragment.TodoFragment;

public class TabAdapter extends FragmentStateAdapter {
    private final int totalTabs;
    private final MemoAdapter memoAdapter;

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int totalTabs, MemoAdapter memoAdapter) {
        super(fragmentManager, lifecycle);

        this.totalTabs      = totalTabs;
        this.memoAdapter    = memoAdapter;
    }

    /**Provide a new Fragment associated with the specified position.
     The adapter will be responsible for the Fragment lifecycle:
     The Fragment will be used to display an item.
     The Fragment will be destroyed when it gets too far from the viewport,
     and its state will be saved. When the item is close to the viewport again,
     a new Fragment will be requested, and a previously saved state will be used to initialize it.
     * */
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0)
            return new AllFragment(memoAdapter);
        else if(position == 1)
            return new ReminderFragment();

        return new TodoFragment();
    }

    /**Returns the total number of items in the data set held by the adapter.*/
    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
