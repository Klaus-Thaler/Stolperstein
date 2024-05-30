package com.example.stolperstein.ui.names;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {
    public static MutableLiveData<String> kringelSet;
    public NameViewModel() throws Exception {
        kringelSet = new MutableLiveData<>();
        kringelSet.setValue("okay");
    }
}