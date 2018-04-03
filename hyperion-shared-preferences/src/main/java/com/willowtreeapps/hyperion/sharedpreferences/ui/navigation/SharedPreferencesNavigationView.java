package com.willowtreeapps.hyperion.sharedpreferences.ui.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.sharedpreferences.R;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SharedPreferencesNavigationView extends FrameLayout {

    private final SharedPreferencesAdapter sharedPreferencesAdapter;
    private final SharedPreferences sharedPreferences;
    private final PreferenceListener preferenceListener = new PreferenceListener();

    public SharedPreferencesNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public SharedPreferencesNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SharedPreferencesNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesAdapter = new SharedPreferencesAdapter(sharedPreferences);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        RecyclerView recyclerView = findViewById(R.id.hsp_navigation_recycler);
        recyclerView.setAdapter(sharedPreferencesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }

    private class PreferenceListener implements OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            sharedPreferencesAdapter.notifyKeyChanged(key);
        }
    }
}
