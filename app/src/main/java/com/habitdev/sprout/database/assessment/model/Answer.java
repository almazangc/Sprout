package com.habitdev.sprout.database.assessment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = Question.class,
        parentColumns = "pk_questions_uid",
        childColumns = "fk_question_uid",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
))
public class Answer {
    @PrimaryKey(autoGenerate = true)
    private long pk_answer_uid;

    @ColumnInfo(name = "fk_question_uid", index = true)
    private long fk_question_uid;

    @ColumnInfo(name = "selected_answer")
    private String selected_answer;

    @ColumnInfo(name = "user_uid")
    private long user_uid;

    public Answer(long fk_question_uid, String selected_answer, long user_uid) {
        this.fk_question_uid = fk_question_uid;
        this.selected_answer = selected_answer;
        this.user_uid = user_uid;
    }

    @Ignore
    public Answer(long pk_answer_uid, long fk_question_uid, String selected_answer, long user_uid) {
        this.pk_answer_uid = pk_answer_uid;
        this.fk_question_uid = fk_question_uid;
        this.selected_answer = selected_answer;
        this.user_uid = user_uid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Answer{" +
                "pk_answer_uid=" + pk_answer_uid +
                ", fk_question_uid=" + fk_question_uid +
                ", selected_answer='" + selected_answer + '\'' +
                ", user_uid=" + user_uid +
                '}';
    }

    public long getPk_answer_uid() {
        return pk_answer_uid;
    }

    public void setPk_answer_uid(long pk_answer_uid) {
        this.pk_answer_uid = pk_answer_uid;
    }

    public long getFk_question_uid() {
        return fk_question_uid;
    }

    public void setFk_question_uid(long fk_question_uid) {
        this.fk_question_uid = fk_question_uid;
    }

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    public long getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(long user_uid) {
        this.user_uid = user_uid;
    }
}
