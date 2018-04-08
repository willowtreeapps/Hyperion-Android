package com.willowtreeapps.hyperion.sample.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {UsersEntity.class}, version = 1, exportSchema = false)
public abstract class SampleDatabase extends RoomDatabase {
    public static final String NAME = "sample_db";

    private static SampleDatabase INSTANCE;

    public synchronized static SampleDatabase getInstance(@NonNull final Context context) {
        if(INSTANCE == null) {
            INSTANCE = buildDb(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private static SampleDatabase buildDb(final Context context) {
        return Room.databaseBuilder(context,
                SampleDatabase.class, SampleDatabase.NAME)
                .fallbackToDestructiveMigration()
                //Pre-populating database with some sample data
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).getUserDao()
                                        .insert(new UsersEntity("User1"),
                                        new UsersEntity("User2"));
                            }
                        }) .start();
                    }
                })
                .build();
    }

    public abstract UsersEntity.UserDao getUserDao();
}
