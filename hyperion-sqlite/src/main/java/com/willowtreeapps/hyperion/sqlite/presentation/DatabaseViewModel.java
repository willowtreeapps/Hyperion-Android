package com.willowtreeapps.hyperion.sqlite.presentation;

import androidx.lifecycle.ViewModel;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.UseCase;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public abstract class DatabaseViewModel<T extends UseCase> extends ViewModel {

    protected CompositeDisposable subscriptions = new CompositeDisposable();
    protected WeakReference<T> useCase;

    public DatabaseViewModel() {

    }

    public void initDatabase(@NonNull File file) {
        final SQLiteDatabase db
                = SQLiteDatabase.openDatabase(file.getPath(),
                null, SQLiteDatabase.OPEN_READWRITE);
        useCase = new WeakReference<>(createUsecase(db));
    }

    public T getUseCase() {
        return useCase.get();
    }

    protected abstract T createUsecase(SQLiteDatabase db);

    @Override
    protected void onCleared() {
        subscriptions.clear();
        if (useCase.get() != null) {
            useCase.get().closeDatabase();
        }
        super.onCleared();
    }
}
