package com.rishita.evesdiary.ui.distraction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DistractionViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public DistractionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mind distraction fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}