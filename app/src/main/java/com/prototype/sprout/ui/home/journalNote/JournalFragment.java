package com.prototype.sprout.ui.home.journalNote;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.prototype.sprout.database.AppDatabase;
import com.prototype.sprout.database.note.Note;
import com.prototype.sprout.database.note.NoteAdapter;
import com.prototype.sprout.database.note.NoteViewModel;
import com.prototype.sprout.databinding.FragmentJournalBinding;

import java.util.List;

public class JournalFragment extends Fragment {

    private FragmentJournalBinding binding;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private NoteViewModel noteViewModel;

    public JournalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);

        binding.journalNoteRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteList = noteViewModel.getNoteList();

        noteAdapter = new NoteAdapter(noteList);
        binding.journalNoteRecyclerView.setAdapter(noteAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            noteAdapter.setNotes(notes);
        });

//        getNotes();
        return binding.getRoot();
    }

    /**
     * What is this?
     */
    private void getNotes() {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return AppDatabase.getDbInstance(requireContext()).noteDao().getAllNoteList();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (noteList.size() == 0) {
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                } else {
                    noteList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                }
                binding.journalNoteRecyclerView.smoothScrollToPosition(0);
            }
        }
        new GetNotesTask().execute();
    }
}