package com.habitdev.sprout.database.quotes.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Quotes {
    private String id;
    private String author;
    private String quoted;

    public Quotes() {
        // Default constructor required for calls to DataSnapshot.getValue( quotes.class)
    }

    @Ignore
    public Quotes(String id, String author, String quoted) {
        this.id = id;
        this.author = author;
        this.quoted = quoted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoted() {
        return quoted;
    }

    public void setQuoted(String quoted) {
        this.quoted = quoted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Quotes{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", quoted='" + quoted + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("quoted", quoted);
        return result;
    }
}
