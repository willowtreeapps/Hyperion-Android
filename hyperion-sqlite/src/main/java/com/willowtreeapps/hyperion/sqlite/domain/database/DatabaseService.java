package com.willowtreeapps.hyperion.sqlite.domain.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import io.reactivex.Flowable;

public class DatabaseService {

    private final SQLiteDatabase db;

    public DatabaseService(@NonNull SQLiteDatabase db) {
        this.db = db;
    }

    public Flowable<Cursor> performQuery(@NonNull String query, String[] args) {
        return Flowable.just(db.rawQuery(query, args));
    }
}
