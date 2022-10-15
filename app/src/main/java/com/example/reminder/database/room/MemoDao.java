package com.example.reminder.database.room;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Memo memo);
    @Delete
    void delete(Memo memo);
    @Update
    void update(Memo... memos);


    @Query("UPDATE table_memo SET completed = :state WHERE id = :id")
    void toggleCompletedState(int id, int state);

    @Query("SELECT * FROM table_memo ORDER BY id")
    LiveData<List<Memo>> selectAll();

    @Query("DELETE FROM table_memo")
    void deleteAll();

    @Query("SELECT count(*) FROM table_memo WHERE completed = 1")
    LiveData<List<Integer>> countCompleted();
}
