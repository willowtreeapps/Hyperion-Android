package com.willowtreeapps.hyperion.sample;

import android.app.Application;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import timber.log.Timber;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        // Initialize some sample preferences
        Set<String> stringSet = new LinkedHashSet<>(Arrays.asList("Hello", "Hyperion!"));
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("KEY_BOOLEAN", true)
                .putFloat("KEY_FLOAT", 12.34f)
                .putInt("KEY_INT", 1234)
                .putLong("KEY_LONG", 1234L)
                .putString("KEY_STRING", "Hello Hyperion!")
                .putStringSet("KEY_STRING_SET", stringSet)
                .apply();

        getSharedPreferences("sample_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("KEY_BOOLEAN", true)
                .apply();
    }
}