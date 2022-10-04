package com.prototype.sprout.database.journal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Journal {
    @PrimaryKey(autoGenerate = true)
    private int uid;

}
