package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.quotes.QuotesViewModel;
import com.habitdev.sprout.database.quotes.model.Quotes;
import com.habitdev.sprout.databinding.FragmentTerminalBinding;
import com.habitdev.sprout.ui.menu.setting.adapter.QuotesAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerminalFragment extends Fragment {

    private FragmentTerminalBinding binding;
    private static QuotesViewModel quotesViewModel;
    private static final List<Quotes>[] quotesList = new List[]{new ArrayList<>()};
    private static int selectedItemPos = -1; // default none selected item
    private static QuotesAdapter quotesAdapter;

    public interface OnReturnSetting {
        void returnFromTerminalToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public TerminalFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTerminalBinding.inflate(inflater, container, false);
        onBackPress();

        quotesViewModel = new ViewModelProvider(requireActivity()).get(QuotesViewModel.class);
        setDropItems();
        setSaveOnClearListener();
        setDeleteOnClickListener();
        setClearOnClickListener();

        return binding.getRoot();
    }

    private void setDropItems() {
        quotesViewModel.fetchData();
        quotesList[0] = quotesViewModel.getData();

        List<String> quotesID = new ArrayList<>();
        for (Quotes quotes : quotesList[0]) {
            quotesID.add(quotes.getQuoted());
        }

        quotesAdapter = new QuotesAdapter(requireContext(), quotesList[0]);
        binding.quotesDropItem.setAdapter(quotesAdapter);

        binding.quotesDropItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItemPos = position;
                Quotes quotes = quotesList[0].get(position);
                binding.quotesDropItem.setText(quotesList[0].get(position).getQuoted());
                binding.docID.setText(quotes.getId());
                binding.docID.setEnabled(false);
                binding.quoted.setText(quotes.getQuoted());
                binding.author.setText(quotes.getAuthor());
                binding.save.setText("Update");
            }
        });

        binding.quotesDropItemTextInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quotesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!quotesList[0].isEmpty()) {
                    quotesAdapter.filter(editable.toString().trim().toLowerCase());
                }
            }
        });

        quotesViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Quotes>>() {
            @Override
            public void onChanged(List<Quotes> quotes) {
                quotesViewModel.fetchData();
                quotesAdapter.setNewQuotesList(quotes);
                quotesList[0] = quotes;
                Log.d("tag", "onChanged: obsserving");
            }
        });

        binding.quotesDropItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not neeed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not neeed
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    selectedItemPos = -1; // for none is selected
                    binding.docID.setText("");
                    binding.docID.setEnabled(true);
                    binding.quoted.setText("");
                    binding.author.setText("");
                    binding.save.setText("Save");
                }
            }
        });
    }

    private void setSaveOnClearListener() {
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String docID = binding.docID.getText().toString().trim();
                final String quoted = binding.quoted.getText().toString().trim();
                final String author = binding.author.getText().toString().trim();

                Quotes quote = new Quotes(quoted, author);
                quote.setId(docID);

                if (selectedItemPos == -1) {
                    quotesViewModel.insertQuote(docID, quote);
                    Toast.makeText(requireActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }

                if (selectedItemPos >= 0 && selectedItemPos < quotesList[0].size()) {
                    Quotes quotes = quotesList[0].get(selectedItemPos);
                    quotes.setQuoted(quoted);
                    quotes.setAuthor(author);
                    quotesViewModel.updateQuote(quotes);
                    Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setDeleteOnClickListener() {
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemPos == -1) {
                    Toast.makeText(requireActivity(), "Item is not found dabase", Toast.LENGTH_SHORT).show();
                }
                if (selectedItemPos >= 0 && selectedItemPos < quotesList[0].size()) {
                    quotesViewModel.deleteQuote(quotesList[0].get(selectedItemPos).getId());
                    quotesList[0].remove(quotesList[0].get(selectedItemPos));
                    selectedItemPos = -1;
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    binding.docID.setText("");
                    binding.docID.setEnabled(true);
                    binding.quoted.setText("");
                    binding.author.setText("");
                    binding.quotesDropItem.setText("");
                    binding.save.setText("Save");
                    notifyDataSetChanged();
                }

            }
        });
    }

    private void setClearOnClickListener() {
        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Cleared", Toast.LENGTH_SHORT).show();
                binding.docID.setText("");
                binding.docID.setEnabled(true);
                binding.quoted.setText("");
                binding.author.setText("");
                binding.quotesDropItem.setText("");
                binding.save.setText("Save");
                notifyDataSetChanged();
            }
        });
    }

    private void notifyDataSetChanged() {
        if (quotesAdapter != null && binding.quotesDropItem.getAdapter() != null) {
            quotesAdapter.notifyDataSetChanged();
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) {
                    mOnReturnSetting.returnFromTerminalToSetting();
                }
                quotesViewModel.getLiveData().removeObservers(getViewLifecycleOwner()); // limiter for reads
                Log.d("tag", "handleOnBackPressed: removed observers");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}