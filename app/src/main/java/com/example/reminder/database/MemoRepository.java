package com.example.reminder.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.reminder.database.room.Memo;
import com.example.reminder.database.room.MemoDao;
import com.example.reminder.database.room.MemoDatabase;

import java.util.List;

public class MemoRepository {
    private MemoDao dao;
    private LiveData<List<Memo>> memo_list;

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

    private static class insertAsyncTask extends AsyncTask<Memo,Void,Void>{
        //field
        private MemoDao dao;
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
        private MemoDao dao;
        //constructor
        public updateAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Memo... memos) {
            dao.update(memos[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Memo,Void,Void>{
        //field
        private MemoDao dao;
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
        private MemoDao dao;
        //constructor
        public deleteAllAsyncTask(MemoDao dao){this.dao = dao;}

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
