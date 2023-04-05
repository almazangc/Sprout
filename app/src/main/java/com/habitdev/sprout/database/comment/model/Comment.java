package com.habitdev.sprout.database.comment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Comment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_comment_uid")
    private long pk_comment_uid;

    @ColumnInfo(name = "fk_habit_uid")
    private long fk_habit_uid;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "date_commented")
    private String date_commented;

    public Comment(long pk_comment_uid, long fk_habit_uid, String comment, String date_commented) {
        this.pk_comment_uid = pk_comment_uid;
        this.fk_habit_uid = fk_habit_uid;
        this.comment = comment;
        this.date_commented = date_commented;
    }

    @Ignore
    public Comment(long fk_habit_uid, String comment, String date_commented) {
        this.fk_habit_uid = fk_habit_uid;
        this.comment = comment;
        this.date_commented = date_commented;
    }

    public long getPk_comment_uid() {
        return pk_comment_uid;
    }

    public void setPk_comment_uid(long pk_comment_uid) {
        this.pk_comment_uid = pk_comment_uid;
    }

    public long getFk_habit_uid() {
        return fk_habit_uid;
    }

    public void setFk_habit_uid(long fk_habit_uid) {
        this.fk_habit_uid = fk_habit_uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_commented() {
        return date_commented;
    }

    public void setDate_commented(String date_commented) {
        this.date_commented = date_commented;
    }
}
