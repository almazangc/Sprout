package com.habitdev.sprout.ui.menu.journal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.databinding.FragmentJournalBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemOnclickListener;
import com.habitdev.sprout.ui.menu.journal.adapter.JournalNoteItemAdapter;
import com.habitdev.sprout.ui.menu.journal.ui.NoteFragment;

import java.util.List;

public class JournalFragment extends Fragment implements HomeParentItemOnclickListener {

    private FragmentJournalBinding binding;
    private JournalNoteItemAdapter journalNoteItemAdapter;
    private List<Note> noteList;
    private FragmentManager fragmentManager;

    public JournalFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        fragmentManager = getChildFragmentManager();
        setRecyclerViewAdapter();
        fabOnClick();
        onSearchNote();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        NoteViewModel noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteList = noteViewModel.getNoteList();

        journalNoteItemAdapter = new JournalNoteItemAdapter(noteList, this);
        binding.journalRecyclerView.setAdapter(journalNoteItemAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            journalNoteItemAdapter.updateNotes(notes);
            noteList = notes;
            setEmptyJournalLbl();
        });
        onSwipeRefresh();
    }

    private void fabOnClick() {
        binding.fabJournal.setOnClickListener(v -> changeFragment(new NoteFragment(fragmentManager)));
    }

    private void onSwipeRefresh() {
        binding.journalSwipeRefresh.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Journal Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.journalSwipeRefresh.setRefreshing(false);
        });
    }

    private void onSearchNote() {
        binding.searchBarInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                journalNoteItemAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!noteList.isEmpty()) {
                    journalNoteItemAdapter.searchNote(editable.toString());
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Fragment fragment = new NoteFragment(fragmentManager);
        Bundle bundle = new Bundle();
        Note note = noteList.get(position);
        bundle.putSerializable(BundleKeys.JOURNAL_NOTE.getKEY(), note);
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    private void setEmptyJournalLbl() {
        if (noteList.isEmpty()) {
            binding.journalEmptyLbl.setVisibility(View.VISIBLE);
        } else {
            binding.journalEmptyLbl.setVisibility(View.GONE);
        }
    }


    private void changeFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(binding.journalFrameLayout.getId(), fragment)
                .commit();
        binding.journalContainer.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        journalNoteItemAdapter = null;
        noteList = null;
        binding = null;
    }
}