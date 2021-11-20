package com.example.reminder.database;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import androidx.lifecycle.LiveData;

import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.example.reminder.database.room.Memo;
import com.example.reminder.database.room.MemoDao;
import com.example.reminder.database.room.MemoDatabase;

import java.util.List;

public class MemoRepository {
    private final MemoDao dao;
    private final LiveData<List<Memo>> memo_list;

    public MemoRepository(Application application){
        //create and initialize database refer to the builder
        MemoDatabase database = MemoDatabase.getDatabase(application);
        //initialize
        dao = database.getMemoDao();
        memo_list = dao.selectAll();
    }

    public LiveData<List<Memo>> selectAll() {
        return memo_list;
    }

    public void insert(Memo memo){
        new insertAsyncTask(dao).execute(memo);
    }
    public void delete(Memo memo){
        new deleteAsyncTask(dao).execute(memo);
    }
    public void update(Memo memo){
        new updateAsyncTask(dao).execute(memo);
    }
    public void deleteAll(){
        new deleteAllAsyncTask(dao).execute();
    }
    public void countCompleted(Activity activity){ new countCompletedAsyncTask(dao,activity).execute();}

    private static class insertAsyncTask extends AsyncTask<Memo,Void,Void>{
        //field
        private final MemoDao dao;
        //constructor
        public insertAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Memo... memos) {
            dao.insert(memos[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Memo,Void,Void>{
        //field
        private final MemoDao dao;
        //constructor
        public updateAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Memo... memos) {
            Log.d("UPDATE IN BACKGROUND","U P D A T E");
            dao.update(memos[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Memo,Void,Void>{
        //field
        private final MemoDao dao;
        //constructor
        public deleteAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Memo... memos) {
            dao.delete(memos[0]);
            return null;
        }
    }
    private static class deleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        //field
        private final MemoDao dao;
        //constructor
        public deleteAllAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
    private static class countCompletedAsyncTask extends AsyncTask<Void,Void,Integer>{
        //field
        private final MemoDao dao;
        private final Activity activity;
        //constructor
        public countCompletedAsyncTask(MemoDao dao,Activity activity){
            this.dao = dao;
            this.activity = activity;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return dao.countCompleted();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Button button = activity.findViewById(R.id.reminder_completed_list);
            String current_text = button.getText().toString();
            String new_text = current_text + String.format("(%d)",integer);
            button.setText(new_text);
        }
    }
}
