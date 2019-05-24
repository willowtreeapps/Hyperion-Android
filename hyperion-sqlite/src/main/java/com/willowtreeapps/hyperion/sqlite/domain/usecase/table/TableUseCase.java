package com.willowtreeapps.hyperion.sqlite.domain.usecase.table;

import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.UseCase;
import com.willowtreeapps.hyperion.sqlite.model.ColumnInfo;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableItem;

import java.util.List;

import io.reactivex.Single;

public interface TableUseCase extends UseCase {
    Single<ColumnInfo> fetchTableInfo(@NonNull String tableName);
    Single<List<TableItem>> fetchDatabaseTables();
}
