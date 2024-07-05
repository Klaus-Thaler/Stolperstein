package com.example.stolperstein.ui.settings;

import static com.example.stolperstein.MainActivity.CacheKMLFileName;
import static com.example.stolperstein.MainActivity.PermsStorage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SettingViewModel settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView infoText = binding.firstDescription;
        SettingViewModel.downloadInfo.observe(getViewLifecycleOwner(), infoText::setText);

        if (FileManager.CacheFileExist(requireActivity(),CacheKMLFileName)) {
            String date = FileManager.CacheFileLastModified(requireContext(), CacheKMLFileName);
            String descript1 = getResources().getString(R.string.Description_done_1);
            String descript2 = getResources().getString(R.string.Description_done_2);
            infoText.setText(descript1 + ": " + date +  "\n" + descript2);
        } else {
            infoText.setText(R.string.Greeting_Description);
        }

        final ProgressBar progBarSet = binding.counterWeb;
        SettingViewModel.progBarSet.observe(getViewLifecycleOwner(), progBarSet::setProgress);
        //progBarSet.setProgressTintList(ColorStateList.valueOf(Color.RED));

        final TextView settingSearch = binding.listSetup;
        SettingViewModel.mSearch.observe(getViewLifecycleOwner(), settingSearch::setText);

        final  TextView furtherText = binding.furtherInfo;
        furtherText.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView license = binding.licenseInfo;
        license.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView osm = binding.licenseOSM;
        osm.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView fdroid = binding.fdroidInfo;
        fdroid.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView wiki = binding.licenseWiki;
        wiki.setMovementMethod(LinkMovementMethod.getInstance());

        final Button settingButton = binding.searchStart;
        SettingViewModel.mButton.observe(getViewLifecycleOwner(), settingButton::setText);

        final ImageView imageLGPL = binding.imageLGPLlogo;
        imageLGPL.setImageBitmap(utils.getBitmapFromAsset(requireContext(),"LGPLv3_Logo.png"));

        // worker background process
        settingButton.setOnClickListener(v -> {
            // Background Thread with worker
            WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(getWebWorker.class).build();
            WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);
        });

        // language
        //  radiobutton lang de
        //utils.showToast(getContext(), "-" + AppCompatDelegate.getApplicationLocales());
        View radio_german = root.findViewById(R.id.lang_german);
        radio_german.setOnClickListener(v -> {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("de");
            AppCompatDelegate.setApplicationLocales(appLocale);
        });
        //  radiobuttons lang en
        View radio_english = root.findViewById(R.id.lang_english);
        radio_english.setOnClickListener(v -> {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("en");
            AppCompatDelegate.setApplicationLocales(appLocale);
        });
        //  radiobuttons lang fr
        View radio_france = root.findViewById(R.id.lang_france);
        radio_france.setOnClickListener(v -> {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("fr");
            AppCompatDelegate.setApplicationLocales(appLocale);
        });
        //  radiobuttons lang uk
        View radio_ukrainian = root.findViewById(R.id.lang_ukrainian);
        radio_ukrainian.setOnClickListener(v -> {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("uk");
            AppCompatDelegate.setApplicationLocales(appLocale);
        });

        // theme
        // crashed todo
        /*
        utils.showToast(getContext(),": " + AppCompatDelegate.getDefaultNightMode());
        // system
        View radio_system = root.findViewById(R.id.theme_system);
        radio_system.setOnClickListener(v -> {
            //utils.showToast(getContext(),"system: " + AppCompatDelegate.getDefaultNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            //utils.showToast(getContext(),"system: " + AppCompatDelegate.getDefaultNightMode());
        });
        // hell
        View radio_light = root.findViewById(R.id.theme_light);
        radio_light.setOnClickListener(v -> {
            //utils.showToast(getContext(),"light: " + AppCompatDelegate.getDefaultNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //utils.showToast(getContext(),"light: " + AppCompatDelegate.getDefaultNightMode());
        });
        // dunkel
        View radio_dark = root.findViewById(R.id.theme_dark);
        radio_dark.setOnClickListener(v -> {
            //utils.showToast(getContext(),"dark: " + AppCompatDelegate.getDefaultNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //utils.showToast(getContext(),"dark: " + AppCompatDelegate.getDefaultNightMode());
        });

        */

        // download kml file
        TextView downloadText = root.findViewById(R.id.download_cachefile_text);
        SettingViewModel.dText.observe(getViewLifecycleOwner(), downloadText::setText);
        Button downloadButton = root.findViewById(R.id.button_download_file);

        if (FileManager.CacheFileExist(requireContext(), CacheKMLFileName)) {
            downloadButton.setOnClickListener(v -> {
                ActivityCompat.requestPermissions(requireActivity(), PermsStorage, 2);
                FileManager.saveDataInDownload(requireContext(), CacheKMLFileName);
                SettingViewModel.dText.postValue("Download okay");
            });
        } else {
            downloadButton.setActivated(false);
            downloadText.setText(getText(R.string.No_data_available));
            //utils.showToast(requireContext(),"No data available yet. Please run Setting first.");
        }

    return root;
    }
}