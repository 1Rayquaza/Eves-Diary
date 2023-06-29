package com.example.evesdiary.ui.distraction;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evesdiary.R;
import com.example.evesdiary.databinding.FragmentDistractionBinding;
import com.example.evesdiary.ui.distraction.DistractionViewModel;

public class DistractionFragment extends Fragment {

    private FragmentDistractionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DistractionViewModel exerciseViewModel =
                new ViewModelProvider(this).get(DistractionViewModel.class);

        binding = FragmentDistractionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDistraction;
        exerciseViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}