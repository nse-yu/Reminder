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
import android.widget.Toast;


import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.database.MemoViewModel;
import com.example.reminder.database.room.Memo;

public class AllFragment extends Fragment implements MemoAdapter.ClickListener{
    private static MemoAdapter memo_adapter;
    public static final String TOPIC_STRING = "TOPIC";
    public static final String SUMMARY_STRING = "SUMMARY";
    public static final String ID_INT = "ID";

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

    /**This method is used to give a MemoAdapter instance to an activity of the outside
     * */
    public static MemoAdapter getMemoAdapter(){
        return memo_adapter;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("ON ITEM CLICK", "O N I T E M C L I C K : position = "+position);

        Intent det_intent = new Intent(getContext(),DetailActivity.class);
        //get a clicked memo
        Memo clicked_memo = memo_adapter.getMemoAtPosition(position);
        //put some arguments
        Log.d("TOPIC",clicked_memo.getTopic());
        det_intent.putExtra(TOPIC_STRING,clicked_memo.getTopic());
        Log.d("SUMMARY",clicked_memo.getSummary());
        det_intent.putExtra(SUMMARY_STRING,clicked_memo.getSummary());
        det_intent.putExtra(ID_INT,clicked_memo.getId());
        //start
        startActivity(det_intent);
    }

    @Override
    public void onChecked(View view, int position, boolean isChecked) {
        if(isChecked) {
            Log.d("IS CHECKED == TRUE", "I S C H E C K E D = = T R U E : position = "+position);
            Toast.makeText(getActivity(), "リマインダーを完了しました。", Toast.LENGTH_SHORT).show();

            Memo memo = memo_adapter.getMemoAtPosition(position);
            MemoViewModel viewModel = MainActivity.getMemoViewModel();
            //when checked the box, it is updated to add new value of completed
            memo.setCompleted(true);
            viewModel.update(memo);
        }
    }
}