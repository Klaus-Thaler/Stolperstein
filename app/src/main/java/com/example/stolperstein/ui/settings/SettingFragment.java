package com.example.stolperstein.ui.settings;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.MainActivity;
import com.example.stolperstein.R;
import com.example.stolperstein.classes.getWebWorker;
import com.example.stolperstein.databinding.FragmentSettingBinding;
import com.example.stolperstein.ui.settings.SettingViewModel;
import com.example.stolperstein.classes.getWebSite;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textViewDesc = binding.firstDescription;
        // textview clickable
        textViewDesc.setMovementMethod(LinkMovementMethod.getInstance());

        final ProgressBar progBarSet = binding.counterWeb;
        SettingViewModel.progBarSet.observe(getViewLifecycleOwner(), progBarSet::setProgress);
        progBarSet.setProgressTintList(ColorStateList.valueOf(Color.RED));

        final TextView settingSearch = binding.listSetup;
        SettingViewModel.mSearch.observe(getViewLifecycleOwner(), settingSearch::setText);


        final Button settingButton = binding.searchStart;
        SettingViewModel.mButton.observe(getViewLifecycleOwner(), settingButton::setText);
        if (FileManager.CacheFileExist(requireContext(), MainActivity.CacheFileName)) {
            settingSearch.setText(R.string.die_suche_war_erfolgreich_);
        }

        settingButton.setOnClickListener(v -> {
            // Background Thread with worker
            WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(getWebWorker.class).build();
            WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);
        });
        return root;
    }
}