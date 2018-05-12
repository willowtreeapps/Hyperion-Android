package com.willowtreeapps.hyperion.sqlite.presentation.database;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TablesListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@HyperionIgnore
public class DatabaseListActivity extends AppCompatActivity implements DatabaseListAdapter.OnDatabaseSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsql_database_list);
        setSupportActionBar((Toolbar) findViewById(R.id.hsql_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.hsql_database_list_heading);
        }

        final RecyclerView list = findViewById(R.id.hsql_list);
        list.setLayoutManager(new LinearLayoutManager(this));

        final List<String> databaseList = fetchDatabaseList();
        DatabaseListAdapter adapter = new DatabaseListAdapter(databaseList);
        adapter.setListener(this);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<String> fetchDatabaseList() {

        Set<String> dbNames = new HashSet<>();
        for (String db : databaseList()) {
            if (!db.endsWith("-journal") && !db.endsWith("-wal") && !db.endsWith("-shm")) {
                dbNames.add(db);
            }
        }
        return new ArrayList<>(dbNames);
    }

    @Override
    public void onClick(String databaseName) {
        TablesListActivity.startActivity(this, databaseName);
    }
}
