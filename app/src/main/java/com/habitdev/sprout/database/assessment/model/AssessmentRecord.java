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

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @NonNull
    @Override
    public String toString() {
        return "AssessmentRecord{" +
                "pk_assessment_record_uid=" + pk_assessment_record_uid +
                ", date='" + date + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }

    public AssessmentRecord(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public long getPk_assessment_record_uid() {
        return pk_assessment_record_uid;
    }

    public void setPk_assessment_record_uid(long pk_assessment_record_uid) {
        this.pk_assessment_record_uid = pk_assessment_record_uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
