package com.example.reminder.database.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class},version = 5,exportSchema = false)
public abstract class MemoDatabase extends RoomDatabase {

    private static volatile MemoDatabase INSTANCE;

    public abstract MemoDao getMemoDao();

    public static MemoDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (MemoDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemoDatabase.class,"memo_database")
                            //schemaのバージョン移行に不具合が合った場合には、schemaの退行を認める
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
