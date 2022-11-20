package com.habitdev.sprout.database.qoutes;

public class Qoutes {
    String author;
    String quoted;

    public Qoutes() {
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
        return "Qoutes{" +
                "author='" + author + '\'' +
                ", quoted='" + quoted + '\'' +
                '}';
    }
}
