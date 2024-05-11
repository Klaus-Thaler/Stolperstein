package com.example.stolperstein.ui.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

public class SettingViewModel extends ViewModel {
    public static MutableLiveData<String> mSearch;
    public static MutableLiveData<String> mButton;
    public static MutableLiveData<Integer> progBarSet;


    public SettingViewModel() throws IOException {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is Setting fragment");

        progBarSet = new MutableLiveData<>();
        progBarSet.setValue(0);

        mSearch = new MutableLiveData<>();

        mButton = new MutableLiveData<>();
        //mButton.postValue("Stop");

    }
}