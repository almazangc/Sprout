package com.habitdev.sprout.ui.menu.journal.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.databinding.FragmentAddNoteBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding binding;
    private NoteViewModel noteViewModel;
    private final int ic_check =  R.drawable.ic_check;
    private int current_selected_color;
    private int old_selected_color;
    private String color =  AppColor.CLOUDS.getColor();;
    private Note note;
    private Bundle bundle;

    public AddNoteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        setFabDisplay();
        setNotes();
        colorSelect();
        validateNote();
        onSaveNote();
        onDeleteNote();
        onBackPress();
        return binding.getRoot();
    }

    private void setFabDisplay() {
        if (bundle != null) {
            binding.fabDeleteNote.setVisibility(View.VISIBLE);
            binding.fabSaveNote.setImageResource(R.drawable.ic_save);
        } else {
            binding.fabDeleteNote.setVisibility(View.GONE);
            binding.fabSaveNote.setImageResource(R.drawable.ic_check);
        }
    }

    private void setNotes() {
        if (bundle != null) {

            note = (Note) bundle.getSerializable(BundleKeys.JOURNAL_NOTE.getKEY());

            binding.noteHint.setText("");
            binding.noteTitle.setText(note.getTitle());
            binding.noteSubTitle.setText(note.getSubtitle());
            binding.noteCurrentTime.setText(note.getDateTime());
            binding.noteContent.setText(note.getNoteContent());

            if (note.getColor().equals(AppColor.ALZARIN.getColor())) {
                current_selected_color = 1;
                setSelected_color();
            } else if (note.getColor().equals(AppColor.AMETHYST.getColor())) {
                current_selected_color = 2;
                setSelected_color();
            } else if (note.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                current_selected_color = 3;
                setSelected_color();
            } else if (note.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                current_selected_color = 4;
                setSelected_color();
            } else if (note.getColor().equals(AppColor.SUNFLOWER.getColor())) {
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
                new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
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
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
        });
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
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
            if (bundle != null) {
                if (showAlertDialog()) {
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setMessage("Save Changes")
                            .setCancelable(false)
                            .setPositiveButton("YES", (dialogInterface, i) -> {
                                noteViewModel.update(
                                        binding.noteSubTitle.getText().toString().trim().isEmpty() ?
                                                new Note(
                                                        note.getPk_note_uid(),
                                                        binding.noteTitle.getText().toString(),
                                                        note.getDateTime(),
                                                        binding.noteContent.getText().toString(),
                                                        color) :
                                                new Note(
                                                        note.getPk_note_uid(),
                                                        binding.noteTitle.getText().toString(),
                                                        note.getDateTime(),
                                                        binding.noteSubTitle.getText().toString(),
                                                        binding.noteContent.getText().toString(),
                                                        color)
                                );
                                //hide save button
                                gotoJournalFragment();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> {
                            })
                            .show();
                } else {
                    gotoJournalFragment();
                }
            } else {
                if (binding.noteHint.getText().toString().trim().isEmpty()){
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
                }
                gotoJournalFragment();
            }
        });
    }

    private void onDeleteNote() {
        if (binding.fabDeleteNote.getVisibility() == View.VISIBLE) {
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

    private void validateNote() {
        final String REQUIRED = "Required*";
        final String NO_TITLE = "Title is empty*";
        final String NO_CONTENT = "Content description is empty*";
        binding.noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    binding.noteHint.setText(REQUIRED);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.noteHint.setText(NO_TITLE);
                } else {
                    if (binding.noteContent.getText().toString().trim().isEmpty()) {
                        binding.noteHint.setText(NO_CONTENT);
                    } else {
                        binding.noteHint.setText("");
                    }
                }
            }
        });
        binding.noteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    binding.noteHint.setText(REQUIRED);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.noteHint.setText(NO_CONTENT);
                } else {
                    if (binding.noteTitle.getText().toString().trim().isEmpty()) {
                        binding.noteHint.setText(NO_TITLE);
                    } else {
                        binding.noteHint.setText("");
                    }
                }
            }
        });
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
        getChildFragmentManager().beginTransaction()
                .replace(binding.addNewNoteFrameLayout.getId(), new JournalFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        binding.addNewNoteContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}