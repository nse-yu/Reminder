package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;

public class AllFragment extends Fragment implements MemoAdapter.ClickListener{
    private static MemoAdapter memo_adapter;

    public AllFragment() {
        // Required empty public constructor
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

        //initialize(viewはfragment_all.xmlのこと)
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //apply some effect to recyclerview
        memo_adapter = new MemoAdapter(getActivity());
        recyclerView.setAdapter(memo_adapter);

        //listener
        memo_adapter.setOnCheckedListener(this);
    }

    public static MemoAdapter getMemoAdapter(){
        return memo_adapter;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("ON ITEM CLICK", "O N I T E M C L I C K");

        Intent det_intent = new Intent(getContext(),DetailActivity.class);
        startActivity(det_intent);
    }

    @Override
    public void onChecked(View view, int position, boolean isChecked) {
        if(isChecked) {
            Log.d("IS CHECKED == TRUE", "I S C H E C K E D = = T R U E");

            Memo memo = memo_adapter.getMemoAtPosition(position);
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            viewModel.delete(memo);
        }
    }
}