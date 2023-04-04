package com.habitdev.sprout.utill.diffutils;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.note.model.Note;

import java.util.List;

public class NotesDiffUtil extends DiffUtil.Callback {

    private final List<Note> oldNoteList;
    private final List<Note> newNoteList;

    public NotesDiffUtil(List<Note> oldNoteList, List<Note> newNoteList) {
        this.oldNoteList = oldNoteList;
        this.newNoteList = newNoteList;
    }

    @Override
    public int getOldListSize() {
        return oldNoteList == null ? 0 : oldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return newNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).getPk_note_uid() == newNoteList.get(newItemPosition).getPk_note_uid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldNoteList.get(oldItemPosition);
        Note newNote = newNoteList.get(newItemPosition);
        return oldNote.getTitle().equals(newNote.getTitle()) &&
                oldNote.getSubtitle().equals(newNote.getSubtitle()) &&
                oldNote.getNoteContent().equals(newNote.getNoteContent()) &&
                oldNote.getColor().equals(newNote.getColor());
    }
}
