package com.example.reminder.database.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Memo memo);
    @Delete
    void delete(Memo memo);
    @Update
    void update(Memo... memos);

    @Query("SELECT * FROM table_memo ORDER BY id")
    LiveData<List<Memo>> selectAll();
    @Query("SELECT * FROM table_memo LIMIT 1")
    Memo[] selectMemo();
    @Query("DELETE FROM table_memo")
    void deleteAll();
    @Query("SELECT count(*) FROM table_memo WHERE completed = 1")
    int countCompleted();
}
