package com.example.reminder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.database.room.LocationCodes;

import java.util.ArrayList;

public class AllFragment extends Fragment {
    private MemoAdapter memoAdapter;

    public AllFragment(){}

    public AllFragment(MemoAdapter memoAdapter) {
        this.memoAdapter = memoAdapter;
        this.memoAdapter.setLocation(LocationCodes.All);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //[RecyclerView]
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(memoAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(memoAdapter == null) return;

        MainActivity.tabPosition = LocationCodes.All.code;
        memoAdapter.setLocation(LocationCodes.All);
        memoAdapter.setFilteredMemos(new ArrayList<>());
    }
}