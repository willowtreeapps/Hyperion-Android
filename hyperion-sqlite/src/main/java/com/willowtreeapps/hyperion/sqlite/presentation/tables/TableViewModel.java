package com.willowtreeapps.hyperion.sqlite.presentation.tables;

import android.database.sqlite.SQLiteDatabase;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.table.TableUseCase;
import com.willowtreeapps.hyperion.sqlite.domain.usecase.table.TablesUseCaseImpl;
import com.willowtreeapps.hyperion.sqlite.presentation.DatabaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TableViewModel extends DatabaseViewModel<TableUseCase> {

    @Override
    protected TableUseCase createUsecase(SQLiteDatabase db) {
        return new TablesUseCaseImpl(db);
    }

    public void loadTables(Consumer<List<TableItem>> subscriber) {
        subscriptions.add(getUseCase().fetchDatabaseTables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
