package com.habitdev.sprout.ui.menu.journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.databinding.FragmentJournalBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.interfaces.IRecyclerView;
import com.habitdev.sprout.ui.menu.journal.adapter.NoteItemAdapter;
import com.habitdev.sprout.ui.menu.journal.ui.NoteFragment;

import java.util.List;

public class JournalFragment extends Fragment implements IRecyclerView {

    private FragmentJournalBinding binding;
    private NoteItemAdapter noteItemAdapter;
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
        onSearchNote();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        NoteViewModel noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteList = noteViewModel.getNoteList();

        noteItemAdapter = new NoteItemAdapter(noteList, this);
        binding.journalRecyclerView.setAdapter(noteItemAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            noteItemAdapter.updateNotes(notes);
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
        //Updates Notes based search query
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(requireContext(), "Recycler View on Item Click", Toast.LENGTH_SHORT).show();

        Fragment fragment = new NoteFragment(fragmentManager);
        Bundle bundle = new Bundle();
        Note note = noteList.get(position);
        bundle.putSerializable(BundleKeys.JOURNAL_NOTE.getKEY(), note);
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    private void setEmptyJournalLbl(){
        if(noteList.isEmpty()){
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
        noteItemAdapter = null;
        noteList = null;
        binding = null;
    }
}