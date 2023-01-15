package com.habitdev.sprout.database.quotes.model;

import androidx.annotation.NonNull;

public class Quotes {
    private String id;
    private String author;
    private String quoted;

    public Quotes() {
        // Default constructor required for calls to DataSnapshot.getValue( quotes.class)
    }

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
}
