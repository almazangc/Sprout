package com.habitdev.sprout.ui.menu.journal;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.databinding.FragmentJournalBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.journal.adapter.JournalNoteItemAdapter;
import com.habitdev.sprout.ui.menu.journal.ui.AddNoteFragment;
import com.habitdev.sprout.ui.dialog.CompletedAchievementDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalFragment extends Fragment implements JournalNoteItemAdapter.NoteItemOnClickListener {

    private FragmentJournalBinding binding;
    NoteViewModel noteViewModel;
    private JournalNoteItemAdapter journalNoteItemAdapter;
    private List<Note> noteList;

    public JournalFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        setRecyclerViewAdapter();
        fabOnClick();
        onSearchNote();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        noteList = noteViewModel.getNoteList();
        journalNoteItemAdapter = new JournalNoteItemAdapter(noteList);
        journalNoteItemAdapter.setNoteItemOnClickListener(this);
        binding.journalRecyclerView.setAdapter(journalNoteItemAdapter);

        noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            journalNoteItemAdapter.setNewNoteList(new ArrayList<>(notes));
            noteList = new ArrayList<>(notes);
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
                noteViewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
                    journalNoteItemAdapter.setNewNoteList(new ArrayList<>(notes));
                    noteList = new ArrayList<>(notes);
                    setEmptyJournalView();
                });
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
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};
        final boolean[] isAchievementDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(JournalFragment.this.getId()), 2);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(new OnBackPressDialogFragment.onCancelDialog() {
                                    @Override
                                    public void cancelDialog() {
                                        keypress_count[0] = 0;
                                        isOnBackPressDialogShowing[0] = false;
                                    }
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }

                            if (!isAchievementDialogShowing[0]) {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement CLOSEAPPPROMPT = achievementViewModel.getAchievementByUID(26);

                                if (!CLOSEAPPPROMPT.is_completed()) {
                                    CLOSEAPPPROMPT.setIs_completed(true);
                                    CLOSEAPPPROMPT.setCurrent_progress(CLOSEAPPPROMPT.getCurrent_progress()+1);
                                    CLOSEAPPPROMPT.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    CLOSEAPPPROMPT.setTitle("Close Application");
                                    CLOSEAPPPROMPT.setDescription("Unlocked by pressing back button twice");
                                    achievementViewModel.updateAchievement(CLOSEAPPPROMPT);
                                    CompletedAchievementDialogFragment completedAchievementDialogFragment = new CompletedAchievementDialogFragment(CLOSEAPPPROMPT.getTitle());
                                    completedAchievementDialogFragment.setTargetFragment(getChildFragmentManager()
                                            .findFragmentById(JournalFragment.this.getId()), 1);
                                    completedAchievementDialogFragment.show(getChildFragmentManager(), "CompletedAchievementDialog");
                                    isAchievementDialogShowing[0] = true;
                                }
                            }
                        } else {
                            requireActivity().moveTaskToBack(true);
                            keypress_count[0] = 0;
                        }
                        this.cancel();
                    }
                }.start();
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