package com.example.reminder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reminder.DetailActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.MemoAdapter;
import com.example.reminder.background.QueryViewModel;
import com.example.reminder.database.room.Memo;

public class AllFragment extends Fragment implements MemoAdapter.ClickListener{
    private MemoAdapter memoAdapter;
    public static final String TOPIC_STRING     = "TOPIC";
    public static final String SUMMARY_STRING   = "SUMMARY";
    public static final String ID_INT           = "ID";

    public AllFragment(){}

    public AllFragment(MemoAdapter memoAdapter) {
        this.memoAdapter = memoAdapter;
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
        recyclerView.setAdapter(memoAdapter);

        //listener
        memoAdapter.setOnCheckedListener(this);
    }

    /**Implementations for Listener of the MemoAdapter. This is responsible for the click. */
    @Override
    public void onItemClick(View view, int position) {

        Intent det_intent = new Intent(getContext(), DetailActivity.class);

        //get a clicked memo
        Memo clicked_memo = memoAdapter.getMemoAtPosition(position);

        //put some arguments
        det_intent.putExtra(TOPIC_STRING,clicked_memo.getTopic());
        det_intent.putExtra(SUMMARY_STRING,clicked_memo.getSummary());
        det_intent.putExtra(ID_INT,clicked_memo.getId());

        //start
        startActivity(det_intent);
    }

    /**Implementations for Listener of the MemoAdapter. This is responsible for the check. */
    @Override
    public void onChecked(View view, int position, boolean isChecked) {

        if(!isChecked) return;

        //notify that a memo is completed
        Toast.makeText(getActivity(), "リマインダーを完了しました。", Toast.LENGTH_SHORT).show();

        //retrieve a memo
        Memo memo = memoAdapter.getMemoAtPosition(position);

        //retrieve the ViewModel scoped to the MainActivity( RequireActivity() retrieves parent application instances )
        QueryViewModel viewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);

        //when checked the box, it is updated to add new value of completed
        memo.setCompleted(true);
        viewModel.update(memo);
    }
}