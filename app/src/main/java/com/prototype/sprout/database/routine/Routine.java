package com.prototype.sprout.database.routine;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Routine {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "routine")
    private String routine;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "repeated")
    private String repeated;

    @Ignore
    public Routine() {

    }

    public Routine(String routine, String desc, String repeated) {
        this.routine = routine;
        this.desc = desc;
        this.repeated = repeated;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRepeated() {
        return repeated;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }
}
