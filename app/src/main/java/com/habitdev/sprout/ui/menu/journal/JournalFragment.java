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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.databinding.FragmentJournalBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.journal.adapter.JournalNoteItemAdapter;
import com.habitdev.sprout.ui.menu.journal.ui.AddNoteFragment;

import java.util.List;

public class JournalFragment extends Fragment implements NoteItemOnClickListener {

    private FragmentJournalBinding binding;
    private JournalNoteItemAdapter journalNoteItemAdapter;
    private List<Note> noteList;

    public JournalFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        setRecyclerViewAdapter();
        fabOnClick();
        onSearchNote();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        NoteViewModel noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteList = noteViewModel.getNoteList();

        journalNoteItemAdapter = new JournalNoteItemAdapter(noteList);
        journalNoteItemAdapter.setNoteItemOnClickListener(this);
        binding.journalRecyclerView.setAdapter(journalNoteItemAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            journalNoteItemAdapter.setNewNoteList(notes);
            noteList = notes;
            setEmptyJournalView();
        });
        onSwipeRefresh();
    }

    private void fabOnClick() {
        binding.fabJournal.setOnClickListener(v -> changeFragment(new AddNoteFragment()));
    }

    private void onSwipeRefresh() {
        binding.journalSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(requireContext(), "Journal Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
                binding.journalSwipeRefresh.setRefreshing(false);
            }
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
        Fragment fragment = new AddNoteFragment();
        Bundle bundle = new Bundle();
        Note note = noteList.get(position);
        bundle.putSerializable(BundleKeys.JOURNAL_NOTE.getKEY(), note);
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    private void setEmptyJournalView() {
        if (noteList.isEmpty()) {
            binding.journalEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.journalEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
        }
    }


    private void changeFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.journalFrameLayout.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.journalContainer.setVisibility(View.GONE);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
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