package com.habitdev.sprout.ui.menu.analytic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.habitdev.sprout.databinding.FragmentAnalyticBinding;

import java.util.ArrayList;
import java.util.List;

public class AnalyticFragment extends Fragment {

    private FragmentAnalyticBinding binding;

    public AnalyticFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyticBinding.inflate(inflater, container, false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(0, new BarEntry(1,3));
//        barEntries.add(0, new BarEntry(1,5));
//        barEntries.add(0, new BarEntry(3,7));
//        barEntries.add(0, new BarEntry(2,4));
//
//        barEntries.add(1, new BarEntry(0,3));
//        barEntries.add(1, new BarEntry(1,5));
//        barEntries.add(2, new BarEntry(3,7));
//        barEntries.add(2, new BarEntry(2,4));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Testing MPL CHARTS");
        BarData barData = new BarData(barDataSet);

        binding.barchart.setData(barData);
//        barChart.invalidate();

        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
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