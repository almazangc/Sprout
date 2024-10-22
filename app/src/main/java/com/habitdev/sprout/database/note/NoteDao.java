package com.habitdev.sprout.database.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.habitdev.sprout.database.note.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY pk_note_uid DESC")
    LiveData<List<Note>> getAllNoteLiveData();

    @Query("SELECT * FROM Note ORDER BY pk_note_uid DESC")
    List<Note> getAllNoteList();

    @Query("SELECT COUNT(*) FROM Note")
    long getNoteEntryCount();

    @Insert
    void insert(Note... notes);

    @Update
    void update(Note... note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM Note")
    void deleteAllNotes();
}
