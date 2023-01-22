package com.habitdev.sprout.database.habit.model.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.habitdev.sprout.enums.AppColor;

import java.util.HashMap;
import java.util.Map;

public class HabitFireStore {
    private String id;
    private String title;
    private String description;
    private String color;
    private long pk_uid;
    private int upvote;
    private int downvote;
    private double rating;

    public HabitFireStore() {
        // Default constructor required for calls to DataSnapshot.getValue(HabitFireStore.class)
    }

    public HabitFireStore(String title, String description, long pk_uid) {
        this.title = title;
        this.description = description;
        this.pk_uid = pk_uid;
        this.color = AppColor.CLOUDS.getColor();
        this.upvote = 0;
        this.downvote = 0;
        this.rating = 0d;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "HabitFireStore{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", pk_uid=" + pk_uid +
                ", upvote=" + upvote +
                ", downvote=" + downvote +
                ", rating=" + rating +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("color", color);
        result.put("pk_uid", pk_uid);
        result.put("upvote", upvote);
        result.put("downvote", downvote);
        result.put("rating", rating);
        return result;
    }
}
