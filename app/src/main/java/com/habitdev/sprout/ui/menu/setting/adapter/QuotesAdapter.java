package com.habitdev.sprout.ui.menu.setting.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.quotes.model.Quotes;
import com.habitdev.sprout.utill.QuotesDiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuotesAdapter extends ArrayAdapter<Quotes> {

    private List<Quotes> oldQuotesList;
    private List<Quotes> originalNoteList;
    private Timer timer;

    public QuotesAdapter(@NonNull Context context, @NonNull List<Quotes> oldQuotesList) {
        super(context, 0, oldQuotesList);
        this.oldQuotesList = oldQuotesList;
        this.originalNoteList = new ArrayList<>(oldQuotesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_setting_achievement_quotes_item, parent, false);
        }

        Quotes quotes = oldQuotesList.get(position);
        TextView quoted = itemView.findViewById(R.id.quotes_drop_item_quoted);
        TextView author = itemView.findViewById(R.id.quotes_drop_item_author);

        if (quotes != null) {
            quoted.setText(quotes.getQuoted());
            author.setText(quotes.getAuthor());
        }

        return itemView;
    }

    @Override
    public int getCount() {
        return oldQuotesList.size();
    }

    @Override
    public Quotes getItem(int position) {
        return oldQuotesList.get(position);
    }

    /**
     * Filters the list of quotes based on the search key.
     *
     * @param keyword the search key
     */
    public void filter(final String keyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (keyword.isEmpty()) {
                    oldQuotesList = originalNoteList;
                } else {
                    List<Quotes> filteredList = new ArrayList<>();
                    for (Quotes item : oldQuotesList) {
                        if (item.getAuthor().toLowerCase().contains(keyword) || item.getQuoted().toLowerCase().contains(keyword)) {
                            filteredList.add(item);
                        }
                    }
                    oldQuotesList = filteredList;
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        DiffUtil.Callback DIFF_CALLBACK = new QuotesDiffUtil(originalNoteList, oldQuotesList);
                        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
                        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(new ListUpdateCallback() {
                            @Override
                            public void onInserted(int position, int count) {
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onRemoved(int position, int count) {
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onMoved(int fromPosition, int toPosition) {
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onChanged(int position, int count, @Nullable Object payload) {
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }, 500);

        notifyDataSetChanged();
    }

    public void cancelTimer() {
        if (timer != null) timer.cancel();
    }

    /**
     * Updates the list of quotes with new quotes and animates the changes.
     *
     * @param newQuotesList the new list of quotes
     */
    public void setNewQuotesList(List<Quotes> newQuotesList) {
        DiffUtil.Callback DIFF_CALLBACK = new QuotesDiffUtil(oldQuotesList, newQuotesList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);

        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                oldQuotesList = newQuotesList;
                originalNoteList = oldQuotesList;
            }

            @Override
            public void onRemoved(int position, int count) {
                oldQuotesList = newQuotesList;
                originalNoteList = oldQuotesList;
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                oldQuotesList = newQuotesList;
                originalNoteList = oldQuotesList;
            }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                oldQuotesList = newQuotesList;
                originalNoteList = oldQuotesList;
            }
        });
    }
}
