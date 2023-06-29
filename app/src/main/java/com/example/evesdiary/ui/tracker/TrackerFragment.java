package com.example.evesdiary.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.evesdiary.databinding.FragmentTrackerBinding;

public class TrackerFragment extends Fragment {

    private FragmentTrackerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrackerViewModel trackerViewModel =
                new ViewModelProvider(this).get(TrackerViewModel.class);

        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTracker;
        trackerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}