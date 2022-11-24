package com.habitdev.sprout.database.assessment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Question {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_questions_uid")
    private long pk_question_uid;

    @ColumnInfo(name = "question")
    private String question;

    public Question(String question) {
        this.question = question;
    }

    @Ignore
    public Question(long pk_question_uid, String question) {
        this.pk_question_uid = pk_question_uid;
        this.question = question;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "pk_question_uid=" + pk_question_uid +
                ", question='" + question + '\'' +
                '}';
    }

    public long getPk_question_uid() {
        return pk_question_uid;
    }

    public void setPk_question_uid(long pk_question_uid) {
        this.pk_question_uid = pk_question_uid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
