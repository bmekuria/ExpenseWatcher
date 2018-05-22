package com.example.b.expensewatcher.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.b.expensewatcher.R;

import java.util.Locale;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by B on 08-Jun-17.
 */

public class ExpenseWatcherPreferences {


    /**
     * Returns the language currently set in Preferences.
     * @param context Context used to get SharedPreferences
     * @return the current language the user has set in SharedPreferences


    public static void loadLanguage(Context context){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_eng_value);

        String language = prefs.getString(keyForLanguage, defaultLanguage);
        changeLang(context, language);
    }


    /*public void loadLocale(Context context) {
        String langPref = "Language";
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String language = prefs.getString(langPref, "");
        changeLang(context, language);
    }


    public static void changeLang(Context context, String lang) {
        if (lang.equalsIgnoreCase(""))
            return;

        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());

        /*SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_language_key), lang);
        editor.commit();

    }

  /*  public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }*/


    public String getPreferredLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_eng_value);

        return prefs.getString(keyForLanguage,defaultLanguage);

    }

    /**
     * Returns the account currently set in Preferences.
     * @param context Context used to get SharedPreferences
     * @return the current account the user has set in SharedPreferences
     */
    public String getPreferredAccount(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForAccount = context.getString(R.string.pref_account_key);
        String defaultAccount = context.getString(R.string.pref_account_main_value);

        return prefs.getString(keyForAccount, defaultAccount);
    }

    /**
     * Returns the status of the Notifications checkbox
     * @param ctx Context used to get SharedPreferences
     * @return boolean value the user has set in SharedPreferences
     */
    public boolean getPreferredNotifications(Context ctx) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        return prefs.getBoolean("notifications",false);
    }

    public void LoadLanguage(Context ctx) {

        Locale locale = new Locale(getPreferredLanguage(ctx));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getResources().updateConfiguration(config,ctx.getResources().getDisplayMetrics());
    }



}
