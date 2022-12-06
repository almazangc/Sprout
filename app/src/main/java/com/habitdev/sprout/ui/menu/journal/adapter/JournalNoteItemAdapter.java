package com.habitdev.sprout.ui.menu.journal.adapter;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.journal.JournalOnClickListener;
import com.habitdev.sprout.utill.NotesDiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JournalNoteItemAdapter extends RecyclerView.Adapter<JournalNoteItemAdapter.NoteViewHolder> {

    private List<Note> oldNoteList;
    private JournalOnClickListener journalOnClickListener;

    private Timer timer;
    private final List<Note> originalNoteList;

    public JournalNoteItemAdapter(List<Note> oldNoteList) {
        this.oldNoteList = oldNoteList;
        originalNoteList = oldNoteList;
    }

    public void setJournalOnClickListener(JournalOnClickListener journalOnClickListener) {
        this.journalOnClickListener = journalOnClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_journal_parent_note_item, parent, false), journalOnClickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bindNote(oldNoteList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldNoteList.size();
    }

    public void setNewNoteList(List<Note> newNoteList) {
        DiffUtil.Callback DIFF_CALLBACK = new NotesDiffUtil(oldNoteList, newNoteList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldNoteList = newNoteList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteSubtitle, noteContent, noteDateTime;
        RelativeLayout layout_note;
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;
//        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_bright_sky_blue, cs_alzarin;

        public NoteViewHolder(@NonNull View itemView, JournalOnClickListener journalOnClickListener) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteSubtitle = itemView.findViewById(R.id.noteSubtitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            noteDateTime = itemView.findViewById(R.id.noteDateTime);
            layout_note = itemView.findViewById(R.id.layout_note);

            itemView.setOnClickListener(v -> {
                if (journalOnClickListener != null) {
                    int position = getBindingAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        journalOnClickListener.onItemClick(position);
                    }
                }
            });

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

//            cs_cloud = ContextCompat.getColorStateList(itemView.getContext(), R.color.CLOUDS);
//            cs_amethyst = ContextCompat.getColorStateList(itemView.getContext(), R.color.AMETHYST);
//            cs_sunflower = ContextCompat.getColorStateList(itemView.getContext(), R.color.SUNFLOWER);
//            cs_nephritis = ContextCompat.getColorStateList(itemView.getContext(), R.color.NEPHRITIS);
//            cs_bright_sky_blue = ContextCompat.getColorStateList(itemView.getContext(), R.color.BRIGHT_SKY_BLUE);
//            cs_alzarin = ContextCompat.getColorStateList(itemView.getContext(), R.color.ALIZARIN);
        }

        void bindNote(Note note) {

            if (note.getColor().equals(AppColor.ALZARIN.getColor())) {
                layout_note.setBackground(alzarin);
            } else if (note.getColor().equals(AppColor.AMETHYST.getColor())) {
                layout_note.setBackground(amethyst);
            } else if (note.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                layout_note.setBackground(bright_sky_blue);
            } else if (note.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                layout_note.setBackground(nephritis);
            } else if (note.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                layout_note.setBackground(sunflower);
            } else {
                layout_note.setBackground(cloud);
            }

            noteTitle.setText(note.getTitle());

            if (note.getSubtitle().trim().isEmpty()) {
                noteSubtitle.setVisibility(View.GONE);
            } else {
                noteSubtitle.setText(note.getSubtitle());
            }

            noteContent.setText(note.getNoteContent());
            noteDateTime.setText(note.getDateTime());
        }
    }

    public void searchNote(final String keyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (keyword.trim().isEmpty()) {

                    oldNoteList = originalNoteList;

                } else {

                    ArrayList<Note> tempNote = new ArrayList<>();

                    for (Note note : originalNoteList) {

                        String searchKeyword = keyword.toLowerCase();

                        if (
                                note.getTitle().toLowerCase().contains(searchKeyword) ||
                                note.getSubtitle().toLowerCase().contains(searchKeyword) ||
                                note.getNoteContent().toLowerCase().contains(searchKeyword) ||
                                note.getDateTime().toLowerCase().contains(searchKeyword))
                        {
                            tempNote.add(note);
                        }
                    }
                    oldNoteList = tempNote;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        notifyDataSetChanged();

                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) timer.cancel();
    }
}
