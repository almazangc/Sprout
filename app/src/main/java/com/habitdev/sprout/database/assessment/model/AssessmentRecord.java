package com.habitdev.sprout.database.assessment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AssessmentRecord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_assessment_record_uid", index = true)
    private long pk_assessment_record_uid;

    @ColumnInfo(name = "date_taken")
    private final String date_taken;

    @ColumnInfo(name = "date_completed")
    private String date_completed;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @NonNull
    @Override
    public String toString() {
        return "AssessmentRecord{" +
                "pk_assessment_record_uid=" + pk_assessment_record_uid +
                ", date_taken='" + date_taken + '\'' +
                ", date_completed='" + date_completed + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }

    public AssessmentRecord(String date_taken) {
        this.date_taken = date_taken;
        this.isCompleted = false;
    }

    public long getPk_assessment_record_uid() {
        return pk_assessment_record_uid;
    }

    public void setPk_assessment_record_uid(long pk_assessment_record_uid) {
        this.pk_assessment_record_uid = pk_assessment_record_uid;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public String getDate_completed() {
        return date_completed;
    }

    public void setDate_completed(String date_completed) {
        this.date_completed = date_completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
