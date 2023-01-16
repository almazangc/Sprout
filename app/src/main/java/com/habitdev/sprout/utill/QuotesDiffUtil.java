package com.habitdev.sprout.utill;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.List;
import java.util.Objects;

public class QuotesDiffUtil extends DiffUtil.Callback {

    private final List<Quotes> oldQuotesList;
    private final List<Quotes> newQuotesList;

    public QuotesDiffUtil(List<Quotes> oldQuotesList, List<Quotes> newQuotesList) {
        this.oldQuotesList = oldQuotesList;
        this.newQuotesList = newQuotesList;
    }

    @Override
    public int getOldListSize() {
        return oldQuotesList == null ? 0 : oldQuotesList.size();
    }

    @Override
    public int getNewListSize() {
        return newQuotesList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldQuotesList.get(oldItemPosition).getId(), newQuotesList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Quotes oldQuotes = oldQuotesList.get(oldItemPosition);
        Quotes newQuotes = newQuotesList.get(newItemPosition);
        return oldQuotes.getAuthor().equals(newQuotes.getAuthor()) &&
                oldQuotes.getQuoted().equals(newQuotes.getQuoted());
    }
}
