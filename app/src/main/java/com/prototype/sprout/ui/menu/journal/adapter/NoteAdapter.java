package com.prototype.sprout.ui.menu.journal.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.prototype.sprout.R;
import com.prototype.sprout.database.note.Note;

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
        LinearLayout layout_note;
        Drawable cloud, amethyst, sunflower, nephritis, brightsky_blue, alzarin;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteSubtitle = itemView.findViewById(R.id.noteSubtitle);
            noteDateTime = itemView.findViewById(R.id.note_DateTime);
            layout_note = itemView.findViewById(R.id.layoutNote);

            //Deprecated way to get drawable resource
//          itemView.getContext().getResources().getDrawable(R.drawable.background_item_cloud)

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_cloud);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_amethyst);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_sunflower);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_nephritis);
            brightsky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_brightsky_blue);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_item_alzarin);
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
                    break;
                case "sunflower":
                    layout_note.setBackground(sunflower);
                    break;
                case "nephritis":
                    layout_note.setBackground(nephritis);
                    break;
                case "brightsky_blue":
                    layout_note.setBackground(brightsky_blue);
                    break;
                case "alzarin":
                    layout_note.setBackground(alzarin);
                    break;
                default:
                    layout_note.setBackground(cloud);
                    break;
            }
        }
    }
}
