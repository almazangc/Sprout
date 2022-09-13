package com.example.sprout.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Assestment {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "aselect")
    public String aselect;

    @ColumnInfo(name = "bselect")
    public String bselect;

    @ColumnInfo(name = "cselect")
    public String cselect;

    @ColumnInfo(name = "dselect")
    public String dselect;

    @ColumnInfo(name = "selected")
    public String selected;

    @NonNull
    @Override
    public String toString() {
        return "Assessment{" + '\n'+
                "uid: " + uid  + '\n'+
                "question: " + question + '\n' +
                "aselect: " + aselect + '\n' +
                "bselect: " + bselect + '\n' +
                "cselect: " + cselect + '\n' +
                "dselect: " + dselect + '\n' +
                "selected: " + selected + '}';
    }

    public int getUid() {
        return uid;
    }

    public String getQuestion() {
        return question;
    }

    public String getAselect() {
        return aselect;
    }

    public String getBselect() {
        return bselect;
    }

    public String getCselect() {
        return cselect;
    }

    public String getDselect() {
        return dselect;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
