package com.habitdev.sprout.database.assessment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Question implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_questions_uid", index = true)
    private long pk_question_uid;

    @ColumnInfo(name = "question")
    private String question;

    /**
     * <p>1 - positive question</p>
     * <p>-1 - negative question</p>
     * <p>0 - undefined | neutral</p>
     *
     * <p>This values are for determining what type of the question was set. </p>
     * <p>Positive and Negative classification will be used for calculating the actual score for determining which habit to recommend</p>
     * <p>Meanwhile, a neutral or undefined classification does not affect the result of score. </p>
     */
    @ColumnInfo(name = "classification")
    private int classification;

    /**
     * Habit related to the question that will be recommended if a certain score is met.
     */
    @ColumnInfo(name = "fk_habit_uid")
    private long fk_habit_uid;

    public Question() {

    }

    @Ignore
    public Question(String question) {
        this.question = question;
        this.classification = 0; //0 represent undefined
        this.fk_habit_uid = -1; // no habit
    }

    @Ignore
    public Question(String question, int classification, long fk_habit_uid) {
        this.question = question;
        this.classification = classification;
        this.fk_habit_uid = fk_habit_uid;
    }

    @Ignore
    public Question(long pk_question_uid, String question, int classification, long fk_habit_uid) {
        this.pk_question_uid = pk_question_uid;
        this.question = question;
        this.classification = classification;
        this.fk_habit_uid = fk_habit_uid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "pk_question_uid=" + pk_question_uid +
                ", question='" + question + '\'' +
                ", classification=" + classification +
                ", fk_habit_uid=" + fk_habit_uid +
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

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public long getFk_habit_uid() {
        return fk_habit_uid;
    }

    public void setFk_habit_uid(long fk_habit_uid) {
        this.fk_habit_uid = fk_habit_uid;
    }
}
