package com.habitdev.sprout.database.quotes;

public class Quotes {
    String author;
    String quoted;

    public Quotes() {

    }

    public Quotes(String author, String quoted) {
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

    @Override
    public String toString() {
        return "Quotes{" +
                "author='" + author + '\'' +
                ", quoted='" + quoted + '\'' +
                '}';
    }
}
