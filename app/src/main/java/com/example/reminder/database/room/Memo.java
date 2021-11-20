package com.example.reminder.database.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_memo")
public class Memo {
    /**固有のidであり、自動生成される。*/
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**idによって一意に識別されるテキスト*/
    @NonNull
    private String topic;
    @NonNull
    private String summary;
    /**初期状態をfalse（未完了）とする*/
    @NonNull
    private boolean completed = false;

    //constructor
    @Ignore
    public Memo(@NonNull String topic) {
        this.topic = topic;
    }
    public Memo(@NonNull String topic, @NonNull String summary) {
        this.topic = topic;
        this.summary = summary;
    }
    @Ignore
    public Memo(@NonNull String topic, @NonNull String summary,@NonNull boolean completed) {
        this.topic = topic;
        this.summary = summary;
        this.completed = completed;
    }
    @Ignore
    public Memo(int id, @NonNull String topic,@NonNull String summary) {
        this.id = id;
        this.topic = topic;
        this.summary = summary;
    }
    @Ignore
    public Memo(int id, @NonNull String topic,@NonNull String summary,@NonNull boolean completed) {
        this.id = id;
        this.topic = topic;
        this.summary = summary;
        this.completed = completed;
    }

    //getter
    public int getId() {
        return id;
    }
    @NonNull
    public String getTopic() {
        return topic;
    }
    @NonNull
    public String getSummary() {
        return summary;
    }
    @NonNull
    public boolean isCompleted(){return completed;}

    //setter
    public void setId(int id) {
        this.id = id;
    }
    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }
    public void setSummary(@NonNull String summary) {
        this.summary = summary;
    }
    public void setCompleted(@NonNull boolean completed){this.completed = completed;}
}
