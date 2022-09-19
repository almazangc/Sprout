package com.example.sprout.database.Assessment;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "aSelect")
    private String aSelect;

    @ColumnInfo(name = "bSelect")
    private String bSelect;

    @ColumnInfo(name = "cSelect")
    private String cSelect;

    @ColumnInfo(name = "dSelect")
    private String dSelect;

    @ColumnInfo(name = "selected")
    private String selected;

    public Assessment() {

    }

    public Assessment(String question, String aSelect, String bSelect, String cSelect, String dSelect, String selected) {
        this.question = question;
        this.aSelect = aSelect;
        this.bSelect = bSelect;
        this.cSelect = cSelect;
        this.dSelect = dSelect;
        this.selected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return "Assessment{" + '\n' +
                "uid: " + getUid() + '\n' +
                "question: " + getQuestion() + '\n' +
                "aSelect: " + getASelect() + '\n' +
                "bSelect: " + getBSelect() + '\n' +
                "cSelect: " + getCSelect() + '\n' +
                "dSelect: " + getDSelect() + '\n' +
                "selected: " + getSelected() + '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getASelect() {
        return aSelect;
    }

    public void setASelect(String aSelect) {
        this.aSelect = aSelect;
    }

    public String getBSelect() {
        return bSelect;
    }

    public void setBSelect(String bSelect) {
        this.bSelect = bSelect;
    }

    public String getCSelect() {
        return cSelect;
    }

    public void setCSelect(String cSelect) {
        this.cSelect = cSelect;
    }

    public String getDSelect() {
        return dSelect;
    }

    public void setDSelect(String dSelect) {
        this.dSelect = dSelect;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
