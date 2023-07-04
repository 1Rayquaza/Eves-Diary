package com.example.evesdiary.ui.insights;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evesdiary.ArticleAdapter;
import com.example.evesdiary.ArticleData;
import com.example.evesdiary.R;
import com.example.evesdiary.databinding.FragmentInsightsBinding;

public class InsightsFragment extends Fragment {

    private FragmentInsightsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InsightsViewModel insightsViewModel =
                new ViewModelProvider(this).get(InsightsViewModel.class);

        binding = FragmentInsightsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArticleData[] articleData = new ArticleData[]{
                new ArticleData("Menarche", "https://www.firstperiod.org/", R.drawable.menarche),
                new ArticleData("Menopause", "https://www.reddit.com/r/Menopause/wiki/index/#wiki_r.2Fmenopause_wiki", R.drawable.menopause),
                new ArticleData("Self Care", "https://bloodybrilliant.wales/my-period/period-self-care/", R.drawable.selfcare),
                new ArticleData("Period Products", "https://kidshealth.org/en/teens/supplies.html", R.drawable.products),
                new ArticleData("Health Disorders", "https://kidshealth.org/en/teens/supplies.html", R.drawable.health_disorders),
                new ArticleData("Cycle Phases", "https://kidshealth.org/en/teens/supplies.html", R.drawable.phases),

        };

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        ArticleAdapter articleAdapter = new ArticleAdapter(articleData);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(articleAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}