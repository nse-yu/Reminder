package com.example.reminder.database.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_memo")
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int     id;

    @NonNull
    private String  topic;

    private String  summary;

    private int     location = LocationCodes.REMINDER.code;

    private boolean completed = false;

    //constructor
    @Ignore
    public Memo(@NonNull String topic) {
        this.topic = topic;
    }

    @Ignore
    public Memo(@NonNull String topic, @NonNull String summary) {
        this(topic);
        this.summary    = summary;
    }
    @Ignore
    public Memo(@NonNull String topic, @NonNull String summary, boolean completed) {
        this(topic, summary);
        this.completed  = completed;
    }

    @Ignore
    public Memo(@NonNull String topic, @NonNull String summary, boolean completed, LocationCodes location) {
        this(topic, summary, completed);
        this.location   = location.code;
    }

    public Memo(@NonNull String topic, @NonNull String summary, boolean completed, int location) {
        this(topic, summary, completed);
        this.location   = location;
    }

    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary) {
        this(topic, summary);
        this.id         = id;
    }
    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary, boolean completed) {
        this(id, topic, summary);
        this.completed  = completed;
    }

    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary, int location) {
        this(id, topic, summary);
        this.location   = location;
    }

    @Ignore
    public Memo(int id, @NonNull String topic, @NonNull String summary, boolean completed, LocationCodes location) {
        this(topic, summary, completed, location);
        this.id         = id;
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

    public int      getLocation(){
        return location;
    }

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

    public void setLocation(int location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", summary='" + summary + '\'' +
                ", location=" + location +
                ", completed=" + completed +
                '}';
    }
}
