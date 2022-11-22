package com.habitdev.sprout.ui.menu.journal.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.Note;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.databinding.FragmentAddNewNoteBinding;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {

    private FragmentAddNewNoteBinding binding;
    private final FragmentManager fragmentManager;
    private int current_selected_color;
    private int old_selected_color;

    public AddNoteFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false);

        binding.cloudSelected.setImageResource(R.drawable.ic_check);

        setCurrentDate();
        colorSelect();
        onBackPress();
        onSaveNote();
        return binding.getRoot();
    }

    private void setCurrentDate() {
        binding.noteCurrentTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault())
                        .format(new Date())
        );
    }

    private void colorSelect(){
        binding.noteColorSelector.setOnClickListener(v -> {
            if (binding.noteLayoutMiscellaneous.getVisibility() == View.GONE){
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

    private void setSelected_color(){
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color){
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_alzarin));
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_amethyst));
                    break;
                case 3:
                    //brightsky_blue
                    binding.brightskyBlueSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_brightsky_blue));
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_nephritis));
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_sunflower));
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(R.drawable.ic_check);
                    setBackgroundNoteIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_note_indicator_clouds));
                    break;
            }

            switch (old_selected_color){
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //brightsky_blue
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

    private void updateSelectedColorIndex(int newSelected){
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
    }

    private void setBackgroundNoteIndicator(Drawable backgroundNoteIndicator){
        binding.noteColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void onSaveNote() {
        binding.fabSaveNote.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Saved Note", Toast.LENGTH_SHORT).show();
            if(validateNote()){
                NoteViewModel noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
                noteViewModel.insert(
                        (binding.noteSubTitle.getText().toString().trim().isEmpty()) ?
                        new Note(binding.noteTitle.getText().toString(), binding.noteCurrentTime.getText().toString(), binding.noteContent.getText().toString(), "cloud") :
                        new Note(binding.noteTitle.getText().toString(), binding.noteCurrentTime.getText().toString(), binding.noteSubTitle.getText().toString(), binding.noteContent.getText().toString(), "sunflower")
                        );
                gotoJournalFragment();
            }
        });
    }

    private boolean validateNote(){
        if (binding.noteTitle.getText().toString().trim().isEmpty() && binding.noteContent.getText().toString().trim().isEmpty()) {
            binding.noteHint.setText("note title and content is required*");
            return false;
        } else if (binding.noteTitle.getText().toString().trim().isEmpty()){
            binding.noteHint.setText("note title is required*");
            return false;
        } else if (binding.noteContent.getText().toString().trim().isEmpty()){
            binding.noteHint.setText("note content is required*");
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
        Fragment fragment = fragmentManager.findFragmentById(R.id.journalFragment);
        fragmentManager.beginTransaction()
                .replace(binding.addNewNoteFrameLayout.getId(), new JournalFragment())
                .commit();
        binding.addNewNoteContainer.setVisibility(View.GONE);
        Log.d("tag", "gotoJournalFragment: " + fragmentManager.getFragments().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}