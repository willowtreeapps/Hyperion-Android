package com.willowtreeapps.hyperion.sqlite.domain.usecase.records;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.BaseUseCase;

import io.reactivex.Single;

public class RecordsViewerUseCaseImpl extends BaseUseCase implements RecordsViewerUseCase {

    public RecordsViewerUseCaseImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Single<Cursor> fetchRecords(String tableName) {
        return executeRawQuery("SELECT * FROM USER_TABLE", null);
    }
}
