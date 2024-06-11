package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.mSharedPref;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;


public class SettingsFragment extends PreferenceFragmentCompat {
    public FragmentManager fragmentManager;
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // theme font size
        getPreferenceManager()
                .findPreference(getString(R.string.mPreference))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    // neue shared Preference
                    SharedPreferences.Editor editor = mSharedPref.edit();

                    // meine default text-size preferences aus main activity holen und veraendern
                    int[] mDefaultFontSizes = getResources().getIntArray(R.array.default_text_size);
                    for (Integer item : mDefaultFontSizes) {
                        int result = item + Integer.parseInt((String) newValue);
                        //utils.showToast(getContext(), "> " + result);
                        editor.putInt(String.format("mFontSize_%s", item), result);
                    }
                    editor.apply();
                    Log.i("setFrag","" + mSharedPref.getAll());
                    return true;
                });
        // dark modus
        getPreferenceManager()
                .findPreference(getString(R.string.settings_theme_key))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    if (getContext().getString(R.string.settings_theme_value_dark).equals(newValue)) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else if (getContext().getString(R.string.settings_theme_value_light).equals(newValue)) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                    return true;
                });
        // locale
        getPreferenceManager()
                .findPreference(getString(R.string.settings_locale_key))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    LocaleListCompat appLocale;
                    String[] localeArray = getResources().getStringArray(R.array.settings_locale_values);
                    appLocale = LocaleListCompat.forLanguageTags("de");
                    for (String item : localeArray){
                        if (item.equals(newValue)) {
                            appLocale = LocaleListCompat.forLanguageTags(item);
                        }
                    }
                    //utils.showToast(getContext(), "applocale " + appLocale);
                    AppCompatDelegate.setApplicationLocales(appLocale);
                    return true;
                });
    }

}