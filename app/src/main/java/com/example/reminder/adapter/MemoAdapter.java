package com.example.reminder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.database.room.Memo;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onChecked(View view, int position, boolean isChecked);
    }

    //field
    private List<Memo> memos = new ArrayList<>();
    private static ClickListener clickListener;


    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MemoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

        /*
        if(memos.isEmpty()){

            holder.text_topic.setText(R.string.topic_nothing);
            holder.text_summary.setText(R.string.summary_nothing);

            return;
        }

         */

        //get a memo
        Memo current = memos.get(position);

        //restrict summary length to show in the recycler view holder
        String sub_summary = current.getSummary();

        if(current.getSummary().length() > 40)
            sub_summary = current.getSummary().substring(0,35) + "...";

        //set text to textview in recyclerview
        holder.text_topic   .setText(current.getTopic());
        holder.text_summary .setText(sub_summary);
        holder.checkBox     .setChecked(current.isCompleted());
    }

    public void setMemos(List<Memo> memos){

        this.memos = memos;
        notifyDataSetChanged();
    }

    @Override
    public int  getItemCount() {

        if(memos == null)
            return 0;

        return memos.size();
    }

    public Memo getMemoAtPosition(int position){

        return memos.get(position);
    }

    /**SetOnCheckedListener requires listeners and allocates it for each View saved in the MemoViewHolder.
     * It is beneficial that caller(ex. Activity, Fragment that has the MemoAdapter) is solely responsible for listener implementation. */
    public void setOnCheckedListener(ClickListener listener){ clickListener = listener; }




    public static class MemoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
                
        private final TextView text_topic;
        private final TextView text_summary;
        private final CheckBox checkBox;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize all views
            text_topic      = itemView.findViewById(R.id.text_topic);
            text_summary    = itemView.findViewById(R.id.text_summary);
            checkBox        = itemView.findViewById(R.id.checkbox);

            //set listeners
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {

            //depends on caller implementations
            clickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            //depends on caller implementations
            clickListener.onChecked(compoundButton, getAdapterPosition(), b);
        }
    }
}
