package com.prototype.sprout.database.note;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prototype.sprout.database.AppDatabase;
import com.prototype.sprout.database.assessment.Assessment;
import com.prototype.sprout.database.assessment.AssessmentDao;
import com.prototype.sprout.database.assessment.AssessmentRepository;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> notesListLivedata;
    private List<Note> notesList;

    public NoteRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        noteDao = appDatabase.noteDao();
        notesListLivedata = noteDao.getAllNoteLiveData();
        notesList = noteDao.getAllNoteList();
    }

    public void insert(Note note) {
        new NoteRepository.InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new NoteRepository.UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new NoteRepository.DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAll() {
        new NoteRepository.DeleteAllNotesAsyncTask(noteDao).execute();
    }


    public static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        public DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public LiveData<List<Note>> getNotesListLivedata() {
        return notesListLivedata;
    }

    public List<Note> getNotesList() {
        return notesList;
    }
}