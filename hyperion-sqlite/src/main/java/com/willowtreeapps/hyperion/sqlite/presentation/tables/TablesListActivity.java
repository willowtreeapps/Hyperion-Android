package com.willowtreeapps.hyperion.sqlite.presentation.tables;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.hyperion.sqlite.R;

import java.util.Arrays;
import java.util.List;

public class TablesListActivity extends AppCompatActivity {

    private static final String ARGS_DB_NAME = "args_db_name";

    public static void startActivity(@NonNull Context context, @NonNull String databaseName) {
        final Intent intent = new Intent(context, TablesListActivity.class);
        intent.putExtra(ARGS_DB_NAME, databaseName);
        context.startActivity(intent);
    }

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

        final List<TableItem> databaseList = fetchTables();
        list.setAdapter(new TablesListAdapter(databaseList));
    }

    private List<TableItem> fetchTables() {
        //TODO fetch using SQLiteDatabase.opendatabase()??
        return Arrays.asList(new TableItem("Table 1"), new TableItem("Table 2"));
    }
}
