package com.habitdev.sprout.ui.menu.journal.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.note.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_journal_parent_note_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bindNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteSubtitle, noteDateTime;
        CardView layout_note;
        Drawable cloud, amethyst, sunflower, nephritis, brightsky_blue, alzarin;
        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_brightsky_blue, cs_alzarin;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteSubtitle = itemView.findViewById(R.id.noteSubtitle);
            noteDateTime = itemView.findViewById(R.id.note_DateTime);
            layout_note = itemView.findViewById(R.id.layout_note);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_cloud);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_amethyst);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_sunflower);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_nephritis);
            brightsky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_brightsky_blue);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_alzarin);

            cs_cloud = ContextCompat.getColorStateList(itemView.getContext(), R.color.CLOUDS);
            cs_amethyst = ContextCompat.getColorStateList(itemView.getContext(), R.color.AMETHYST);
            cs_sunflower = ContextCompat.getColorStateList(itemView.getContext(), R.color.SUNFLOWER);
            cs_nephritis = ContextCompat.getColorStateList(itemView.getContext(), R.color.NEPHRITIS);
            cs_brightsky_blue = ContextCompat.getColorStateList(itemView.getContext(), R.color.BRIGHT_SKY_BLUE);
            cs_alzarin = ContextCompat.getColorStateList(itemView.getContext(), R.color.ALIZARIN);
        }

        void bindNote(Note note) {
            noteTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                noteSubtitle.setVisibility(View.GONE);
            } else {
                noteSubtitle.setText(note.getSubtitle());
            }
            noteDateTime.setText(note.getDateTime());

            switch (note.getColor()){
                case "amethyst":
                    layout_note.setBackground(amethyst);
                    layout_note.setBackgroundTintList(cs_amethyst);
                    break;
                case "sunflower":
                    layout_note.setBackground(sunflower);
                    layout_note.setBackgroundTintList(cs_sunflower);
                    break;
                case "nephritis":
                    layout_note.setBackground(nephritis);
                    layout_note.setBackgroundTintList(cs_nephritis);
                    break;
                case "brightsky_blue":
                    layout_note.setBackground(brightsky_blue);
                    layout_note.setBackgroundTintList(cs_brightsky_blue);
                    break;
                case "alzarin":
                    layout_note.setBackground(alzarin);
                    layout_note.setBackgroundTintList(cs_alzarin);
                    break;
                default:
                    layout_note.setBackground(cloud);
                    layout_note.setBackgroundTintList(cs_cloud);
                    break;
            }
        }
    }
}
