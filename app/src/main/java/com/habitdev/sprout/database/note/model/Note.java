package com.habitdev.sprout.database.note.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity()
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_note_uid")
    private long pk_note_uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date_time")
    private String dateTime;

    @ColumnInfo(name = "subtitle")
    private String subtitle;

    @ColumnInfo(name = "note_content")
    private String noteContent;

    @ColumnInfo(name = "color")
    private String color;

    public Note() {}

    @Ignore
    public Note(long pk_note_uid, String title, String dateTime, String subtitle, String noteContent, String color) {
        this.pk_note_uid = pk_note_uid;
        this.title = title;
        this.dateTime = dateTime;
        this.subtitle = subtitle;
        this.noteContent = noteContent;
        this.color = color;
    }

    @Ignore
    public Note(long pk_note_uid, String title, String dateTime, String noteContent, String color) {
        this.pk_note_uid = pk_note_uid;
        this.title = title;
        this.dateTime = dateTime;
        this.subtitle = "";
        this.noteContent = noteContent;
        this.color = color;
    }

    @Ignore
    public Note(String title, String dateTime, String subtitle, String noteContent, String color) {
        this.title = title;
        this.dateTime = dateTime;
        this.subtitle = subtitle;
        this.noteContent = noteContent;
        this.color = color;
    }

    @Ignore
    public Note(String title, String dateTime, String noteContent, String color) {
        this.title = title;
        this.dateTime = dateTime;
        this.subtitle = "";
        this.noteContent = noteContent;
        this.color = color;
    }

    public long getPk_note_uid() {
        return pk_note_uid;
    }

    public void setPk_note_uid(int pk_note_uid) {
        this.pk_note_uid = pk_note_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "pk_note_uid=" + pk_note_uid +
                ", title='" + title + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
