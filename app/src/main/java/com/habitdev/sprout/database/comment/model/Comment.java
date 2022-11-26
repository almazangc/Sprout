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

    @ColumnInfo(name = "fk_subroutine_uid")
    private long fk_subroutine_uid;

    @ColumnInfo(name = "comment_type")
    private String comment_type;

    @ColumnInfo(name = "commnent")
    private String comment;

    @ColumnInfo(name = "date_commented")
    private String date_commented;

    public Comment(long pk_comment_uid, long fk_habit_uid, long fk_subroutine_uid, String comment_type, String comment, String date_commented) {
        this.pk_comment_uid = pk_comment_uid;
        this.fk_habit_uid = fk_habit_uid;
        this.fk_subroutine_uid = fk_subroutine_uid;
        this.comment_type = comment_type;
        this.comment = comment;
        this.date_commented = date_commented;
    }

    @Ignore
    public Comment(long fk_habit_uid, long fk_subroutine_uid, String comment_type, String comment, String date_commented) {
        if (comment_type.toString().equals("Habit")){
            this.fk_habit_uid = fk_habit_uid;
            this.fk_subroutine_uid = 0;
        } else if (comment_type.toString().equals("Subroutine")){
            this.fk_subroutine_uid = fk_subroutine_uid;
            this.fk_habit_uid = 0;
        }
        this.comment_type = comment_type;
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

    public long getFk_subroutine_uid() {
        return fk_subroutine_uid;
    }

    public void setFk_subroutine_uid(long fk_subroutine_uid) {
        this.fk_subroutine_uid = fk_subroutine_uid;
    }

    public String getComment_type() {
        return comment_type;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
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
