package com.example.b.expensewatcher.Utilities;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.b.expensewatcher.DashboardActivity;
import com.example.b.expensewatcher.R;

import java.util.Locale;


/**
 * Copied and modified from udacity course on Android Development
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    // setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();

        if (preference instanceof ListPreference) {
            /* For list preferences, look up the correct display value in */
            /* the preference's 'entries' list (since they have separate labels/values). */
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_dashboard);

        // Set the preference summary on each preference that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
           if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
                if(value == "en" || value == "om" || value == "am" || value == "so-rET" || value == "ti"){
                   // Log.d("LANG VALUE",value);
                    setLocale(value);
                }
            }
            if(p instanceof CheckBoxPreference) {
                boolean value = sharedPreferences.getBoolean("notifications", false);
                setPreferenceSummary(p, value);
            }
        }
    }

    // Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart

    @Override
    public void onStart() {
        super.onStart();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    // Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

    @Override
    public void onStop() {
        super.onStop();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
                if(key.equals("language")){
                    String value = sharedPreferences.getString(key, "");
                    Log.d("LANG VALUE",value);
                    setLocale(value);
                }
            }
            if(preference instanceof CheckBoxPreference) {
                setPreferenceSummary(preference, sharedPreferences.getBoolean("notifications",false));
            }
        }
    }

    public void setLocale(String lang) {

        Locale myLocale;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent i = new Intent(getContext(), DashboardActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        switch(lang) {
            case "en":
                Toast.makeText(getContext(),"Language: English",Toast.LENGTH_SHORT).show(); break;
            case "am":
                Toast.makeText(getContext(),"Language: Amharic",Toast.LENGTH_SHORT).show(); break;
            case "om":
                Toast.makeText(getContext(),"Language: Oromigna",Toast.LENGTH_SHORT).show(); break;
            case "so-rET":
                Toast.makeText(getContext(),"Language: Somali",Toast.LENGTH_SHORT).show(); break;
            case "ti":
                Toast.makeText(getContext(),"Language: Tigrigna",Toast.LENGTH_SHORT).show(); break;
            default:
                break;
        }
    }
}
