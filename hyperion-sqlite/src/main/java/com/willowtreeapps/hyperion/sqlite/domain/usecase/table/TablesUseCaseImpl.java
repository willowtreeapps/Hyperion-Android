package com.willowtreeapps.hyperion.sqlite.domain.usecase.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.BaseUseCase;
import com.willowtreeapps.hyperion.sqlite.model.ColumnInfo;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class TablesUseCaseImpl extends BaseUseCase implements TableUseCase {

    public TablesUseCaseImpl(@NonNull SQLiteDatabase database) {
        super(database);
    }

    @Override
    public Single<ColumnInfo> fetchTableInfo(@NonNull String tableName) {
        return executeRawQuery("PRAGMA table_info(" + tableName + ")", null)
                .map(new Function<Cursor, ColumnInfo>() {
                    @Override
                    public ColumnInfo apply(Cursor cursor) throws Exception {
                        return readFromCursor(cursor);
                    }
                });
    }

    @Override
    public Single<List<TableItem>> fetchDatabaseTables() {
        return executeRawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        .map(new Function<Cursor, List<TableItem>>() {
            @Override
            public List<TableItem> apply(Cursor cursor) throws Exception {
                return parseTableItemFromCursor(cursor);
            }
        });
    }

    private List<TableItem> parseTableItemFromCursor(Cursor cursor) {
        List<TableItem> tables = new ArrayList<>();
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                tables.add(new TableItem(cursor.getString(0)));
            }
        }
        cursor.close();
        return tables;
    }

    private ColumnInfo readFromCursor(Cursor cursor) {
        int nameIdx = cursor.getColumnIndexOrThrow("name");
        int typeIdx = cursor.getColumnIndexOrThrow("type");
        int notNullIdx = cursor.getColumnIndexOrThrow("notnull");
        int defaultValueIdx = cursor.getColumnIndexOrThrow("dflt_value");
        int pkIdx = cursor.getColumnIndex("pk");

        ColumnInfo columnInfo = null;
        while (cursor.moveToNext()) {
            columnInfo = new ColumnInfo(cursor.getString(nameIdx),
                    cursor.getString(typeIdx),
                    cursor.getInt(notNullIdx) == 1,
                    cursor.getString(defaultValueIdx),
                    cursor.getInt(pkIdx) == 1);
        }
        cursor.close();
        return columnInfo;
    }

}
