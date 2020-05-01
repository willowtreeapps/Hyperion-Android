package com.willowtreeapps.hyperion.sqlite.presentation.records;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.domain.usecase.records.RecordsViewerUseCase;
import com.willowtreeapps.hyperion.sqlite.domain.usecase.records.RecordsViewerUseCaseImpl;
import com.willowtreeapps.hyperion.sqlite.presentation.DatabaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DbRecordsViewModel extends DatabaseViewModel<RecordsViewerUseCase> {

    public static final int FIELD_TYPE_NULL = 0;
    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_STRING = 3;
    public static final int FIELD_TYPE_BLOB = 4;

    public void createTableRows(final Context context,
                                String tableName, Consumer<List<TableRow>> subscriber) {
        subscriptions.add(getUseCase().fetchRecords(tableName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Cursor, List<TableRow>>() {
                    @Override
                    public List<TableRow> apply(@NonNull Cursor cursor) throws Exception {
                        return getTableRows(context, cursor);
                    }
                })
                .subscribe(subscriber));
    }


    //Derived from https://github.com/infinum/android_dbinspector/blob/master/dbinspector/src/main/java/im/dino/dbinspector/adapters/TablePageAdapter.java
    private List<TableRow> getTableRows(Context context, Cursor cursor) {

        List<TableRow> rows = new ArrayList<>();
        TableRow header = new TableRow(context);
        cursor.moveToFirst();

        int paddingPx = context.getResources().getDimensionPixelSize(R.dimen.hsql_column_padding);

        for (int col = 0; col < cursor.getColumnCount(); col++) {
            TextView textView = new TextView(context);
            textView.setText(cursor.getColumnName(col));
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            header.addView(textView);
        }

        rows.add(header);

        boolean alternate = true;

        if (cursor.getCount() == 0) {
            return rows;
        }

        do {
            TableRow row = new TableRow(context);
            for (int col = 0; col < cursor.getColumnCount(); col++) {
                TextView textView = new TextView(context);
                if (cursor.getType(col) == FIELD_TYPE_BLOB) {
                    //We don't need to display the contents of the blob
                    textView.setText(R.string.hsql_data_type_blob);
                } else {
                    textView.setText(cursor.getString(col));
                }
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
                int rowBgRes;
                if (alternate) {
                    rowBgRes = R.color.hsql_table_row_colour_alt;
                } else {
                    rowBgRes = R.color.hsql_table_row_colour_standard;
                }
                textView.setBackgroundColor(context.getResources().getColor(rowBgRes));
                row.addView(textView);
            }

            alternate = !alternate;
            rows.add(row);

        } while (cursor.moveToNext());

        cursor.close();
        return rows;
    }


    @Override
    protected RecordsViewerUseCase createUsecase(SQLiteDatabase db) {
        return new RecordsViewerUseCaseImpl(db);
    }
}
