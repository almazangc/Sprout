package com.habitdev.sprout.ui.menu.journal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.Note;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.databinding.FragmentJournalBinding;
import com.habitdev.sprout.ui.menu.journal.adapter.NoteItemAdapter;
import com.habitdev.sprout.ui.menu.journal.ui.AddNoteFragment;

import java.util.List;

public class JournalFragment extends Fragment {

    private FragmentJournalBinding binding;
    private NoteItemAdapter noteItemAdapter;
    private NoteViewModel noteViewModel;
    private List<Note> noteList;
    private FragmentManager fragmentManager;

    public JournalFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        fragmentManager = getChildFragmentManager();
        setRecyclerViewAdapter();
        fabOnClick();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter(){
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteList = noteViewModel.getNoteList();
        noteItemAdapter = new NoteItemAdapter(noteList);
        binding.journalRecyclerView.setAdapter(noteItemAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            noteItemAdapter.setNotes(notes);
        });
    }

    private void fabOnClick(){
        binding.fabJournal.setOnClickListener(view -> {
            fragmentManager
                    .beginTransaction()
                    .replace(binding.journalFrameLayout.getId(), new AddNoteFragment(fragmentManager))
                    .commit();
//            Log.d("tag", "fabOnClick() called: backstack counts " + fragmentManager.getBackStackEntryCount());
        binding.journalContainer.setVisibility(View.GONE);
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Something
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    /**
     * What is this?
     */
//    private void getNotes() {
//        @SuppressLint("StaticFieldLeak")
//        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
//
//            @Override
//            protected List<Note> doInBackground(Void... voids) {
//                return AppDatabase.getDbInstance(requireContext()).noteDao().getAllNoteList();
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            protected void onPostExecute(List<Note> notes) {
//                super.onPostExecute(notes);
//                if (noteList.size() == 0) {
//                    noteList.addAll(notes);
//                    noteAdapter.notifyDataSetChanged();
//                } else {
//                    noteList.add(0, notes.get(0));
//                    noteAdapter.notifyItemInserted(0);
//                }
//                binding.journalRecyclerView.smoothScrollToPosition(0);
//            }
//        }
//        new GetNotesTask().execute();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("tag", "Journal Fragment: onDestroyView() called");
        noteItemAdapter = null;
        noteList = null;
        binding = null;
    }
}