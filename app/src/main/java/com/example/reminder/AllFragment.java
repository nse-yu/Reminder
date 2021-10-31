package com.example.reminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;
import com.example.reminder.databinding.FragmentAllBinding;

import java.util.List;

public class AllFragment extends Fragment {
    private FragmentAllBinding binding;
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
        Log.d("AAAAAAAAAAAAAAAA","AAAAAAAAAAAAA");
        //initialize
        binding = FragmentAllBinding.inflate(getLayoutInflater());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //apply some effect to recyclerview
        memo_adapter = new MemoAdapter(getActivity());
        recyclerView.setAdapter(memo_adapter);
    }

    public static MemoAdapter getMemoAdapter(){
        return memo_adapter;
    }
}