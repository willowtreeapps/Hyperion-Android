package com.willowtreeapps.hyperion.sqlite.domain.usecase.records;

import android.database.Cursor;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.UseCase;

import io.reactivex.Single;

public interface RecordsViewerUseCase extends UseCase {
    Single<Cursor> fetchRecords(String tableName);
}
