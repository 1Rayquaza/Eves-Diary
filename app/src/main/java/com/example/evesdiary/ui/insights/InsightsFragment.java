package com.example.evesdiary.ui.insights;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.evesdiary.databinding.FragmentInsightsBinding;

public class InsightsFragment extends Fragment {

    private FragmentInsightsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InsightsViewModel insightsViewModel =
                new ViewModelProvider(this).get(InsightsViewModel.class);

        binding = FragmentInsightsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textInsights;
        insightsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}