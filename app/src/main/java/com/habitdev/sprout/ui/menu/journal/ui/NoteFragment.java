package com.habitdev.sprout.ui.menu.journal.ui;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.Note;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.databinding.FragmentNoteBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.enums.NoteColor;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private NoteViewModel noteViewModel;
    private final FragmentManager fragmentManager;
    private final int ic_check;
    private int current_selected_color;
    private int old_selected_color;
    private String color;
    private Note note;
    private Bundle bundle;

    public NoteFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.ic_check = R.drawable.ic_check;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
        this.color = NoteColor.CLOUDS.getColor();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        showDeleteNote();
        setNotes();
        colorSelect();
        onSaveNote();
        onDeleteNote();
        onBackPress();
        return binding.getRoot();
    }

    private void showDeleteNote(){
        if (bundle != null){
            binding.fabDeleteNote.setVisibility(View.VISIBLE);
        } else {
            binding.fabDeleteNote.setVisibility(View.GONE);
        }
    }

    private void setNotes() {
        if (bundle != null) {

            note = (Note) bundle.getSerializable(BundleKeys.JOURNAL_NOTE.getKEY());

            binding.noteTitle.setText(note.getTitle());
            binding.noteSubTitle.setText(note.getSubtitle());
            binding.noteCurrentTime.setText(note.getDateTime());
            binding.noteContent.setText(note.getNoteContent());

            if (note.getColor().equals(NoteColor.ALZARIN.getColor())) {
                current_selected_color = 1;
                setSelected_color();
            } else if (note.getColor().equals(NoteColor.AMETHYST.getColor())) {
                current_selected_color = 2;
                setSelected_color();
            } else if (note.getColor().equals(NoteColor.BRIGHT_SKY_BLUE.getColor())) {
                current_selected_color = 3;
                setSelected_color();
            } else if (note.getColor().equals(NoteColor.NEPHRITIS.getColor())) {
                current_selected_color = 4;
                setSelected_color();
            } else if (note.getColor().equals(NoteColor.SUNFLOWER.getColor())) {
                current_selected_color = 5;
                setSelected_color();
            } else {
                old_selected_color = 1;
                setSelected_color();
            }
            return;
        }
        setCurrentDate();
        binding.cloudSelected.setImageResource(ic_check);
    }

    private void setCurrentDate() {
        binding.noteCurrentTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault())
                        .format(new Date())
        );
    }

    private void colorSelect() {
        binding.noteColorSelector.setOnClickListener(v -> {
            if (binding.noteLayoutMiscellaneous.getVisibility() == View.GONE) {
                binding.noteLayoutMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.noteLayoutMiscellaneous.setVisibility(View.GONE);
            }
        });

        binding.cloudMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(0);
            setSelected_color();
            Toast.makeText(requireActivity(), "Cloud", Toast.LENGTH_SHORT).show();
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
            Toast.makeText(requireActivity(), "Alzarin", Toast.LENGTH_SHORT).show();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
            Toast.makeText(requireActivity(), "Amethyst", Toast.LENGTH_SHORT).show();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
            Toast.makeText(requireActivity(), "BrightSky Blue", Toast.LENGTH_SHORT).show();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
            Toast.makeText(requireActivity(), "Nephritis", Toast.LENGTH_SHORT).show();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
            Toast.makeText(requireActivity(), "Sunflower", Toast.LENGTH_SHORT).show();
        });
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_alzarin));
                    color = NoteColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_amethyst));
                    color = NoteColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_brightsky_blue));
                    color = NoteColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_nephritis));
                    color = NoteColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_sunflower));
                    color = NoteColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_clouds));
                    color = NoteColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
    }

    private void setBackgroundNoteIndicator(Drawable backgroundNoteIndicator) {
        binding.noteColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void onSaveNote() {
        binding.fabSaveNote.setOnClickListener(v -> {
            if (validateNote()) {
                if (bundle != null) {
                    if (showAlertDialog()) {
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setMessage("Save Changes")
                                .setCancelable(false)
                                .setPositiveButton("YES", (dialogInterface, i) -> {
                                    noteViewModel.update(
                                            binding.noteSubTitle.getText().toString().trim().isEmpty() ?
                                                    new Note(
                                                            note.getUid(),
                                                            binding.noteTitle.getText().toString(),
                                                            note.getDateTime(),
                                                            binding.noteContent.getText().toString(),
                                                            color) :
                                                    new Note(
                                                            note.getUid(),
                                                            binding.noteTitle.getText().toString(),
                                                            note.getDateTime(),
                                                            binding.noteSubTitle.getText().toString(),
                                                            binding.noteContent.getText().toString(),
                                                            color)
                                    );
                                    gotoJournalFragment();
                                })
                                .setNegativeButton("No",(dialogInterface, i) -> {
//                                    gotoJournalFragment();
                                })
                                .show();
                    } else {
                        gotoJournalFragment();
                    }
                } else {
                    noteViewModel.insert(
                            binding.noteSubTitle.getText().toString().trim().isEmpty() ?
                                    new Note(
                                            binding.noteTitle.getText().toString(),
                                            binding.noteCurrentTime.getText().toString(),
                                            binding.noteContent.getText().toString(),
                                            color) :
                                    new Note(
                                            binding.noteTitle.getText().toString(),
                                            binding.noteCurrentTime.getText().toString(),
                                            binding.noteSubTitle.getText().toString(),
                                            binding.noteContent.getText().toString(),
                                            color)
                    );
                    gotoJournalFragment();
                }
            }
        });
    }

    private void onDeleteNote(){
        if (binding.fabDeleteNote.getVisibility() == View.VISIBLE){
            binding.fabDeleteNote.setOnClickListener(v -> {
                noteViewModel.delete(note);
                gotoJournalFragment();
            });
        }
    }

    private boolean showAlertDialog() {
        return (!binding.noteTitle.getText().toString().equals(note.getTitle()) ||
                !binding.noteSubTitle.getText().toString().equals(note.getSubtitle()) ||
                !color.equals(note.getColor()) ||
                !binding.noteContent.getText().toString().equals(note.getNoteContent())
        );
    }

    private boolean validateNote() {
        if (binding.noteTitle.getText().toString().trim().isEmpty() && binding.noteContent.getText().toString().trim().isEmpty()) {
            binding.noteHint.setText((CharSequence) "Note title and content is required*");
            return false;
        } else if (binding.noteTitle.getText().toString().trim().isEmpty()) {
            binding.noteHint.setText((CharSequence) "Note title is required*");
            return false;
        } else if (binding.noteContent.getText().toString().trim().isEmpty()) {
            binding.noteHint.setText((CharSequence) "Note content is required*");
            return false;
        }
        binding.noteHint.setText("");
        return true;
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                gotoJournalFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void gotoJournalFragment() {
//        Fragment fragment = fragmentManager.findFragmentById(R.id.journalFragment);
        fragmentManager.beginTransaction()
                .replace(binding.addNewNoteFrameLayout.getId(), new JournalFragment())
                .commit();
        binding.addNewNoteContainer.setVisibility(View.GONE);
        Log.d("tag", "gotoJournalFragment: " + fragmentManager.getFragments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}