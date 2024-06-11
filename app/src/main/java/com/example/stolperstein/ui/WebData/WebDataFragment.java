package com.example.stolperstein.ui.WebData;

import static com.example.stolperstein.MainActivity.mSharedPref;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.stolperstein.databinding.FragmentWebdataBinding;

public class WebDataFragment extends Fragment {
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SettingViewModel settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        FragmentWebdataBinding binding = FragmentWebdataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textFirstWelcome = binding.firstWelcome;
        textFirstWelcome.setTextSize(mSharedPref.getInt("mFontSize_24",24));

        final TextView textViewDesc = binding.firstDescription;
        textViewDesc.setMovementMethod(LinkMovementMethod.getInstance());
        textViewDesc.setTextSize(mSharedPref.getInt("mFontSize_16",16));

        final ProgressBar progBarSet = binding.counterWeb;
        SettingViewModel.progBarSet.observe(getViewLifecycleOwner(), progBarSet::setProgress);
        //progBarSet.setProgressTintList(ColorStateList.valueOf(Color.RED));

        final TextView settingSearch = binding.listSetup;
        SettingViewModel.mSearch.observe(getViewLifecycleOwner(), settingSearch::setText);
        settingSearch.setTextSize(mSharedPref.getInt("mFontSize_16",16));

        final  TextView furtherText = binding.furtherInfo;
        furtherText.setMovementMethod(LinkMovementMethod.getInstance());
        furtherText.setTextSize(mSharedPref.getInt("mFontSize_16",16));

        final Button settingButton = binding.searchStart;
        SettingViewModel.mButton.observe(getViewLifecycleOwner(), settingButton::setText);


        // todo Anzeige last modi der DB -> noch mal?
        /*
        if (FileManager.CacheFileExist(requireContext(), MainActivity.CacheXMLData)) {
            settingSearch.setText(R.string.die_suche_war_erfolgreich_);
        }
        */
        settingButton.setOnClickListener(v -> {
            // Background Thread with worker
            WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(getWebWorker.class).build();
            WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);
        });
        return root;
    }
}