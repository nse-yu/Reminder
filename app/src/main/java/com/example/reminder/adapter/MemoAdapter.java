package com.example.reminder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.database.room.Memo;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    public interface ClickListener{
        void onItemClick(View view,int position);
    }
    //field
    private List<Memo> memos;
    private static ClickListener clickListener;
    private final LayoutInflater inflater;

    //constructor
    public MemoAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.recyclerview_items,parent,false);
        return new MemoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

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

    public void setOnItemClickListener(ClickListener listener){
        clickListener = listener;
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView text_topic;
        private final TextView text_summary;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            text_topic = itemView.findViewById(R.id.text_topic);
            text_summary = itemView.findViewById(R.id.text_summary);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view,getAdapterPosition());
        }
    }
}
