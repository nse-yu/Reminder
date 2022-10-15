package com.example.reminder.background;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.reminder.database.room.Memo;
import com.example.reminder.database.room.MemoDao;
import com.example.reminder.database.room.MemoDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueryRepository {
    private final MemoDao               dao;
    private final LiveData<List<Memo>>  memos;
    private final LiveData<List<Integer>> countCompleted;
    private final ExecutorService service = Executors.newFixedThreadPool(1);

    public QueryRepository(Application application) {

        MemoDatabase database = MemoDatabase.getDatabase(application);

        dao = database.getMemoDao();

        //daoが提供するQueryメソッドの戻り値で初期化することで、LiveDataのsetValueを呼び出すことなく、内容が変更される
        //QueryViewModelがQueryRepository.selectAll()を使用しているため、返されるmemosインスタンスは同じであり、
        //ViewModelも変更が適用されるため、結果としてMainActivityではObserverに自動的に変更通知が行く。
        memos           = dao.selectAll();

        countCompleted  = dao.countCompleted();
    }

    public void requestQuery(Memo memo, Integer requestCode){

        //asynchronous tasks to execute a query corresponding to the requestCode.
        service.execute(() -> {
            if(requestCode == QueryViewModel.QueryActions.INSERT.value)
                insert(memo);
            else if(requestCode == QueryViewModel.QueryActions.UPDATE.value)
                update(memo);
            else if(requestCode == QueryViewModel.QueryActions.DELETE.value)
                delete(memo);
        });
    }

    public void requestQuery(int id, boolean toCompleted){

        service.execute(() -> toggleCompletedState(id, toCompleted ? 1 : 0));

    }

    public void requestQuery(Integer request){

        service.execute(() -> {
            if(request == QueryViewModel.QueryActions.DELETE_ALL.value)
                deleteAll();
        });

    }

    public LiveData<List<Memo>> selectAll() {
        return memos;
    }

    public LiveData<List<Integer>> countCompleted(){
        return countCompleted;
    }

    private void insert(Memo memo){
        dao.insert(memo);
    }

    private void delete(Memo memo){
        dao.delete(memo);
    }

    private void update(Memo memo){
        dao.update(memo);
    }

    private void deleteAll(){
        dao.deleteAll();
    }

    private void toggleCompletedState(int id, int state){
        dao.toggleCompletedState(id, state);
    }

}
