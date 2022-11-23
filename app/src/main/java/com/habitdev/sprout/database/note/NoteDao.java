package com.habitdev.sprout.database.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY uid DESC")
    LiveData<List<Note>> getAllNoteLiveData();

    @Query("SELECT * FROM Note ORDER BY uid DESC")
    List<Note> getAllNoteList();

    @Insert
    void insert(Note... notes);

    @Update
    void update(Note... note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM Note")
    void deleteAllNotes();
}
