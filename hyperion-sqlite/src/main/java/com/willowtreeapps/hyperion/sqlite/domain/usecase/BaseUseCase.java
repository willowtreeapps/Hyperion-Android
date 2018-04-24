package com.willowtreeapps.hyperion.sqlite.domain.usecase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class BaseUseCase {

    private final SQLiteDatabase db;

    public BaseUseCase(SQLiteDatabase db) {
        this.db = db;
    }

    protected SQLiteDatabase getDatabase() {
        return this.db;
    }

    protected Single<Cursor> executeRawQuery(final String query, final String[] selectionArgs) {
        return Single.just(db)
                .map(new Function<SQLiteDatabase, Cursor>() {
                    @Override
                    public Cursor apply(SQLiteDatabase database) throws Exception {
                        return db.rawQuery(query, selectionArgs);
                    }
                });
    }
}
