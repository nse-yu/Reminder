package com.example.reminder.database;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.reminder.database.room.Memo;

import java.util.List;

public class MemoViewModel extends AndroidViewModel {
    private final MemoRepository repository;
    private final LiveData<List<Memo>> memo_list;

    public MemoViewModel(@NonNull Application application) {
        super(application);
        //initialize
        repository = new MemoRepository(application);
        memo_list = repository.selectAll();
    }

    public LiveData<List<Memo>> selectAll(){return memo_list;}

    //this is the root SQL query
    public void insert(Memo memo){repository.insert(memo);}
    public void update(Memo memo){repository.update(memo);}
    public void delete(Memo memo){repository.delete(memo);}
    public void deleteAll(){repository.deleteAll();}
    public void countCompleted(Activity activity){repository.countCompleted(activity);}
}
