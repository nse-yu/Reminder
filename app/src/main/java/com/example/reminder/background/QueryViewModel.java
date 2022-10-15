package com.example.reminder.background;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.reminder.database.room.Memo;

import java.util.List;

/**QueryViewModel is start-point to communicate with the Room database and stores all memos, completed count which is observable. */
public class QueryViewModel extends AndroidViewModel {
    private final QueryRepository           repo;
    private final LiveData<List<Memo>>      memos;
    private final LiveData<List<Integer>>   count;

    public enum QueryActions {
        INSERT(0),
        UPDATE(1),
        DELETE(2),
        DELETE_ALL(22);

        final int value;

        QueryActions(int value){ this.value = value; }
    }


    public QueryViewModel(@NonNull Application application) {
        super(application);

        this.repo   = new QueryRepository(application);
        this.count  = repo.countCompleted();
        this.memos  = repo.selectAll();
    }

    public LiveData<List<Integer>>  getCurrentCount() {
        return count;
    }

    public LiveData<List<Memo>>     getMemos(){
        return memos;
    }

    //this is the root SQL query
    public void insert(Memo memo){ repo.requestQuery(memo, QueryActions.INSERT.value); }

    public void update(Memo memo){ repo.requestQuery(memo, QueryActions.UPDATE.value); }

    public void delete(Memo memo){ repo.requestQuery(memo, QueryActions.DELETE.value); }

    public void deleteAll(){ repo.requestQuery(QueryActions.DELETE_ALL.value); }

    public void toggleCompletedState(int id, boolean toCompleted){
        repo.requestQuery(id, toCompleted);
    }

}
