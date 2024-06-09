package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.mSharedPref;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.ui.WebData.WebDataFragment;


public class SettingsFragment extends PreferenceFragmentCompat {
    public FragmentManager fragmentManager;
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // theme font size
        getPreferenceManager()
                .findPreference(getString(R.string.settings_textlabel_key))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    // neue shared Preference
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    Integer[] mFontSize = new Integer[]{12, 14, 16, 18, 20, 24, 28, 32};

                    if (getContext().getString(R.string.sTextSmallLabel).equals(newValue)) {
                        utils.showToast(getContext(), newValue.toString());
                        for (Integer item : mFontSize) {
                            editor.putInt(String.format("mFontSize_%s", item), item - 4);
                        }
                    } else if (getContext().getString(R.string.sTextMediumLabel).equals(newValue)) {
                        utils.showToast(getContext(), newValue.toString());
                        for (Integer item : mFontSize) {
                            editor.putInt(String.format("mFontSize_%s", item), item - 2);
                        }
                    } else if (getContext().getString(R.string.sTextModerateLabel).equals(newValue)) {
                        utils.showToast(getContext(), newValue.toString());
                        for (Integer item : mFontSize) {
                            editor.putInt(String.format("mFontSize_%s", item), item);
                        }
                    } else if (getContext().getString(R.string.sTextLargeLabel).equals(newValue)) {
                        utils.showToast(getContext(), newValue.toString());
                        for (Integer item : mFontSize) {
                            editor.putInt(String.format("mFontSize_%s", item), item + 2);
                        }
                    } else if (getContext().getString(R.string.sTextExtraLargeLabel).equals(newValue)) {
                        utils.showToast(getContext(), newValue.toString());
                        for (Integer item : mFontSize) {
                            editor.putInt(String.format("mFontSize_%s", item), item + 4);
                        }
                    }
                    editor.apply();
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
                    if (getContext().getString(R.string.locale_en).equals(newValue)) {
                        appLocale = LocaleListCompat.forLanguageTags(getContext().getString(R.string.locale_en));
                    } else {
                        appLocale = LocaleListCompat.forLanguageTags(getContext().getString(R.string.locale_de));
                    }
                    // Call this on the main thread as it may require Activity.restart()
                    AppCompatDelegate.setApplicationLocales(appLocale);
                    return true;
                });

        // todo
        /*
        // start fragment
        getPreferenceManager()
                .findPreference(getString(R.string.hello_blank_fragment))
                .setOnPreferenceChangeListener(((preference, newValue) -> {


                    utils.showToast(getContext(), "click");

                    @SuppressLint("CommitTransaction") FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main,new Fragment());
                    fragmentTransaction.commit();



                    return true;
                }));
        */
    }
}