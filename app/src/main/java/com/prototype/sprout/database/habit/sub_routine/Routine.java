package com.prototype.sprout.database.habit.sub_routine;

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

    @ColumnInfo(name = "streak")
    private String streak;

    @ColumnInfo(name = "total")
    private String total;

    @ColumnInfo(name = "skips")
    private String skips;

    @ColumnInfo(name = "alarm")
    private int alarm;

    @Ignore
    public Routine() {

    }


}
