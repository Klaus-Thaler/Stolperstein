package com.example.stolperstein.ui.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingViewModel extends ViewModel {
    public static MutableLiveData<String> mSearch;
    public static MutableLiveData<String> mButton;
    public static MutableLiveData<Integer> progBarSet;
    public static MutableLiveData<String> downloadInfo;
    public static MutableLiveData<String> dText;

    public SettingViewModel() {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is Setting fragment");
        progBarSet = new MutableLiveData<>();
        progBarSet.setValue(0);
        mSearch = new MutableLiveData<>();
        mButton = new MutableLiveData<>();
        downloadInfo = new MutableLiveData<>();
        dText = new MutableLiveData<>();
        //mButton.postValue("Stop");
    }
}