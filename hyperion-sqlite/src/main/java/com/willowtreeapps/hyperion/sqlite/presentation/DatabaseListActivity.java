package com.willowtreeapps.hyperion.sqlite.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.hyperion.sqlite.browser.R;

import java.util.Arrays;
import java.util.List;

public class DatabaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsql_database_list);
        setSupportActionBar((Toolbar) findViewById(R.id.hsql_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final RecyclerView list = findViewById(R.id.hsql_list);
        list.setLayoutManager(new LinearLayoutManager(this));

        final List<String> databaseList = fetchDatabaseList();
        list.setAdapter(new DatabaseListAdapter(databaseList));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<String> fetchDatabaseList() {
        return Arrays.asList(databaseList());
    }
}
