package com.willowtreeapps.hyperion.sharedpreferences.list;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.sharedpreferences.R;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@HyperionIgnore
public class SharedPreferencesListActivity extends AppCompatActivity {

    private static final String PREFS_DIRECTORY = "shared_prefs";
    private SharedPreferencesListAdapter mHspAdapter;

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
        mHspAdapter = new SharedPreferencesListAdapter(preferences);
        recyclerView.setAdapter(mHspAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.hsp_filter, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_item_filter);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mHspAdapter != null) {
                    mHspAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mHspAdapter != null) {
                    mHspAdapter.filter(newText);
                }
                return false;
            }
        });
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