package com.example.reminder.adapter;

import android.content.Context;
import android.util.Log;
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

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onChecked(View view,int position,boolean isChecked);
    }
    //field
    private List<Memo> memos;
    private static ClickListener clickListener;
    private final LayoutInflater inflater;

    //constructor
    public MemoAdapter(Context context){
        Log.d("CREATE ADAPTER","C R E A T E A D A P T E R");
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ON CREATE VIEW HOLDER","O N C R E A T E V I E W H O L D E R");

        View itemView = inflater.inflate(R.layout.recyclerview_items,parent,false);
        return new MemoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Log.d("ON BIND VIEW HOLDER","O N B I N D V I E W H O L D E R");

        if(memos != null){
            Memo current = memos.get(position);
            holder.text_topic.setText(current.getTopic());
            holder.text_summary.setText(current.getSummary());
        }else{
            holder.text_topic.setText(R.string.topic_nothing);
            holder.text_summary.setText(R.string.summary_nothing);
        }
    }

    public void setMemos(List<Memo> memos){
        this.memos = memos;
        if (memos != null)
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(memos != null)
            return memos.size();
        return 0;
    }

    public Memo getMemoAtPosition(int position){
        return memos.get(position);
    }

    public void setOnCheckedListener(ClickListener listener){clickListener = listener;}

    public static class MemoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private final TextView text_topic;
        private final TextView text_summary;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            text_topic = itemView.findViewById(R.id.text_topic);
            text_summary = itemView.findViewById(R.id.text_summary);
            CheckBox checkBox = itemView.findViewById(R.id.checkbox);

            //set the listener
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("ON CLICK","O N C L I C K");

            clickListener.onItemClick(view,getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d("ON CHECKED CHANGE","O N C H E C K E D C H A N G E");

            clickListener.onChecked(compoundButton, getAdapterPosition(),b);
        }
    }
}
