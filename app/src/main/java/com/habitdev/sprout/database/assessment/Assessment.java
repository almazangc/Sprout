package com.habitdev.sprout.database.assessment;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;

import java.io.Serializable;
import java.util.List;

public class Assessment implements Serializable {
    @Embedded
    public Question question;

    @Relation(parentColumn = "pk_questions_uid", entityColumn = "pk_choices_uid")
    public List<Choices> choices;

    public Assessment() {

    }

    public Assessment(Question question, List<Choices> choices) {
        this.question = question;
        this.choices = choices;
    }

    @NonNull
    @Override
    public String toString() {
        return "Assessment{" +
                "question=" + question +
                ", choices=" + choices +
                '}';
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }
}
