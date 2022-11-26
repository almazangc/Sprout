package com.habitdev.sprout.database.assessment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(
        entity = Question.class,
        parentColumns = "pk_questions_uid",
        childColumns = "fk_question_uid",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
))
public class Choices implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_choices_uid")
    private long pk_choices_uid;

    @ColumnInfo(name = "fk_question_uid", index = true)
    private long fk_question_uid;

    @ColumnInfo(name = "choices")
    private String choices;

    public Choices(String choices) {
        this.choices = choices;
    }

    @Ignore
    public Choices(long pk_choices_uid, long fk_question_uid, String choices) {
        this.pk_choices_uid = pk_choices_uid;
        this.fk_question_uid = fk_question_uid;
        this.choices = choices;
    }

    @NonNull
    @Override
    public String toString() {
        return "Choices{" +
                "pk_choices_uid=" + pk_choices_uid +
                ", fk_question_uid=" + fk_question_uid +
                ", choices='" + choices + '\'' +
                '}';
    }

    public long getPk_choices_uid() {
        return pk_choices_uid;
    }

    public void setPk_choices_uid(long pk_choices_uid) {
        this.pk_choices_uid = pk_choices_uid;
    }

    public long getFk_question_uid() {
        return fk_question_uid;
    }

    public void setFk_question_uid(long fk_question_uid) {
        this.fk_question_uid = fk_question_uid;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }
}
