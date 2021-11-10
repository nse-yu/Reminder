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
import java.lang.String;

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
            //get memo
            Memo current = memos.get(position);
            //restrict summary length to show in the recycler view holder
            String sub_summary;
            if(current.getSummary().length() > 40)
                sub_summary = current.getSummary().substring(0,35) + "...";
            else
                sub_summary = current.getSummary();

            //set text to textview in recyclerview
            holder.text_topic.setText(current.getTopic());
            holder.text_summary.setText(sub_summary);
            holder.checkBox.setChecked(current.isCompleted());

        }else{
            holder.text_topic.setText(R.string.topic_nothing);
            holder.text_summary.setText(R.string.summary_nothing);
        }
    }

    public void setMemos(List<Memo> memos){
        Log.d("SET MEMOS","S E T M E M O S");
        this.memos = memos;
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
        private final CheckBox checkBox;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            text_topic = itemView.findViewById(R.id.text_topic);
            text_summary = itemView.findViewById(R.id.text_summary);
            checkBox = itemView.findViewById(R.id.checkbox);

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

    @Override
    public boolean onFailedToRecycleView(@NonNull MemoViewHolder holder) {
        Log.d("ON FAILED TO","O N F A I L E D T O");
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("ON DETACHED","O N D E T A C H E D");
    }

    @Override
    public void onViewRecycled(@NonNull MemoViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d("ON VIEW RECYCLED","O N V I E W R E C Y C L E D");
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        Log.d("UNREGISTER","U N R E G I S T E R");
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d("ON ATTACHED","O N A T T A C H E D");
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MemoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("ON VIEW ATTACHED","O N V I E W A T T A C H E D");
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        Log.d("SET HAS STABLE IDS","S T A B L E I D S");
    }
}
