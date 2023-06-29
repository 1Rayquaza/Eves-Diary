package com.example.evesdiary.ui.insights;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InsightsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InsightsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is insights fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}