package com.habitdev.sprout.database.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.note.model.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> noteListLiveData;
    private final List<Note> noteList;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        noteListLiveData = repository.getNotesListLivedata();
        noteList = repository.getNotesList();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAll();
    }

    public LiveData<List<Note>> getNoteListLiveData() {
        return noteListLiveData;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public long getNoteEntryCount() {
        return repository.getNoteEntryCount();
    }
}
