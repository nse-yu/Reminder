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
import com.example.reminder.database.room.LocationCodes;
import com.example.reminder.database.room.Memo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onChecked(View view, int position, boolean isChecked);
    }

    public interface LongClickListener{
        void onItemLongClick(View view, int adapterPosition);
    }

    //field
    private List<Memo> memos = new ArrayList<>();

    private List<Memo> filteredMemos = new ArrayList<>();

    private static ClickListener        clickListener;

    private static LongClickListener    longClickListener;

    public LocationCodes location = LocationCodes.All;


    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MemoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false)
        );

    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

        //get a memo
        Memo current = location == LocationCodes.All ? memos.get(position) : filteredMemos.get(position);

        //restrict summary length to show in the recycler view holder
        final String summary = current.getSummary();
        String copyOfSummary = summary;

        //to confirm that summary strings have CR+LF code
        int[] codePoints = IntStream.range(0, summary.length()).filter(i -> summary.codePointAt(i) == 10).toArray();

        if(codePoints.length > 0)
            copyOfSummary = copyOfSummary.substring(0, codePoints[0]);

        if(copyOfSummary.length() > 15)
            copyOfSummary = copyOfSummary.substring(0,15) + "...";

        //set text to textview in recyclerview
        holder.text_topic   .setText(current.getTopic());
        holder.text_summary .setText(copyOfSummary);
        holder.checkBox     .setChecked(current.isCompleted());
    }

    public void setMemos(List<Memo> memos){

        this.memos = memos;
        notifyDataSetChanged();

    }

    public void setFilteredMemos(List<Memo> filteredMemos){

        this.filteredMemos = filteredMemos;
        notifyDataSetChanged();

    }

    @Override
    public int  getItemCount() {

        if(memos == null)
            return 0;

        if(location == LocationCodes.All)
            return memos.size();

        return filteredMemos.size();
    }

    public void setLocation(LocationCodes location){
        this.location = location;
    }

    public Memo getMemoAtPosition(int position){

        if(location == LocationCodes.All)
            return memos.get(position);
        return filteredMemos.get(position);
    }

    public List<Memo> getMemos(){
        return memos;
    }

    public List<Memo> getFilteredMemos(){
        return filteredMemos;
    }

    /**SetOnCheckedListener requires listeners and allocates it for each View saved in the MemoViewHolder.
     * It is beneficial that caller(ex. Activity, Fragment that has the MemoAdapter) is solely responsible for listener implementation. */
    public void setOnCheckedListener(ClickListener listener){ clickListener = listener; }
    public void setOnLongClickListener(LongClickListener listener){ longClickListener = listener; }




    public static class MemoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {
                
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
            itemView.setOnLongClickListener(this);
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

        @Override
        public boolean onLongClick(View view) {

            longClickListener.onItemLongClick(view, getAdapterPosition());

            return false;
        }
    }
}
