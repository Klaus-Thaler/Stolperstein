package com.example.stolperstein.ui.names;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NameViewModel extends ViewModel {

    public static MutableLiveData<Integer> kringelSet;

    public NameViewModel() throws Exception {
        kringelSet = new MutableLiveData<>();
        kringelSet.setValue(0);
    }
}