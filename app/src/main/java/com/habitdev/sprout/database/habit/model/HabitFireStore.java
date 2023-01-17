package com.habitdev.sprout.database.habit.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HabitFireStore {
    private String id;
    private String title;
    private String description;
    private long pk_uid;
    private int upvote;
    private int downvote;

    public HabitFireStore() {
        // Default constructor required for calls to DataSnapshot.getValue(HabitFireStore.class)
    }

    public HabitFireStore(String title, String description, long pk_uid) {
        this.title = title;
        this.description = description;
        this.pk_uid = pk_uid;
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

    public long getPk_uid() {
        return pk_uid;
    }

    public void setPk_uid(long pk_uid) {
        this.pk_uid = pk_uid;
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

    @Override
    public String toString() {
        return "HabitFireStore{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pk_uid=" + pk_uid +
                ", upvote=" + upvote +
                ", downvote=" + downvote +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("pk_uid", pk_uid);
        result.put("upvote", upvote);
        result.put("downvote", downvote);
        return result;
    }
}
