package com.willowtreeapps.hyperion.sharedpreferences.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.sharedpreferences.R;

@HyperionIgnore
public class SharedPreferencesDetailActivity extends AppCompatActivity {

    public static final String KEY_PREFS_NAME = "prefs_name";

    public static void start(@NonNull Context context, @NonNull String prefsName) {
        Intent intent = new Intent(context, SharedPreferencesDetailActivity.class);
        intent.putExtra(KEY_PREFS_NAME, prefsName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsp_activity_shared_preferences_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.hsp_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(KEY_PREFS_NAME));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}