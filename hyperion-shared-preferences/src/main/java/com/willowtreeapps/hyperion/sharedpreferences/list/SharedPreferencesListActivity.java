package com.willowtreeapps.hyperion.sharedpreferences.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.sharedpreferences.R;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@HyperionIgnore
public class SharedPreferencesListActivity extends AppCompatActivity {

    private static final String PREFS_DIRECTORY = "shared_prefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsp_activity_shared_preferences_list);
        setSupportActionBar((Toolbar) findViewById(R.id.hsp_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final List<String> preferences = getPreferences();
        final RecyclerView recyclerView = findViewById(R.id.hsp_prefs_recycler);
        recyclerView.setAdapter(new SharedPreferencesListAdapter(preferences));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<String> getPreferences() {
        File prefsDirectory = new File(getApplicationInfo().dataDir,PREFS_DIRECTORY);

        if (prefsDirectory.exists() && prefsDirectory.isDirectory()) {
            String[] list = prefsDirectory.list();
            if (list == null) {
                return Collections.emptyList();
            }
            removeExtensions(list);
            return Arrays.asList(list);
        } else {
            return Collections.emptyList();
        }
    }

    private void removeExtensions(String[] list) {
        for (int i = 0; i < list.length; i++) {
            String item = list[i];
            list[i] = removeExtension(item);
        }
    }

    private String removeExtension(String str) {
        int index = str.lastIndexOf('.');
        if (index > 0) {
            return str.substring(0, index);
        }
        return str;
    }
}