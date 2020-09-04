package com.willowtreeapps.hyperion.sharedpreferences.detail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.sharedpreferences.R;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SharedPreferencesNavigationView extends FrameLayout {

    private final PreferenceListener preferenceListener = new PreferenceListener();

    private SharedPreferences sharedPreferences;
    private SharedPreferencesDetailAdapter sharedPreferencesDetailAdapter;

    public SharedPreferencesNavigationView(@NonNull Context context) {
        super(context);
    }

    public SharedPreferencesNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SharedPreferencesNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final Activity activity = (Activity) getContext();
        final String prefsName = activity.getIntent()
                .getStringExtra(SharedPreferencesDetailActivity.KEY_PREFS_NAME);
        sharedPreferences = getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        sharedPreferencesDetailAdapter = new SharedPreferencesDetailAdapter(sharedPreferences);
        RecyclerView recyclerView = findViewById(R.id.hsp_navigation_recycler);
        recyclerView.setAdapter(sharedPreferencesDetailAdapter);

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
            sharedPreferencesDetailAdapter.notifyKeyChanged(key);
        }
    }
}