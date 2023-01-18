package com.habitdev.sprout.database.habit.model.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SubroutineFireStore {
    private String id;
    private String title;
    private String description;
    private String color;
    private long pk_uid;
    private long fk_habit_uid;
    private int upvote;
    private int downvote;

    public SubroutineFireStore() {
        // Default constructor required for calls to DataSnapshot.getValue(HabitFireStore.class)
    }

    public SubroutineFireStore(String id, String title,String description, String color, long pk_uid, long fk_habit_uid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.pk_uid = pk_uid;
        this.fk_habit_uid = fk_habit_uid;
        this.upvote = 0;
        this.downvote = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getPk_uid() {
        return pk_uid;
    }

    public void setPk_uid(long pk_uid) {
        this.pk_uid = pk_uid;
    }

    public long getFk_habit_uid() {
        return fk_habit_uid;
    }

    public void setFk_habit_uid(long fk_habit_uid) {
        this.fk_habit_uid = fk_habit_uid;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    @NonNull
    @Override
    public String toString() {
        return "SubroutineFireStore{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", pk_uid=" + pk_uid +
                ", fk_habit_uid=" + fk_habit_uid +
                ", upvote=" + upvote +
                ", downvote=" + downvote +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("color", color);
        result.put("pk_uid", pk_uid);
        result.put("fk_habit_uid", fk_habit_uid);
        result.put("upvote", upvote);
        result.put("downvote", downvote);
        return result;
    }
}
