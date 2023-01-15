package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.quotes.QuotesViewModel;
import com.habitdev.sprout.database.quotes.model.Quotes;
import com.habitdev.sprout.databinding.FragmentTerminalBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerminalFragment extends Fragment {

    private FragmentTerminalBinding binding;
    private static QuotesViewModel quotesViewModel;
    private static final List<Quotes>[] quotesList = new List[]{new ArrayList<>()};
    private static int selectedItemPos = -1; // default none selected item

    public interface OnReturnSetting {
        void returnFromTerminalToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public TerminalFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTerminalBinding.inflate(inflater, container, false);
        onBackPress();

        quotesViewModel = new ViewModelProvider(requireActivity()).get(QuotesViewModel.class);

        //Get db ref
        FirebaseFirestore firebaseFirestoreDB = FirebaseFirestore.getInstance();
        setDropItems(firebaseFirestoreDB);

        //Keys
        final String QUOTED_KEY = "quoted";
        final String AUTHOR_KEY = "author";

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String docID = binding.docID.getText().toString().trim();
                final String quoted = binding.quoted.getText().toString().trim();
                final String author = binding.author.getText().toString().trim();

                //create map obj
                Map<String, Object> quotesMap = new HashMap<>(); //impletation of map interface
                quotesMap.put(QUOTED_KEY, quoted);
                quotesMap.put(AUTHOR_KEY, author);

                if (selectedItemPos == -1) {
                    quotesViewModel.insert_quotes(docID, quotesMap);
                    Toast.makeText(requireActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                }

                if (selectedItemPos >= 0 && selectedItemPos < quotesList[0].size()) {
                    Quotes quotes = quotesList[0].get(selectedItemPos);
                    quotes.setQuoted(quoted);
                    quotes.setAuthor(author);
                    quotesViewModel.update_quotes(quotes);
                    Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String docID = binding.docID.getText().toString().trim();
                if (selectedItemPos == -1) {
                    Toast.makeText(requireActivity(), "Item is not found dabase", Toast.LENGTH_SHORT).show();
                }
                if (selectedItemPos >= 0 && selectedItemPos < quotesList[0].size()) {
                    quotesViewModel.delete_quotes(quotesList[0].get(selectedItemPos).getId());
                    quotesList[0].remove(quotesList[0].get(selectedItemPos));
                    selectedItemPos = -1;
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    binding.docID.setText("");
                    binding.docID.setEnabled(true);
                    binding.quoted.setText("");
                    binding.author.setText("");
                    binding.quotesDropItem.setText("");
                    binding.save.setText("Save");
                }
            }
        });

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
            }
        });

        binding.quotesDropItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

        return binding.getRoot();
    }

    private void setDropItems(FirebaseFirestore firebaseFirestoreDB) {
        quotesViewModel.fetchData();
        quotesList[0] = quotesViewModel.getData();

        List<String> quotesID = new ArrayList<>();
        for (Quotes quotes : quotesList[0]){
            quotesID.add(quotes.getQuoted());
        }

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_setting_terminal_quotes_item, quotesID); // handle config changes
        binding.quotesDropItem.setAdapter(adapterItems);

        binding.quotesDropItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItemPos = position;
                Quotes quotes = quotesList[0].get(position);
                binding.docID.setText(quotes.getId());
                binding.docID.setEnabled(false);
                binding.quoted.setText(quotes.getQuoted());
                binding.author.setText(quotes.getAuthor());
                binding.save.setText("Update");
            }
        });

        quotesViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Quotes>>() {
            @Override
            public void onChanged(List<Quotes> quotes) {
                quotesViewModel.fetchData();
                quotesList[0] = quotes;
            }
        });

//        firebaseFirestoreDB.collection("quotes").get(Source.SERVER);
//        firebaseFirestoreDB.collection("quotes")
//                .get(Source.CACHE)
//                .addOnSuccessListener(requireActivity(), new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documents) {
//                        if (documents != null) {
//                            List<String> quotes_doc_id = new ArrayList<>();
//                            List<Quotes> quotesList = documents.toObjects(Quotes.class);
//
//                            if (!quotesList.isEmpty()) {
//                                for (int pos = 0; pos < quotesList.size(); pos++) {
//                                    String documentID = documents.getDocuments().get(pos).getId();
//                                    quotes_doc_id.add(documentID);
//                                }
//                                ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_setting_terminal_quotes_item, quotes_doc_id); // handle config changes
//                                binding.quotesDropItem.setAdapter(adapterItems);
//                                Toast.makeText(requireContext(), "new adapteritems: " + quotes_doc_id.size(), Toast.LENGTH_SHORT).show();
//
//                                binding.quotesDropItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                                        Quotes quotes = quotesList.get(position);
//                                        String documentID = documents.getDocuments().get(position).getId();
//                                        binding.docID.setText(documentID);
//                                        binding.docID.setEnabled(false);
//                                        binding.quoted.setText(quotes.getQuoted());
//                                        binding.author.setText(quotes.getAuthor());
//                                    }
//                                });
//                            } else {
//                                //do nothing for now
//                            }
//                        } else {
//                            Log.d("tag", "SplashScreen: onSuccess() called: null documents ");
//                            firebaseFirestoreDB.collection("quotes").get(Source.SERVER);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("tag", "SplashScreen: onFailure() called: Data is not available on CACHE");
//                    }
//                });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) {
                    mOnReturnSetting.returnFromTerminalToSetting();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}