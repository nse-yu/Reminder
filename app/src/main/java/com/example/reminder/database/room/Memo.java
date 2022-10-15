package com.example.reminder.database.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "table_memo")
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int     id;

    @NonNull
    private String  topic;

    private String  summary;

    private boolean completed = false;

    //constructor
    @Ignore
    public Memo(@NonNull String topic) {
        this.topic = topic;
    }
    public Memo(@NonNull String topic, @NonNull String summary) {
        this.topic      = topic;
        this.summary    = summary;
    }
    @Ignore
    public Memo(@NonNull String topic, @NonNull String summary, boolean completed) {
        this.topic      = topic;
        this.summary    = summary;
        this.completed  = completed;
    }
    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary) {
        this.id         = id;
        this.topic      = topic;
        this.summary    = summary;
    }
    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary, boolean completed) {
        this.id         = id;
        this.topic      = topic;
        this.summary    = summary;
        this.completed  = completed;
    }

    //getter
    public int      getId() {
        return id;
    }
    @NonNull
    public String   getTopic() {
        return topic;
    }
    @NonNull
    public String   getSummary() {
        return summary;
    }
    public boolean  isCompleted(){ return completed; }

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
    public void setCompleted(boolean completed){ this.completed = completed; }

    @NotNull
    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", summary='" + summary + '\'' +
                ", completed=" + completed +
                '}';
    }
}
