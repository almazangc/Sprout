package com.habitdev.sprout.database.comment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Comment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_comment")
    private long pk_comment;

    @ColumnInfo(name = "commnent")
    private String comment;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "date_commented")
    private String date_commented;

    public Comment(String comment, String author, String date_commented) {
        this.comment = comment;
        this.author = author;
        this.date_commented = date_commented;
    }

    public Comment(long pk_comment, String comment, String author, String date_commented) {
        this.pk_comment = pk_comment;
        this.comment = comment;
        this.author = author;
        this.date_commented = date_commented;
    }

    public long getPk_comment() {
        return pk_comment;
    }

    public void setPk_comment(long pk_comment) {
        this.pk_comment = pk_comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate_commented() {
        return date_commented;
    }

    public void setDate_commented(String date_commented) {
        this.date_commented = date_commented;
    }
}
