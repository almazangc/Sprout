package com.prototype.sprout.database.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> noteListLiveData;
    private List<Note> noteList;

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
}
