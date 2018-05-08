package com.willowtreeapps.hyperion.sqlite.presentation.records;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.willowtreeapps.hyperion.sqlite.R;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;

public class DbRecordViewerActivity extends AppCompatActivity {

    public static void startActivity(Context context, String dbName, String tableName) {
        Intent intent = new Intent(context, DbRecordViewerActivity.class);
        intent.putExtra(ARGS_TABLE_NAME, tableName);
        intent.putExtra(ARGS_DB_NAME, dbName);
        context.startActivity(intent);
    }

    static final String ARGS_TABLE_NAME = "args_table_name";
    static final String ARGS_DB_NAME = "args_db_name";

    private String tableName;
    private TableLayout table;

    private DbRecordsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsql_db_record_viewer_activity);
        table = findViewById(R.id.table);
        tableName = getIntent().getStringExtra(ARGS_TABLE_NAME);
        String dbName = getIntent().getStringExtra(ARGS_DB_NAME);
        initViewModels(dbName);
        setUpTable();
    }

    private void setUpTable() {
        viewModel.createTableRows(this, tableName, new Consumer<List<TableRow>>() {
            @Override
            public void accept(List<TableRow> tableRows) throws Exception {
                for (TableRow row: tableRows) {
                    table.addView(row);
                }
            }
        });
    }

    private void initViewModels(String databaseName) {
        final File dbFile = getDatabasePath(databaseName);
        viewModel = ViewModelProviders.of(this).get(DbRecordsViewModel.class);
        viewModel.initDatabase(dbFile);
    }
}
